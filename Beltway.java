/* Author: Kevin Wasiluk

For Questions contact wasil017@umn.edu

*/
import java.util.*;


public class Beltway{



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



    public static boolean distInvariantSatisfied(int[] distances){
        HashMap<Integer,Integer> ch = copyArrToMap(distances);
        int w = distances[distances.length-1];
        for(int i : distances){
            if(i == w){
                continue;
            }
            if(ch.get(i) != ch.get(w-i)){
                return false;
            }
        }
        return true;
    }


    public static ListMapBinder findRotationSolution(int[] distances){
        ListMapBinder initial = new ListMapBinder(distances);
        initial.fillTurnpike();
        while(initial.size() > 2){
            ListMapBinder test = checkRotation(distances,initial);
            if (test.checkSolution()){
                return test;
            }
            initial.backtrack();
        }
        return new ListMapBinder(distances);
    }


    public static ListMapBinder checkRotation(int[] distances, ListMapBinder initial){
        ListMapBinder lmb1 = new ListMapBinder(distances,initial);
        ListMapBinder lmb2 = new ListMapBinder(distances,initial);
        lmb1.rotateTurnpike();
        lmb1.rotateTurnpike();
        while(!lmb1.equals(lmb2)){
            lmb2.rotateTurnpike();
            lmb1.rotateTurnpike();
            lmb1.rotateTurnpike();
        }
        return lmb1;
    }

    public static ListMapBinder findRotationSolutionPoorSpace(int[] distances){
        ListMapBinder initial = new ListMapBinder(distances);
        initial.fillTurnpike();
        HashMap<ListMapBinder,Boolean> collec = new HashMap<>();
        while(initial.size() > 2){
            ListMapBinder test = checkRotationPoorSpace(distances,initial,collec);
            if (test.checkSolution()){

                return test;
            }
            collec.put(initial,true);
            initial.backtrackWithMap(collec);

        }
        return new ListMapBinder(distances);
    }


    public static ListMapBinder checkRotationPoorSpace(int[] distances, ListMapBinder initial,HashMap<ListMapBinder,Boolean> collec){
        ListMapBinder rotate = new ListMapBinder(distances,initial);
        while(!contains(collec,rotate)){
            collec.put(rotate,true);

            rotate.rotateTurnpike();
        }
        return rotate;
    }

    public static boolean contains(HashMap<ListMapBinder,Boolean> collec, ListMapBinder rotate){
        return collec.get(rotate) != null;
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

    public static int[] createTurnpikeDistanceArray(int[] t){
        int L = t[t.length-1];
        ArrayList<Integer> intm = new ArrayList<Integer>();
        intm.add(L);
        for(int i = 0; i < t.length; i++){
            for(int j = 0; j < t.length; j++){
                int num = t[j] - t[i];

                if(num > 0){
                    intm.add(num);
                }
            }
        }
        
        Collections.sort(intm);
        int[] ret = new int[intm.size()];
        for(int i = 0; i < intm.size(); i++){
            ret[i] = intm.get(i);
        }
        return ret;
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
        return ret;
    }
//Random arr that satisifes invariant that #dist = i, == #dist = w-i
    public static int[] createRandomArrSat(int numElements, int length){
        int[] n = createRandomArrRepeat(numElements/2, length/2);
        //Final element is circumference, thus numElements+1
        int[] ret = new int[numElements+1];
        for(int i = 0; i < n.length; i++){
            ret[i] = n[i];
        }
        int z = 0;
        for(int i = n.length; i < ret.length-1; i++){
            ret[i] = length-ret[z];
            z++;
        }
        Arrays.sort(ret);
        ret[0] = length;
        Arrays.sort(ret);
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



    public static void runReconstruction(int numElements, int length, int tests){
        int[] circ;
        int correct = 0;
        int incorrect = 0;
        while(incorrect < 5 && correct < tests){
            circ = createRandomArrFloyd(numElements,length);
            int[] t = createDistanceArray(circ);
            
            System.out.println("Original integer points:");
            printArr(circ);
            
            System.out.println("Current beltway: " );
            ListMapBinder result = findRotationSolution(t);
            if(result.size() > 0){
                
                System.out.println("Answer found");

                result.printRotation();
                
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
        int numElements = Integer.parseInt(args[0]);
        int length = Integer.parseInt(args[1]);
        int tests = Integer.parseInt(args[2]);
        runReconstruction(numElements,length,tests);

    }





}
