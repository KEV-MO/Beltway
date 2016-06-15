/* Author: Kevin Wasiluk

For Questions contact wasil017@umn.edu

*/
import java.util.*;


public class Beltway{


    //Utility methods
    public static ArrayList<Integer> canonicalForm(ArrayList<Integer> a){
        if(a.size() < 2){
            return a;
        }
        int sub = a.get(0);
        for(int i = 0; i < a.size(); i++){
           a.set(i,a.get(i)-sub);
        }
        return a;
    }
    
    public static int contains(int[] distances, int search){
        int low = 0;
        int high = distances.length-1;
        while(low <= high){
            int mid = (low+high)/2;
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
        return -1;
    }
    
    public static int containsLinear(int[] distances, int search){
        for(int i = 0; i < distances.length; i++){
            if(distances[i] == search){
                return i;
            }
        }
        return -1;
    }
    
    public static int[] copyArr(int[] a){
        int[] ret = new int[a.length];
        for(int i = 0; i < a.length; i++){
            ret[i] = a[i];
        }
        return ret;
    }
    
    public static ArrayList<Integer> copyArrList(ArrayList<Integer> a){
        ArrayList<Integer> ret = new ArrayList<>();
        for(int i : a){
            ret.add(i);
        }
        return ret;
    }

    public static HashMap<Integer,Integer> copyArrToMap(int[] distances){
        HashMap<Integer,Integer> ret = new HashMap<>();
        for(int i = 0; i < distances.length; i++){
            if(ret.get(distances[i]) != null){
                int v = ret.get(distances[i]) + 1;
                ret.put(distances[i],v);
            }
            else{
                ret.put(distances[i], 1);
            }
        }
        return ret;
    }
    
    //End utility

    public static boolean canAdd(ArrayList<Integer> a, HashMap<Integer,Integer> allowedDistances, int b, int L){
        for(int i : a){
            if(allowedDistances.get(b-i) != null && allowedDistances.get(b-i) != 0){
                continue;
            }
            else{
                return false;
            }
        }
        if(L-b != 0){
            if(allowedDistances.get(L-b) != null){
                if(allowedDistances.get(L-b) == 0){
                    return false;
                }
            }
            else{
                return true;
            }
            
        }
        return true;
    }
    //This will only terminate if the list entered is part of the cyclic group
    public static void printRotation(int[] distances, ArrayList<Integer> a){
        ArrayList<Integer> test = copyArrList(a);
        System.out.println(a);
        a = greedyRotationMap(a,distances);
        while(!equalsArrayList(test,a)){
            System.out.println(a);
            a = greedyRotationMap(a,distances);
        }
    }

    public static void printGreedyVectorRotation(int[] distances, ArrayList<Integer> a){
        ArrayList<Integer> test = copyArrList(a);
        ArrayList<Integer> output;
        int numIntegerPoints = ((int) Math.sqrt(4*distances.length + 1) + 1)/2 + 1;
        for(int i = 1; i < numIntegerPoints; i++){
            output = greedyVector(distances,test);
            System.out.println(output);
            test = greedyRotationMap(test,distances);
        }
    }

    public static void eraseDistances(ArrayList<Integer> a, HashMap<Integer,Integer> distances, int b){
        for(int i : a){
            int erVal = distances.get(b-i) - 1;
            distances.put(b-i, erVal);
        }

    }


    
    public static HashMap<Integer,Integer> createUnusedDistMap(ArrayList<Integer> a, int[] distances){
        HashMap<Integer,Integer> ret = copyArrToMap(distances);
        for(int i = 0; i < a.size(); i++){
            for(int j = i+1; j < a.size(); j++){
                int indx = contains(distances,a.get(j)-a.get(i));
                int decValue = ret.get(distances[indx]) - 1;
                ret.put(distances[indx], decValue);
            }
        }
        return ret;
    }

    public static ArrayList<Integer> reflection(ArrayList<Integer> a){
        int sum = a.get(0) + a.get(a.size()-1);
        ArrayList<Integer> ret = new ArrayList<>();
        for(int i = a.size()-1; i >= 0; i--){
            int b = a.get(i);
            ret.add(sum-b);

        }
        
        return ret;
    }




    public static ArrayList<Integer> greedyRotationMap(ArrayList<Integer> a, int[] distances){
        if(a.size() < 2){
            return new ArrayList<Integer>();
        }
        a.remove(0);
        a = canonicalForm(a);
        int w = distances[distances.length-1];
        HashMap<Integer,Integer> currDist = createUnusedDistMap(a,distances);
        int k = a.get(a.size()-1);
        for(int i = contains(distances,k); i < distances.length; i++){
            if(canAdd(a,currDist,distances[i],w)){
                eraseDistances(a,currDist,distances[i]);
                a.add(distances[i]);
            }
        }
        return a;
    }


    public static ArrayList<Integer> createGreedyMap(int[] distances){
        int w = distances[distances.length-1];
        ArrayList<Integer> a = new ArrayList<>();
        HashMap<Integer,Integer> currDist = createUnusedDistMap(a,distances);
        a.add(0);
        for(int i : distances){
            if(canAdd(a,currDist,i,w)){
                eraseDistances(a,currDist,i);
                a.add(i);
            }
        }

        return a;
    }
    //Distances is original distance set, gr is just one rotation
    public static boolean checkForIdenticalDistances(int[] distances, ArrayList<Integer> gr){
        int[] circ = new int[gr.size()-1];
        for(int i = 0; i < gr.size()-1; i++){
            circ[i] = gr.get(i+1);
        }
        int[] out = createDistanceArray(circ);
        boolean ret = true;
        if(out.length != distances.length){
            return false;
        }
        for(int i = 0; i < out.length; i++){
            if(out[i] != distances[i]){
                return false;
            }
        }
        return ret;
    }
//addAllArrays not needed in current version
    public static HashMap<ArrayListHolder,Boolean> addAllArrays(HashMap<ArrayListHolder,Boolean> tempCollec, HashMap<ArrayListHolder,Boolean> collec){
        Iterator itr = tempCollec.entrySet().iterator();
        while(itr.hasNext()){
            Map.Entry pair = (Map.Entry)itr.next();
            ArrayListHolder a = (ArrayListHolder)pair.getKey();
            if(!contains(collec,a)){
                collec.put(a,true);
            }
        }
        return collec;
    }
    
    public static boolean equalsArrayList(ArrayList<Integer> a, ArrayList<Integer> b){
        if(a.size() != b.size()){
            return false;
        }
        for(int i = 0; i < a.size(); i++){
            int c = a.get(i);
            int d = b.get(i);
            if(c != d){
                return false;
            }
        }
        return true;
    }

    
    public static ArrayList<Integer> beltwaySolutionsMap(int[] distances){
        ArrayList<Integer> gm = createGreedyMap(distances);
        return beltwaySolutionsMap(distances,gm);
    }

    public static boolean contains(HashMap<ArrayListHolder,Boolean> hm, ArrayListHolder a){
        return hm.get(a) != null;
    }


    public static ArrayList<Integer> beltwaySolutionsMap(int[] distances, ArrayList<Integer> initial){

        ArrayList<Integer> gr = initial;
        HashMap<ArrayListHolder,Boolean> tempCollec = new HashMap<>();
        ArrayList<Integer> initCop = copyArrList(initial);
        tempCollec.put(new ArrayListHolder(initCop),true);
        while(true){
            if(gr.size() < 1){
                return new ArrayList<Integer>();
            }
            gr = greedyRotationMap(gr,distances);


            ArrayListHolder grCop = new ArrayListHolder(gr);
            if(!contains(tempCollec,grCop)){
                tempCollec.put(grCop,true);
            }
            else{
                if(checkForIdenticalDistances(distances,gr)){
                    return gr;
                }
                gr = createNextInitial(tempCollec,distances,initial);
                
                ArrayListHolder grTemp = new ArrayListHolder(gr);
                if(contains(tempCollec,grTemp)){
                    return new ArrayList<Integer>();
                }
                else{
                    tempCollec.put(grTemp,true);
                }
                

            }
        }
    }
    //
    public static ArrayList<Integer> createNextInitial(HashMap<ArrayListHolder,Boolean> collec, int[] distances, ArrayList<Integer> initial){
        ArrayList<Integer> test = initial;
        ArrayListHolder t = new ArrayListHolder(test);
        HashMap<ArrayListHolder,Boolean> loopCheck = new HashMap<>();

        int i = 0;
        while(contains(collec,t)){
            if(contains(loopCheck,t)){
                return new ArrayList<Integer>();
            }
            else{
                loopCheck.put(t,true);
            }
            test.remove(test.size()-1);
            int begin = test.get(test.size()-1);
            if(test.size() > 0){
                test.remove(test.size()-1);
            }
            test = greedilyAdd(distances,test,begin);
            t = new ArrayListHolder(test);
            i++;
        }
        return test;
        
    }


    public static ArrayList<Integer> greedilyAdd(int[] distances, ArrayList<Integer> gr, int begin){
        gr = canonicalForm(gr);
        int w = distances[distances.length-1];
        ArrayList<Integer> a = new ArrayList<>();
        HashMap<Integer,Integer> currDist = createUnusedDistMap(gr,distances);
        for(int i : distances){
            if(canAdd(gr,currDist,i,w) && i > begin){
                eraseDistances(gr,currDist,i);
                gr.add(i);
            }
        }
        return gr;
    }

    public static ArrayList<Integer> greedilyAddSingle(int[] distances, ArrayList<Integer> gr, int begin){
        gr = canonicalForm(gr);
        int w = distances[distances.length-1];
        ArrayList<Integer> a = new ArrayList<>();
        HashMap<Integer,Integer> currDist = createUnusedDistMap(gr,distances);
        for(int i : distances){
            if(canAdd(gr,currDist,i,w) && i > begin){
                eraseDistances(gr,currDist,i);
                gr.add(i);
                break;
            }
        }
        return gr;
    }

    //Assumes we have some number of elements, NOT including 0, largest element is circumference of beltway
    public static int[] createDistanceArray(int[] circ){
        int L = circ[circ.length-1];
        ArrayList<Integer> intm = new ArrayList<Integer>();
        intm.add(L);
        for(int i = 0; i < circ.length; i++){
            for(int j = 0; j < circ.length; j++){
                int num = circ[j] - circ[i];
                if(num < 0){
                    intm.add(((num % L) + L) % L);
                }
                else if(num > 0){
                    intm.add(num);
                }
            }
        }
        
        Collections.sort(intm);
        int[] t = new int[intm.size()];
        for(int i = 0; i < intm.size(); i++){
            t[i] = intm.get(i);
        }
        return t;
    }
    public static ArrayList<Integer> greedyVector(int[] distances, ArrayList<Integer> a){
        ArrayList<Integer> ret = new ArrayList<Integer>();
        ArrayList<Integer> addTo = new ArrayList<>();
        addTo.add(0);
        for(int i = 1; i < a.size(); i++){
            int ad = 0;
            if(i == 1){
                addTo = greedilyAddSingle(distances,addTo,0);
            }
            else{
                addTo = greedilyAddSingle(distances,addTo,addTo.get(i-1));
            }

            while(!addTo.get(i).equals(a.get(i))){
                ad++;
                int nextBegin = addTo.get(i);
                addTo.remove(i);
                addTo = greedilyAddSingle(distances,addTo,nextBegin);
            }
            ret.add(ad);
        }
        ret.remove(ret.size()-1);
        return ret;

    }
    
               
    public static void printArr(int[] circ){
        System.out.print("{");
        int last = circ[circ.length-1];
        if(circ[0] != 0){
            System.out.print(0 + ", ");
        }
        for(int i : circ){
            System.out.print(i);
            if(i != last){
                System.out.print(", ");
            }
        }
        System.out.print("}");
        System.out.println();
    }
    public static void printArrNoZero(int[] circ){
        System.out.print("{");
        int last = circ[circ.length-1];
        for(int i : circ){
            System.out.print(i);
            if(i != last){
                System.out.print(", ");
            }
        }
        System.out.print("}");
        System.out.println();
    }

    public static int[] createRandomArrRepeat(int numElements, int length){
        int r;
        int[] ret = new int[numElements];
        Random rand = new Random();
        int i = 0;
        while(i < numElements){
            int x = Math.abs(rand.nextInt() % length);
            if(x == 0){
                continue;
            }
            else{
                ret[i] = x;
                i++;
            }
        }
        Arrays.sort(ret);

        ret[ret.length-1] = ret[0] + ret[ret.length-2];
        return ret;
    }

    public static int[] createRandomArrFloyd(int numElements, int length){
        boolean[] b = new boolean[length+1];
        Arrays.fill(b,false);
        int im = 0;
        int r;
        int[] ret = new int[numElements];
        Random rand = new Random();
        for(int i = length-numElements; i < length && im < numElements; ++i){
            r = Math.abs(rand.nextInt() % (i + 1));
            if(b[r]){
                r = i;
            }
            ret[im++] = r + 1;
            b[r] = true;
        }
        Arrays.sort(ret);
        return ret;
    }

//This requires us to put the full length of the cycle in the last element, so we will have n(n-1) + 1 elements in array.

    public static void main(String[] args){
        for(int i = 0; i < 5; i++){
            int[] t = createRandomArrRepeat(381,1000);
            printArrNoZero(t);            
            System.out.println("Attempting to find solution...");

            ArrayList<Integer> result = beltwaySolutionsMap(t);
            if(result.size() > 0){
                System.out.println("Resulting array found: ");
                System.out.println(result);
                System.out.println("Rotation of array found:");
                printRotation(t,result);
                System.out.println("Vectors of rotations:");
                printGreedyVectorRotation(t,result);
            }
            else{
                System.out.println("No solution found");
            }
            result.clear();
        }

    }




    
        
    
}





/*

    public static void main(String[] args){
        int[] t = {1,5,20,25,72,150,225};

        System.out.println("Attempting to find solution...");
        ArrayList<Integer> result = beltwaySolutionsMap(t);
        if(result.size() > 0){
            System.out.println("Resulting array found: ");
            System.out.println(result);
            System.out.println("Rotation of array found:");
            printRotation(t,result);
            System.out.println("Vectors of rotations:");
            printGreedyVectorRotation(t,result);
        }
        else{
            System.out.println("No solution found");
        }

    }



    public static void main(String[] args){
        int[] circ = {6, 12, 21, 23, 24, 25, 32, 43, 63, 67, 71, 86, 102, 105, 121, 123, 172, 184, 188, 199};
        int[] t = createDistanceArray(circ);
        System.out.println("Distance set:");
        printArr(t);
        ArrayList<Integer> circAL = new ArrayList<>();
        circAL.add(0);
        for(int i : circ){
            circAL.add(i);
        }
        System.out.println("Solution...");
        printRotation(t,circAL);
        System.out.println("Attempting to find solution...");
        ArrayList<Integer> result = beltwaySolutionsMap(t);
        System.out.println("Resulting array found: ");
        System.out.println(result);
        System.out.println("Rotation of array found:");
        printRotation(t,result);
        System.out.println("Vectors of rotations:");
        printGreedyVectorRotation(t,result);

    }
     public static void main(String[] args){
        int[] circ;
        int correct = 0;
        int incorrect = 0;
        int num = 10;
        while(incorrect < 5 && correct < 10){
            circ = createRandomArrFloyd(num,40);
            int[] t = createDistanceArray(circ);
            System.out.println("Original integer points:");
            printArr(circ);
            
            System.out.println("Current beltway: " );
            ArrayList<Integer> circAL = new ArrayList<Integer>();
            circAL.add(0);
            for(int i : circ){
                circAL.add(i);
            }
            printRotation(t,circAL);
            
            //Occasionally have to put larger number in second argument
            ArrayList<Integer> result = beltwaySolutionsMap(t);
            if(result.size() > 0){
                
                System.out.println("Answer found");

                printGreedyVectorRotation(t,result);

                
                correct++;
                result.clear();
            }
            else{
                System.out.println("Incorrect for: ");
                printArr(circ);
                incorrect++;
                result.clear();
            }
            System.out.println("-----------------");
        }
        System.out.println("Number correct: " + correct);
        System.out.println("Number incorrect: " + incorrect);
    }


    public static void main(String[] args){
        for(int i = 0; i < 500; i++){
            int[] t = createRandomArrRepeat(7,20);
            printArrNoZero(t);            
            System.out.println("Attempting to find solution...");

            ArrayList<Integer> result = beltwaySolutionsMap(t);
            if(result.size() > 0){
                System.out.println("Resulting array found: ");
                System.out.println(result);
                System.out.println("Rotation of array found:");
                printRotation(t,result);
                System.out.println("Vectors of rotations:");
                printGreedyVectorRotation(t,result);
            }
            else{
                System.out.println("No solution found");
            }
            result.clear();
        }

    }
    




/*

---------------------------------
 
 
-------------------------------
        Size of n given n(n-1):
        int x = ((int) Math.sqrt(4*distances.length + 1) + 1)/2;
--------------


*/
