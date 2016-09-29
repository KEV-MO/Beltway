import java.util.*;

public class ListMapBinder{
    
    private LinkedList<Integer> ll;
    private HashMap<Integer,Integer> distMap;
    private int circumference;
    private int[] distances;
    private int solutionSize;
    
    public ListMapBinder(int[] distances){
        this.distances = distances;
        this.circumference = distances[distances.length-1];
        this.ll = new LinkedList<Integer>();
        ll.add(0);
        this.distMap = initDistMap(distances);
      //  Arrays.sort(this.distances);
        solutionSize = ((int) Math.sqrt(4*distances.length) + 1)/2; //This is off by one because we have two elements for pivot point
    }

    public ListMapBinder(int[] distances, ListMapBinder cop){
        this.distances = distances;
        this.circumference = distances[distances.length-1];
        this.ll = new LinkedList<Integer>(cop.ll);
        this.distMap = new HashMap<>(cop.distMap);
     //   Arrays.sort(this.distances);
        solutionSize = ((int) Math.sqrt(4*distances.length) + 1)/2;
    }
    
    public HashMap<Integer,Integer> initDistMap(int[] distances){
        HashMap<Integer,Integer> ret = new HashMap<>();
        for(int i : distances){
            if(ret.get(i) == null){
                ret.put(i,1);
            }
            else{
                ret.put(i,ret.get(i)+1);
            }
        }
        return ret;
    }
    
    //Assumes that ll has elements in increasing order
    public boolean canAddTurnpike(int nextDist){

        ListIterator<Integer> li = ll.listIterator();
        while(li.hasNext()){
            int i = (int) li.next();
            if(distMap.get(nextDist-i) == null || distMap.get(nextDist-i) < 1){
                return false;
            }
        }
        return true;
    }
    
    public void eraseDistancesTurnpike(int addDist){
        ListIterator<Integer> li = ll.listIterator();
        while(li.hasNext()){
            int i = (int) li.next();
            int x = addDist-i;
            distMap.put(x,distMap.get(x)-1);
        }
    }
    

    
    public void addDistTurnpike(int addDist){
        eraseDistancesTurnpike(addDist);
        ll.addLast(addDist);
    }
    
    public boolean canAddBeltway(int nextDist){
        ListIterator<Integer> li = ll.listIterator();
        while(li.hasNext()){
            int i = (int) li.next();
            int x = nextDist-i;
            if(x == circumference-x){
                if(distMap.get(x) == null || distMap.get(x) < 2){
                    return false;
                }
            }
            if(distMap.get(x) == null || distMap.get(x) < 1 || distMap.get(circumference - x) < 1){
                return false;
            }
        }
        return true;
    }
               
    public void eraseDistancesBeltway(int addDist){
        ListIterator<Integer> li = ll.listIterator();
        while(li.hasNext()){
            int i = (int) li.next();
            int x = addDist-i;
            distMap.put(x,distMap.get(x)-1);
            distMap.put(circumference-x,distMap.get(circumference-x)-1 );
        }
    }
    
    public void addDistBeltway(int addDist){
        if(addDist == circumference){
            ll.addLast(addDist);
            distMap.put(circumference,0);
            return;
        }
        eraseDistancesBeltway(addDist);
        ll.addLast(addDist);

    }

    public void rotateBeltway(){
        //pop first element
        ll.remove();
        updateDistMapBeltway();
        int dec = (int) ll.getFirst();

        ListIterator<Integer> li = ll.listIterator();
        while(li.hasNext()){
            int i = li.next();
            li.set(i-dec);
        }
    }
    public void updateDistMapBeltway(){
        ListIterator<Integer> li = ll.listIterator();
        while(li.hasNext()){
            int i = (int) li.next();
            int x = circumference-i;
            if(x == 0){
                return;
            }
            distMap.put(i,distMap.get(i)+1);
            distMap.put(x,distMap.get(x)+1);
        }
    }




    public void rotateTurnpike(){
        //pop first element
        ll.remove();
        updateDistMapTurnpike();
        int dec = (int) ll.getFirst();
        ListIterator<Integer> li = ll.listIterator();
        while(li.hasNext()){
            int i = li.next();
            li.set(i-dec);
        }
        fillTurnpike();
    }


    public void updateDistMapTurnpike(){
        ListIterator<Integer> li = ll.listIterator();
        while(li.hasNext()){
            int i = (int) li.next();
            distMap.put(i,distMap.get(i)+1);
        }
    }


