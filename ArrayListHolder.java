import java.util.*;



public class ArrayListHolder{
        private ArrayList<Integer> a;
        public ArrayListHolder(ArrayList<Integer> a){
            this.a = a;
        }
        public ArrayList<Integer> toArrayList(){
            if(a.size() > 0){
                return this.a;
            }
            else return null;
        }
        @Override
        public int hashCode(){

            int ret = 0;
            int shift = 0;
            for(int i : a){
                shift = (shift + 11) % 21;
                ret ^= (i + 1024) << shift;
            }
            return ret;
        }
        @Override
        public boolean equals(Object o){
            if(o == null){
                return false;
            }
            else if(o instanceof ArrayListHolder){
                ArrayListHolder b = (ArrayListHolder) o;
                ArrayList<Integer> c = this.toArrayList();
                ArrayList<Integer> d = b.toArrayList();
                return BeltwayV17.equalsArrayList(c,d);
                
            }
        
            return false;
        }
    
    public String toString(){
        return this.a.toString();
    }
    
    public static void main(String[] args){
        ArrayList<Integer> a = new ArrayList<>();
        ArrayList<Integer> b = new ArrayList<>();
        a.add(1);
        b.add(1);
        a.add(2);
        b.add(2);
        ArrayListHolder alh = new ArrayListHolder(a);
        ArrayListHolder alh2 = new ArrayListHolder(b);

        HashMap<ArrayListHolder,Boolean> hm = new HashMap<>();
        hm.put(alh,true);
        System.out.println("Is a in hashmap?: ");
        System.out.println(hm.get(alh));
        System.out.println("Is b in hashmap?: ");
        System.out.println(hm.get(alh2));
        
    }

}
