import java.util.BitSet;

public class booleanContainer {
    private BitSet bitSet;
    private int size;
    /**копирование в данный экземпляр**/
    public void copy(booleanContainer otherBC){
        bitSet = new BitSet(otherBC.size());
        size = otherBC.size;
        for(int i = 0; i < size(); i++) set(i, otherBC.get(i));
    }
    /**копирование в данный экземпляр с заданным объёмом**/
    public void copy(booleanContainer otherBC, int customSize){
        bitSet = new BitSet(customSize);
        size = customSize;
        for(int i = 0; i < Math.min(size(), otherBC.size()); i++) set(i, otherBC.get(i));
    }
    /**новый экземпляр с копированием**/
    booleanContainer(booleanContainer otherBC){
        this.copy(otherBC);
    }
    /**новый экземпляр с копированием с заданным объёмом**/
    booleanContainer(booleanContainer otherBC, int customSize){
        this.copy(otherBC, customSize);
    }
    /**новый экземпляр с заданным объёмом**/
    booleanContainer(int newSize){
        bitSet = new BitSet(newSize);
        size = newSize;
    }
    /**размер**/
    public int size(){ return size; }
    /**присвоение значения value в ячейку с индесом pos**/
    public void set(int pos, boolean value){
        if(pos < 0 || pos >= size()) {
            System.out.println("incorrect call set() with pos = " + pos + " in " + this + " where max is " + size());
        }
        bitSet.set(pos, value);
    }
    /**присвоение значений value в ячейки с индесом от from(включая) до to(исключая)**/
    public void set(int from, int to, boolean value){
        if(from < 0 || from >= size()) System.out.println("incorrect call get() with incl = " + from + " in " + this + " where max is " + size());
        if(to < 0 || to > size()) System.out.println("incorrect call get() with excl = " + to + " in " + this + " where max is " + size());
        booleanContainer resultBC = new booleanContainer(to - from);
        for(int i = from; i < to; i++) resultBC.set(i, value);
    }
    /**значение в ячейке с индесом pos**/
    public boolean get(int pos){
        /*if(pos < 0 || pos >= size()) {
            System.out.println("incorrect call get() with pos = " + pos + " in " + this + " where max is " + size());
        }*/
        if(pos > size()) return false;
        return bitSet.get(pos);
    }
    /**значения как booleanContainer из ячеек с индесом от incl(включая) до excl(исключая)**/
    public booleanContainer get(int incl, int excl){
        if(incl < 0 || incl >= size()) System.out.println("incorrect call get() with incl = " + incl + " in " + this + " where max is " + size());
        if(excl < 0 || excl > size()) System.out.println("incorrect call get() with excl = " + excl + " in " + this + " where max is " + size());
        booleanContainer resultBC = new booleanContainer(excl - incl);
        for(int i = 0, j = incl; j < excl; i++) resultBC.set(i, get(j++));
        return resultBC;
    }
    /**id ячейки с самым большим индексом, значение коротой равно true**/
    public int getBiggestId(){
        for(int i = size() - 1; i >= 0; i--){
            if(get(i)) return i;
        }
        return 0;
    }
    /**id ячейки с самым маленьким индексом, значение коротой равно true**/
    public int getLowestId() {
        for (int i = 0; i < size(); i++) {
            if (get(i)) return i;
        }
        return size() - 1;
    }
    public void clearFree(){
        size = getBiggestId() + 1;
    }
}
