//import java.util.BitSet;

public class bigNumber implements Comparable<bigNumber> {
    private booleanContainer intContainer;
    private booleanContainer dotContainer;
    private boolean isNegative;

    /**
     * новый экземпляр, только для сложения
     **/
    private bigNumber(booleanContainer bs) {
        intContainer = new booleanContainer(bs);
        dotContainer = new booleanContainer(1);
    }

    /**
     * новый экземпляр с копированием bigNumber
     **/
    private bigNumber(bigNumber otherBD) {
        intContainer = new booleanContainer(otherBD.intContainer);
        dotContainer = new booleanContainer(otherBD.dotContainer);
        isNegative = otherBD.isNegative;
    }

    /**
     * новый экземпляр с копированием bigNumber с указанными размерами
     **/
    private bigNumber(bigNumber otherBD, int csInt, int csDot) {
        intContainer = new booleanContainer(otherBD.intContainer, csInt);
        dotContainer = new booleanContainer(otherBD.dotContainer, csDot);
        isNegative = otherBD.isNegative;
    }

    /**
     * новый экземпляр с указанными размерами
     **/
    private bigNumber(int intBitCount, int dotBitCount) {
        intContainer = new booleanContainer(intBitCount);
        dotContainer = new booleanContainer(dotBitCount);
        //isNegative = false;
    }

    /**
     * число из int
     **/
    bigNumber(int number) {
        intContainer = new booleanContainer(32);
        dotContainer = new booleanContainer(1);
        isNegative = number < 0;
        number = Math.abs(number);
        int i = 0;
        while (number > 0) {
            intContainer.set(i, number % 2 == 1);
            number /= 2;
            i++;
        }
    }

    /**
     * число из long
     **/
    bigNumber(long number) {
        intContainer = new booleanContainer(64);
        dotContainer = new booleanContainer(1);
        isNegative = number < 0;
        number = Math.abs(number);
        int i = 0;
        while (number > 0) {
            intContainer.set(i, number % 2 == 1);
            number /= 2;
            i++;
        }
    }

    /**
     * число из double с заданной двоичной точностью
     **/
    bigNumber(double number, int precision) {
        intContainer = new booleanContainer(256);
        dotContainer = new booleanContainer(precision);
        isNegative = number < 0;
        number = Math.abs(number);
        int i = 0;
        long intNumber = (long) number;
        double dotNumber = number - intNumber;
        while (intNumber > 0) {
            intContainer.set(i, intNumber % 2 == 1);
            intNumber /= 2;
            i++;
        }
        for (i = 0; i < precision; i++) {
            dotNumber *= 2;
            dotContainer.set(i, dotNumber >= 1);
            dotNumber = dotNumber - (int) dotNumber;
        }
    }

    /**
     * число из float с заданной двоичной точностью
     **/
    bigNumber(float number, int precision) {
        intContainer = new booleanContainer(256);
        dotContainer = new booleanContainer(precision);
        isNegative = number < 0;
        number = Math.abs(number);
        int i = 0;
        long intNumber = (long) number;
        float dotNumber = number - intNumber;
        while (intNumber > 0) {
            intContainer.set(i, intNumber % 2 == 1);
            intNumber /= 2;
            i++;
        }
        for (i = 0; i < precision; i++) {
            dotNumber *= 2;
            dotContainer.set(i, dotNumber >= 1);
            dotNumber = dotNumber - (int) dotNumber;
        }
    }

    /**
     * число из string
     **/
    bigNumber(String str) {
        bigNumber result = new bigNumber(1, 1);
        String[] s = str.replace("-", "").split("\\.");
        bigNumber auxBN = new bigNumber(0);
        bigNumber bn10 = new bigNumber(10);
        bigNumber bn01 = new bigNumber(0.1, 128);
        for (int i = 0; i < s[0].length(); i++) {
            result = mul(result, bn10);
            result = sum(result, new bigNumber(s[0].charAt(i) - '0'));
        }
        for (int i = s[1].length() - 1; i >= 0; i--) {
            auxBN = sum(auxBN, new bigNumber(s[1].charAt(i) - '0'));
            auxBN = mul(auxBN, bn01);
        }
        result = sum(result, auxBN);

        this.isNegative = str.contains("-");
        this.intContainer = new booleanContainer(result.intContainer);
        this.dotContainer = new booleanContainer(result.dotContainer);
    }

