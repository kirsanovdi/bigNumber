import java.util.BitSet;

public class booleanContainer {
    private static final int sectorSize = 32;
    private BitSet bitSet;
    private int absIndex;
    private int size;

    /**
     * копирование в данный экземпляр
     **/
    private void copy(booleanContainer otherBC) {
        bitSet = new BitSet(otherBC.size() * 5);
        size = otherBC.size;
        absIndex = otherBC.size * 2;
        for (int i = 0; i < size(); i++) set(i, otherBC.get(i));
    }

    /**
     * копирование в данный экземпляр с заданным объёмом
     **/
    private void copy(booleanContainer otherBC, int customSize) {
        bitSet = new BitSet(customSize + sectorSize * 2);
        size = customSize;
        absIndex = customSize + sectorSize;
        for (int i = 0; i < Math.min(size(), otherBC.size()); i++) set(i, otherBC.get(i));
    }

    /**
     * новый экземпляр с копированием
     **/
    booleanContainer(booleanContainer otherBC) {
        this.copy(otherBC);
    }

    /**
     * новый экземпляр с копированием с заданным объёмом
     **/
    booleanContainer(booleanContainer otherBC, int customSize) {
        this.copy(otherBC, customSize);
    }

    /**
     * новый экземпляр с заданным объёмом
     **/
    booleanContainer(int newSize) {
        bitSet = new BitSet(newSize + sectorSize * 2);
        absIndex = newSize + sectorSize;
        size = newSize;
    }


    /**
     * размер
     **/
    public int size() {
        return size;
    }

    /**
     * присвоение значения value в ячейку с индесом pos
     **/
    public void set(int pos, boolean value) {
        /*if(pos < 0 || pos >= size()) {
            System.out.println("incorrect call set() with pos = " + pos + " in " + this + " where max is " + size());
        }*/
        //System.out.println("absIndex = " + absIndex + ", pos = " + pos + ", bitset.size() = " + bitSet.size());
        bitSet.set(pos + absIndex, value);
    }

    /**
     * присвоение значений value в ячейки с индесом от from(включая) до to(исключая)
     **/
    public void set(int from, int to, boolean value) {
        if (from < 0 || from >= size())
            System.out.println("incorrect call get() with incl = " + from + " in " + this + " where max is " + size());
        if (to < 0 || to > size())
            System.out.println("incorrect call get() with excl = " + to + " in " + this + " where max is " + size());
        //booleanContainer resultBC = new booleanContainer(to - from);
        for (int i = from; i < to; i++) set(i, value);
    }

    /**
     * значение в ячейке с индесом pos
     **/
    public boolean get(int pos) {
        /*if(pos < 0 || pos >= size()) {
            System.out.println("incorrect call get() with pos = " + pos + " in " + this + " where max is " + size());
        }
        if(pos > size()) return false;*/
        //System.out.println("absIndex = " + absIndex + ", pos = " + pos + ", bitset.size() = " + bitSet.size());
        return bitSet.get(pos + absIndex);
    }

    /**
     * значения как booleanContainer из ячеек с индесом от incl(включая) до excl(исключая)
     **/
    public booleanContainer get(int incl, int excl) {
        if (incl < 0 || incl >= size())
            System.out.println("incorrect call get() with incl = " + incl + " in " + this + " where max is " + size());
        if (excl < 0 || excl > size())
            System.out.println("incorrect call get() with excl = " + excl + " in " + this + " where max is " + size());
        booleanContainer resultBC = new booleanContainer(excl - incl);
        for (int i = 0, j = incl; j < excl; i++) resultBC.set(i, get(j++));
        return resultBC;
    }

    /**
     * id ячейки с самым большим индексом, значение коротой равно true
     **/
    public int getBiggestId() {
        for (int i = size() - 1; i >= 0; i--) {
            if (get(i)) return i;
        }
        return 0;
    }

    /**
     * id ячейки с самым маленьким индексом, значение коротой равно true
     **/
    public int getLowestId() {
        for (int i = 0; i < size(); i++) {
            if (get(i)) return i;
        }
        return size() - 1;
    }

    public void clearFree() {
        size = getBiggestId() + 1;
    }

    public void clear() {
        this.set(0, size(), false);
        size = 1;
    }

    public void mul2() {
        absIndex--;
        size++;
        if (absIndex == 0) {
            absIndex = sectorSize;
            BitSet newBitSet = new BitSet(bitSet.size() + sectorSize);
            for (int i = 0; i < size(); i++) newBitSet.set(absIndex + i, bitSet.get(i));
            bitSet = newBitSet;
        }
    }

    public void div2() {
        set(0, false);//чтобы не оставалось положительное значение за границами
        absIndex++;
        size--;
        if (size == 0) {//не работает
            BitSet newBitSet = new BitSet(bitSet.size() + sectorSize);
            size = sectorSize;
            bitSet = newBitSet;
        }
    }
}