    public int contains(int search){
        int low = 0;
        int high = distances.length-1;
        int mid = (low+high)/2;
        while(low <= high){
            mid = (low+high)/2;
            if(distances[mid] > search){
                high = mid-1;
            }
            else if(distances[mid] < search){
                low = mid+1;
            }
            else{
                return mid;
            }
        }
        /*Usually we throw an exception here, but sometimes turnpike configurations are not
        in the distance array and we just want to start at the smallest feasible element in that case */
        return Math.max(low,0);
    }

    public void fillTurnpike(){
        int last = ll.getLast();
        int begin = contains(last);
        for(int i = begin; i < distances.length; i++){
            if(canAddTurnpike(distances[i]) && distances[i] > last){ //ensure that we keep ll sorted
                addDistTurnpike(distances[i]);
            }
        }
    }

//For backtrack step, when we want to forgo an element
    public void fillTurnpikeNoLessThan(int notLessThan){
        int last = ll.getLast();
        int begin = contains(last);
        for(int i = begin; i < distances.length; i++){
            if(canAddTurnpike(distances[i]) && distances[i] > notLessThan){
                addDistTurnpike(distances[i]);
            }
        }
    }
//Need to update distmap after removing elements...
    public void backtrack(){

        if(ll.size() < 2){
            return;
        }
        updateDistBeforeRemoveLast();
        int y = ll.removeLast();
        updateDistBeforeRemoveLast();
        int z = ll.removeLast();
        //z+=1;
        fillTurnpikeNoLessThan(z);
    }

    public static boolean contains(HashMap<ListMapBinder,Boolean> collec, ListMapBinder lmb){
        return collec.get(lmb) != null;
    }
    public void backtrackWithMap(HashMap<ListMapBinder,Boolean> collec){
        while(ll.size() > 2 && contains(collec,this)){
            collec.put(this,true);
            updateDistBeforeRemoveLast();
            int y = ll.removeLast();
            updateDistBeforeRemoveLast();
            int z = ll.removeLast();
            z+=1;
            fillTurnpikeNoLessThan(z);
            
        }
    }



    public void updateDistBeforeRemoveLast(){
        ListIterator<Integer> li = ll.listIterator();
        int last = ll.getLast();
        while(li.hasNext()){
            int i = (int) li.next();
            if(i != last){
                if(distMap.get(last-i) != null){
                    distMap.put(last-i,distMap.get(last-i)+1);
                }
            } 
        }
    }


    public int hashCode(){
        Object[] dhc = new Object[this.ll.size()];
        ListIterator<Integer> li = ll.listIterator();
        int i = 0;
        while(li.hasNext()){
            int x = (int) li.next();
            dhc[i] = x;
            i++;
        }
        return Arrays.deepHashCode(dhc);
        //return Arrays.deepHashCode(this.ll);
    }

    public boolean equals(ListMapBinder oth){
        return this.ll.equals(oth.ll);
    }
    public String toString(){
        return this.ll.toString();
    }
    public int size(){
        return ll.size();
    }

    public void printRotation(){
        int n = this.ll.size();
        for(int i = 0; i < n-1; i++){
            System.out.println(this);
            rotateTurnpike();
        }
    }

    public void clear(){
        this.ll.clear();
        this.distMap.clear();
    }





    public boolean checkSolution(){
        if(ll.size() != solutionSize+1){ //It needs to be one more than solutionSize
            return false;
        }
        ListMapBinder check = new ListMapBinder(distances);
        ListIterator<Integer> li = ll.listIterator();
        while(li.hasNext()){
            int n = (int)li.next();
            if(n == 0){
                continue;
            }
            check.addDistBeltway(n);
        }
        for(int i : distances){
            if(check.distMap.get(i) != 0){
                return false;
            }
        }
        return true;
    }

    public boolean checkSolutionTurnpike(){
        if(ll.size() != solutionSize+1){ //It needs to be one more than solutionSize
            return false;
        }

        for(int i : distances){
            if(distMap.get(i) != 0){
                return false;
            }
        }
        return true;
    }
    public static void main(String[] args){
        //tests
        int[] dist = {1,2,3,4,5,6,7,8,9,10,11,12,13};
        ListMapBinder lmb = new ListMapBinder(dist);
        System.out.println(lmb.distMap.toString());
        lmb.fillTurnpike();
        lmb.printRotation();
        System.out.println(lmb.checkSolution());
        ListMapBinder notASolution = new ListMapBinder(dist);
        notASolution.addDistTurnpike(5);
        System.out.println(notASolution.checkSolution());
        ListMapBinder another = new ListMapBinder(dist,lmb);

        System.out.println("another:");
        System.out.println(another);
        System.out.println("Lmb, are they equal?");
        System.out.println(lmb);
        System.out.println(another.equals(lmb));
        System.out.println(another.distMap);
        another.rotateTurnpike();
        System.out.println(another);
        System.out.println(lmb);
        
    }

}