    private void clearFree() {//не работает
        intContainer.clearFree();
        dotContainer.clearFree();
    }
    /*private BitSet bitsetCopy(BitSet bitSet){
        BitSet result = new BitSet(bitSet.size());
        for(int i = 0; i < bitSet.size(); i++) result.set(i, bitSet.get(i));
        return result;
    }*/

    /**
     * самая большая степень 2 в целой части числа
     **/
    private int getLeadId() {
        return intContainer.getBiggestId();
    }

    /**
     * самая большая стерень 0.5 в дробной части числа
     **/
    private int getLastId() {
        return dotContainer.getBiggestId();
    }

    /**
     * значение из целой части числа под индексом i
     **/
    private boolean getI(int i) {
        return intContainer.get(i);
    }

    /**
     * значение из дробной части числа под индексом i
     **/
    private boolean getD(int i) {
        return dotContainer.get(i);
    }

    /**
     * значение из целой части числа под индексом i как int
     **/
    private int getAsInt(int id) {
        return intContainer.get(id) ? 1 : 0;
    }

    /**
     * значение из дробной части числа под индексом i как int
     **/
    private int getAsDot(int id) {
        return dotContainer.get(id) ? 1 : 0;
    }

    /**
     * копирование значений в текущий экземпляр
     **/
    private void copyFrom(bigNumber bd) {
        intContainer = new booleanContainer(bd.intContainer);
        dotContainer = new booleanContainer(bd.dotContainer);
    }

    /****/
    private boolean isAbsBigger(bigNumber bd) {
        if (getLeadId() > bd.getLeadId()) return true;
        if (getLeadId() < bd.getLeadId()) return false;
        for (int i = getLeadId(); i >= 0; i--) {
            if (intContainer.get(i) && !bd.intContainer.get(i)) return true;
            if (!intContainer.get(i) && bd.intContainer.get(i)) return false;
        }
        for (int i = 0; i <= Math.min(getLastId(), bd.getLastId()); i++) {
            if (dotContainer.get(i) && !bd.dotContainer.get(i)) return true;
            if (!dotContainer.get(i) && bd.dotContainer.get(i)) return false;
        }
        return getLastId() >= bd.getLastId();
    }

    /****/
    public int toInt() {
        int biggestId = intContainer.getBiggestId();
        if (biggestId > 31) throw new IllegalStateException();
        int result = 0;
        int mod = 1;
        for (int i = 0; i <= biggestId; i++) {
            result += getAsInt(i) * mod;
            mod *= 2;
        }
        return result * (isNegative ? -1 : 1);
    }

