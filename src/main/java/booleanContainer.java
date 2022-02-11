import java.util.BitSet;

public class booleanContainer {
    private BitSet bitSet;
    private int size;
    public void copy(booleanContainer otherBC){
        bitSet = new BitSet(otherBC.size());
        size = otherBC.size;
        for(int i = 0; i < size(); i++) set(i, otherBC.get(i));
    }
    public void copy(booleanContainer otherBC, int customSize){
        bitSet = new BitSet(customSize);
        size = customSize;
        for(int i = 0; i < Math.min(size(), otherBC.size()); i++) set(i, otherBC.get(i));
    }
    booleanContainer(booleanContainer otherBC){
        this.copy(otherBC);
    }
    booleanContainer(booleanContainer otherBC, int customSize){
        this.copy(otherBC, customSize);
    }
    booleanContainer(int newSize){
        bitSet = new BitSet(newSize);
        size = newSize;
    }
    public int size(){ return size; }
    public void set(int pos, boolean value){
        if(pos < 0 || pos >= size()) {
            System.out.println("incorrect call set() with pos = " + pos + " in " + this + " where max is " + size());
        }
        bitSet.set(pos, value);
    }
    public void set(int from, int to, boolean value){
        if(from < 0 || from >= size()) System.out.println("incorrect call get() with incl = " + from + " in " + this + " where max is " + size());
        if(to < 0 || to > size()) System.out.println("incorrect call get() with excl = " + to + " in " + this + " where max is " + size());
        booleanContainer resultBC = new booleanContainer(to - from);
        for(int i = 0, j = from; i < to; i++) resultBC.set(i, value);
    }
    public boolean get(int pos){
        if(pos < 0 || pos >= size()) {
            System.out.println("incorrect call get() with pos = " + pos + " in " + this + " where max is " + size());
        }
        return bitSet.get(pos);
    }
    public booleanContainer get(int incl, int excl){
        if(incl < 0 || incl >= size()) System.out.println("incorrect call get() with incl = " + incl + " in " + this + " where max is " + size());
        if(excl < 0 || excl > size()) System.out.println("incorrect call get() with excl = " + excl + " in " + this + " where max is " + size());
        booleanContainer resultBC = new booleanContainer(excl - incl);
        for(int i = 0, j = incl; j < excl; i++) resultBC.set(i, get(j++));
        return resultBC;
    }

    public int getBiggestId(){
        for(int i = size() - 1; i >= 0; i--){
            if(get(i)) return i;
        }
        return 0;
    }
    public int getLowestId() {
        for (int i = 0; i < size(); i++) {
            if (get(i)) return i;
        }
        return size() - 1;
    }
}
