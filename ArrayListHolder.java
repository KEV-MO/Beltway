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
            Object[] toArr = new Object[a.size()];
            for(int i = 0; i < a.size(); i++){
                toArr[i] = a.get(i);
            }
            return Arrays.deepHashCode(toArr);
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

}