    /****/
    public double toDouble() {
        int biggestId = intContainer.getBiggestId();
        int lowestId = dotContainer.getBiggestId();
        //if(biggestId > 31) throw new IllegalStateException();
        double intResult = 0;
        double dotResult = 0;
        double mod = 1;
        for (int i = 0; i <= biggestId; i++) {
            intResult += getAsInt(i) * mod;
            mod *= 2;
        }
        double modD = 0.5;
        for (int i = 0; i <= lowestId; i++) {
            dotResult += getAsDot(i) * modD;
            modD /= 2.0;
        }
        double result = intResult + dotResult;
        return result * (isNegative ? -1 : 1);
    }

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder(".");
        for (int i = 0; i <= getLeadId(); i++) s.insert(0, (getI(i) ? "1" : "0"));
        for (int i = 0; i <= getLastId(); i++) s.append(getD(i) ? "1" : "0");
        return s.toString();
    }

    public int compareTo(bigNumber otherBN) {
        if (getLeadId() > otherBN.getLeadId()) return 1;
        if (getLeadId() < otherBN.getLeadId()) return -1;
        for (int i = getLeadId(); i >= 0; i--) {
            if (intContainer.get(i) && !otherBN.intContainer.get(i)) return 1;
            if (!intContainer.get(i) && otherBN.intContainer.get(i)) return -1;
        }
        for (int i = 0; i <= Math.min(getLastId(), otherBN.getLastId()); i++) {
            if (dotContainer.get(i) && !otherBN.dotContainer.get(i)) return 1;
            if (!dotContainer.get(i) && otherBN.dotContainer.get(i)) return -1;
        }
        return getLastId() - otherBN.getLastId();
    }

    private void intMul2() {
        intContainer.mul2();
    }

    private void intDiv2() {
        intContainer.div2();
    }

    public static bigNumber sum(bigNumber bd1, bigNumber bd2) {
        if (bd2.isAbsBigger(bd1)) {
            bigNumber temp = bd1;
            bd1 = bd2;
            bd2 = temp;
        }
        bigNumber resultBD = new bigNumber(bd1, Math.max(bd1.getLeadId(), bd2.getLeadId()) + 2,
                Math.max(bd1.getLastId(), bd2.getLastId()) + 1);
        boolean minded = false;

        if (bd1.isNegative == bd2.isNegative) {
            int i;
            for (i = bd2.getLastId(); i >= 0; i--) {
                boolean bd2d = bd2.getD(i);
                boolean resBDd = resultBD.getD(i);
                boolean toAdd = (bd2d == minded) == resBDd;
                minded = resBDd && bd2d || resBDd && minded || bd2d && minded;
                resultBD.dotContainer.set(i, toAdd);
            }
            for (i = 0; i <= bd2.getLeadId(); i++) {
                boolean bd2i = bd2.getI(i);
                boolean resBDi = resultBD.getI(i);
                boolean toAdd = (bd2i == minded) == resBDi;
                minded = resBDi && bd2i || resBDi && minded || bd2i && minded;
                resultBD.intContainer.set(i, toAdd);
            }
            while (minded && resultBD.getI(i)) {
                resultBD.intContainer.set(i, false);
                i++;
            }
            if (minded) resultBD.intContainer.set(i, true);
        } else {
            int i;
            for (i = bd2.getLastId(); i >= 0; i--) {
                boolean bd2d = bd2.getD(i);
                boolean resBDd = resultBD.getD(i);
                boolean toAdd = bd2d == (minded == resBDd);
                minded = bd2d && minded && resBDd || !resBDd && (minded || bd2d);
                resultBD.dotContainer.set(i, toAdd);
            }

            for (i = 0; i <= bd2.getLeadId(); i++) {
                boolean bd2i = bd2.getI(i);
                boolean resBDi = resultBD.getI(i);
                boolean toAdd = bd2i == (minded == resBDi);
                minded = bd2i && minded && resBDi || !resBDi && (minded || bd2i);
                resultBD.intContainer.set(i, toAdd);
            }
            while (minded && !resultBD.getI(i)) {
                resultBD.intContainer.set(i, true);
                i++;
            }
            if (minded) resultBD.intContainer.set(i, false);
        }
        resultBD.isNegative = bd1.isNegative;
        resultBD.clearFree();
        return resultBD;
    }

    public static bigNumber sub(bigNumber bd1, bigNumber bd2) {
        bigNumber temp = new bigNumber(bd2);
        temp.isNegative = !bd2.isNegative;
        return sum(bd1, temp);
    }

    public static bigNumber mul(bigNumber bd1, bigNumber bd2) {

        int posInt = bd1.getLeadId() + bd2.getLeadId() + 1;
        int posDot = bd1.getLastId() + bd2.getLastId() + 1;

        bigNumber resultBD = new bigNumber(posInt + 1, posDot + 1);
        bigNumber auxBD = new bigNumber(1, 1);

        booleanContainer totalBS1 = new booleanContainer(bd1.getLeadId() + bd1.getLastId() + 2);
        booleanContainer totalBS2 = new booleanContainer(bd2.getLeadId() + bd2.getLastId() + 2);

        int pos;

        pos = 0;
        for (int i = bd1.getLastId(); i >= 0; i--) totalBS1.set(pos++, bd1.getD(i));
        for (int i = 0; i <= bd1.getLeadId(); i++) totalBS1.set(pos++, bd1.getI(i));
        pos = 0;
        for (int i = bd2.getLastId(); i >= 0; i--) totalBS2.set(pos++, bd2.getD(i));
        for (int i = 0; i <= bd2.getLeadId(); i++) totalBS2.set(pos++, bd2.getI(i));

        bigNumber totalBD2 = new bigNumber(totalBS2);

        for (int i = 0; i < totalBS1.size(); i++) {
            if (totalBS1.get(i)) {
                auxBD = sum(auxBD, totalBD2);
            }
            totalBD2.intMul2();
        }
        int j = 0;

        for (int i = posDot; i >= 0; i--) resultBD.dotContainer.set(i, auxBD.getI(j++));
        for (int i = 0; j < auxBD.intContainer.size(); i++) resultBD.intContainer.set(i, auxBD.getI(j++));
        resultBD.isNegative = bd1.isNegative ^ bd2.isNegative;
        resultBD.clearFree();
        return resultBD;
    }

    public static bigNumber div(bigNumber bd1, bigNumber bd2, int precision) {
        int pos, auxInt, auxSize, j;
        bigNumber resultBD = new bigNumber(precision, precision + 1);//
        auxInt = Math.max(bd1.getLastId(), bd2.getLastId());
        auxSize = bd1.getLeadId() + bd2.dotContainer.getLowestId() + Math.max(precision, auxInt) + 2;
        booleanContainer totalBS1 = new booleanContainer(auxSize + 1);
        booleanContainer totalBS2 = new booleanContainer(auxSize + 1);

        //копирование первого числа в полное двоичное преставление
        pos = auxInt - bd1.getLastId();
        for (int i = bd1.getLastId(); i >= 0; i--)
            totalBS1.set(pos++, bd1.getD(i));//копирование дробной части в начало полного двоичного преставления
        for (int i = 0; i <= bd1.getLeadId(); i++)
            totalBS1.set(pos++, bd1.getI(i)); //копирование целой части в начало полного двоичного преставления
        bigNumber totalBD1 = new bigNumber(totalBS1);

        //копирование второго числа в полное двоичное преставление
        pos = auxInt - bd2.getLastId();
        for (int i = bd2.getLastId(); i >= 0; i--)
            totalBS2.set(pos++, bd2.getD(i));//копирование дробной части в начало полного двоичного преставления
        for (int i = 0; i <= bd2.getLeadId(); i++)
            totalBS2.set(pos++, bd2.getI(i)); //копирование целой части в начало полного двоичного преставления
        bigNumber totalBD2 = new bigNumber(totalBS2);

        //подборка правильного делителя в виде делитель*pow(2, j)
        j = 0;
        while (totalBD1.isAbsBigger(totalBD2)) {
            totalBD2.intMul2();
            j++;
        }

        while (j >= 0) {
            if (totalBD1.isAbsBigger(totalBD2)) {
                totalBD1 = sub(totalBD1, totalBD2);
                resultBD.intContainer.set(j--, true);
            } else {
                resultBD.intContainer.set(j--, false);
            }
            totalBD2.intDiv2();
        }
        //вычисление дробной части с заданной точностью
        j = 0;
        while (j <= precision) {
            if (totalBD1.isAbsBigger(totalBD2)) {
                //System.out.println(totalBD1.toDouble() > totalBD2.toDouble());
                totalBD1 = sub(totalBD1, totalBD2);
                resultBD.dotContainer.set(j++, true);
            } else {
                //System.out.println(totalBD1.toDouble() < totalBD2.toDouble());
                resultBD.dotContainer.set(j++, false);
            }
            totalBD1.intMul2();
        }

        resultBD.isNegative = bd1.isNegative ^ bd2.isNegative;
        resultBD.clearFree();
        return resultBD;
    }

    public static void main(String[] args) {
        bigNumber bd1 = new bigNumber("218.23456");
        bigNumber bd2 = new bigNumber("4.0");
        System.out.println(bd1.toDouble());
        System.out.println(bd2.toDouble());
        /*System.out.println(bd1.toDouble());
        System.out.println(bd2.toDouble());*/
        //bigNumber bd = div(bd1,bd2, 128);

        //System.out.println(bd.toDouble());
        //System.out.println(-743.8739071679518/-296.923126711913);
        bigNumber bd = new bigNumber(1234543.0, 128);
        int t = 5120;
        for (int i = 0; i < t; i++) bd.intMul2();
        System.out.println(bd.toDouble());
        for (int i = 0; i < t; i++) bd.intDiv2();
        System.out.println(bd.toDouble());
    }
}

