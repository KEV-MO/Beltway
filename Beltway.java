/* Author: Kevin Wasiluk

For questions or comments you can contact: wasil017@umn.edu

*/

import java.util.*;
public class Beltway{
    //Utility methods
    public static ArrayList<Integer> canonicalForm(ArrayList<Integer> a){

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

    public static void printRotation(int[] distances, ArrayList<Integer> a){
        ArrayList<Integer> test = copyArrList(a);
        int numIntegerPoints = ((int) Math.sqrt(4*distances.length + 1) + 1)/2 + 1;
        System.out.println(a);

        a = greedyRotationMap(a,distances);
        while(!equalsArrayList(test,a)){
            //blockMap = createUnusedBlockMap(distances,numIntegerPoints);

            System.out.println(a);
            a = greedyRotationMap(a,distances);
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


/*
    public static ArrayList<Integer> createGreedyMap(int[] distances){
        int i = 1;
        int w = distances[distances.length-1];
        ArrayList<Integer> a = new ArrayList<>();
        HashMap<Integer,Integer> currDist = createUnusedDistMap(a,distances);
        a.add(0);
        while(i <= w){
            if(canAdd(a,currDist,i,w)){
                eraseDistances(a,currDist,i,w);
                a.add(i);
            }
            i++;
        }

        return a;
    }
*/
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

    public static ArrayList<ArrayList<Integer>> addAllArrays(ArrayList<ArrayList<Integer>> tempCollec, ArrayList<ArrayList<Integer>> collec){
        for(int i = 0; i < tempCollec.size(); i++){
            if(!contains(collec,tempCollec.get(i))){
                collec.add(tempCollec.get(i));
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

    public static ArrayList<Integer> beltwaySolutionsMap(int[] distances, int minimalRotations, int maxRotations){
        ArrayList<Integer> gm = createGreedyMap(distances);
        ArrayList<ArrayList<Integer>> collec = new ArrayList<>();
        return beltwaySolutionsMap(distances,gm,minimalRotations,maxRotations,collec);
    }
    
    public static ArrayList<Integer> beltwaySolutionsMap(int[] distances, int minimalRotations){
        ArrayList<Integer> gm = createGreedyMap(distances);
        ArrayList<ArrayList<Integer>> collec = new ArrayList<>();
        return beltwaySolutionsMap(distances,gm,minimalRotations,Integer.MAX_VALUE, collec);
    }


    public static ArrayList<Integer> beltwaySolutionsMap(int[] distances, 
                                                        ArrayList<Integer> initial, 
                                                        int minimalRotations, 
                                                        int maxRotations,
                                                        ArrayList<ArrayList<Integer>> collec){

        int numIntegerPoints = ((int) Math.sqrt(4*distances.length + 1) + 1)/2 + 1;
        ArrayList<Integer> gr = initial;
        ArrayList<Integer> checkForCycle = new ArrayList<>();
        ArrayList<ArrayList<Integer>> tempCollec = new ArrayList<>();
        ArrayList<Integer> initCop = copyArrList(initial);
        tempCollec.add(initCop);
        int i = 0;
        while(i<maxRotations){
            //It appears that minimalRotations should be at least 3*n
            if(i % minimalRotations == 0){
                checkForCycle = copyArrList(gr);
            }

           // System.out.println(gr);
            gr = greedyRotationMap(gr,distances);
            ArrayList<Integer> grCop = copyArrList(gr);
            if(!contains(tempCollec,grCop)){
                tempCollec.add(grCop);
            }

            if(equalsArrayList(checkForCycle,gr)){
                if(checkForIdenticalDistances(distances,gr)){
                    /*
                    System.out.println("Printing forbidden arrays:");

                    for(ArrayList<Integer> a : collec){
                        System.out.println(a);
                    }
                    
                    System.out.println("Answer found as a result of group generated by: ");
                    System.out.println(initCop);
                    */
                    return gr;
                }
                
                collec = addAllArrays(tempCollec,collec);
                /*
                System.out.println("Size of collec: " + collec.size());
                
                for(int z = 0; z < collec.size(); z++){
                    System.out.println(collec.get(z));
                }
                */
                ArrayList<Integer> next = createNextInitial(collec,distances,initial);
                return beltwaySolutionsMap(distances,next,minimalRotations,maxRotations,collec);
            }

            
            i++;  
        }
        //System.out.println("Recursing at end of rotation algorithm.");
        collec = addAllArrays(tempCollec,collec);

        ArrayList<Integer> next = createNextInitial(collec,distances,initial);
        return beltwaySolutionsMap(distances,next,minimalRotations*2,maxRotations,collec);
        //return new ArrayList<Integer>();
    }

    public static ArrayList<Integer> createNextInitial(ArrayList<ArrayList<Integer>> collec, int[] distances, ArrayList<Integer> initial){
        ArrayList<Integer> test = initial;

        int s = test.size();

       while(contains(collec,test)){
            test.remove(test.size()-1);
            int begin = test.get(test.size()-1);
            if(test.size() > 0){
                test.remove(test.size()-1);
            }
            test = greedilyAdd(distances,test,begin);
        }
        return test;

    }

    public static ArrayList<Integer> backtrack(int[] distances, ArrayList<Integer> gr, int totalRotations, int numIntegerPoints, ArrayList<ArrayList<Integer>> collec){

        gr = buildSetByBacktrack(distances,gr,numIntegerPoints);

        return beltwaySolutionsMap(distances,gr,totalRotations,Integer.MAX_VALUE,collec);
    }


    public static ArrayList<Integer> buildSetByBacktrack(int[] distances, ArrayList<Integer> gr, int numIntegerPoints){ 
        ArrayList<Integer> temp = copyArrList(gr);
        gr.remove(gr.size()-1);
        greedilyAdd(distances,gr,gr.size()-1);
        
        while(equalsArrayList(temp,gr)){
            gr.remove(gr.size()-1);
            gr.remove(gr.size()-2);
            gr = greedilyAdd(distances,gr,gr.size()-1);
        }

        return gr;
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


    public static boolean contains(ArrayList<ArrayList<Integer>> collec, ArrayList<Integer> test){
        if(collec.size() == 0){
            return false;
        }
        for(int i = 0; i < collec.size(); i++){
            ArrayList<Integer> n = collec.get(i);
            if(n.size() != test.size()){
                continue;
            }
            if(equalsArrayList(n,test)){
                return true;
            }
        }
        return false;
    }
/*
    public static ArrayList<Integer> greedilyAdd(int[] distances, ArrayList<Integer> gr, int begin){
        gr = canonicalForm(gr);
        int i = begin;
        int w = distances[distances.length-1];
        ArrayList<Integer> a = new ArrayList<>();
        HashMap<Integer,Integer> currDist = createUnusedDistMap(gr,distances);
        while(i <= w){
            if(canAdd(gr,currDist,i,w)){
                eraseDistances(gr,currDist,i,w);
                gr.add(i);
            }
            i++;
        }
        return gr;
    }
    */
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
    
    public static int[] createRandomCircArrayDense(int numElements, int length){
        int im = 0;
        int[] ret = new int[numElements];
        Random random = new Random();
        for(int i = 1; im < numElements && i < length; i++){
            int rn = length - i;
            int rm = numElements - im;
            int rand = random.nextInt() % length;
            if(rand < rm){
                ret[im++] = i + 1;
            }
            
        }
        return ret;
    }
               
    public static void printArr(int[] circ){
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
//{2, 14, 51, 63, 66, 68, 85, 91, 96, 136, 159, 161, 183, 185, 188}





    public static void main(String[] args){
        int[] circ;
        int correct = 0;
        int incorrect = 0;
        int num = 20;
        while(incorrect < 5 && correct < 50){
            circ = createRandomArrFloyd(num,512);
            int[] t = createDistanceArray(circ);
            
            System.out.println("Circ:");
            printArr(circ);
            /*
            System.out.println("Current beltway: " );
            ArrayList<Integer> circAL = new ArrayList<Integer>();
            circAL.add(0);
            for(int i : circ){
                circAL.add(i);
            }
            printRotation(t,circAL);
            */
            //Occasionally have to put larger number in second argument
            ArrayList<Integer> result = beltwaySolutionsMap(t,num+1,25000);
            if(result.size() > 0){
                
                System.out.println("Answer found");

                //printRotation(t,result);
                
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




}

/*



    public static void main(String[] args){
        int[] circ = {1, 4, 6, 15};
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
        ArrayList<Integer> result = beltwaySolutionsMap(t,500,5000);
        System.out.println("Resulting array found: ");
        System.out.println(result);
        System.out.println("Rotation of array found:");
        printRotation(t,result);
    
        
    }
    public static void main(String[] args){
        int[] circ;
        int correct = 0;
        int incorrect = 0;
        int num = 20;
        while(incorrect < 5 && correct < 10){
            circ = createRandomArrFloyd(num,400);
            int[] t = createDistanceArray(circ);
            /*
            System.out.println("Circ:");
            printArr(circ);
            
            System.out.println("Current beltway: " );
            ArrayList<Integer> circAL = new ArrayList<Integer>();
            circAL.add(0);
            for(int i : circ){
                circAL.add(i);
            }
            printRotation(t,circAL);
            
            //Occasionally have to put larger number in second argument
            ArrayList<Integer> result = beltwaySolutionsMap(t,num+1,25000);
            if(result.size() > 0){
                
                System.out.println("Answer found");

                //printRotation(t,result);
                
                correct++;
                result.clear();
            }
            else{
                System.out.println("Incorrect for: ");
                printArr(circ);
                incorrect++;
                result.clear();
            }
           // System.out.println("-----------------");
            
        }
        System.out.println("Number correct: " + correct);
        System.out.println("Number incorrect: " + incorrect);

    }

    */




/*

---------------------------------
 
 
-------------------------------
        Size of n given n(n-1):
        int x = ((int) Math.sqrt(4*distances.length + 1) + 1)/2;
--------------


*/
