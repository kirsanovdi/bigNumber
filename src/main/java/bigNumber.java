import java.util.BitSet;
import java.util.Random;

public class bigNumber {
    private BitSet intBitSet;
    private BitSet dotBitSet;
    private boolean isNegative;

    bigNumber(bigNumber otherBD){
        intBitSet = bitsetCopy(otherBD.intBitSet);
        dotBitSet = bitsetCopy(otherBD.dotBitSet);
        isNegative = otherBD.isNegative;
    }
    bigNumber(int intBitCount, int dotBitCount){
        intBitSet = new BitSet(intBitCount);
        dotBitSet = new BitSet(dotBitCount);
        //isNegative = false;
    }
    bigNumber(int number){
        intBitSet = new BitSet(32);
        dotBitSet = new BitSet(1);
        isNegative = number < 0;
        number = Math.abs(number);
        int i = 0;
        while(number > 0){
            intBitSet.set(i, number%2 == 1);
            number/=2;
            i++;
        }
    }
    bigNumber(double number, int precision){
        intBitSet = new BitSet(32);
        dotBitSet = new BitSet(precision);
        isNegative = number < 0;
        number = Math.abs(number);
        int i = 0;
        int intNumber = (int)number;
        double dotNumber = number - intNumber;
        while(intNumber > 0){
            intBitSet.set(i, intNumber%2 == 1);
            intNumber/=2;
            i++;
        }
        for(i = 0; i < precision; i++){
            dotNumber *= 2;
            dotBitSet.set(i, dotNumber >= 1);
            dotNumber = dotNumber - (int) dotNumber;
        }
    }

    public static bigNumber bigNumberFromString(String str){
        String[] s = str.replace("-", "").split("\\.");
        bigNumber result = new bigNumber(1, 1);
        bigNumber auxBN = new bigNumber(0);
        bigNumber bn10 = new bigNumber(10);
        bigNumber bn01 = new bigNumber(0.1, 128);
        for(int i = 0; i < s[0].length(); i++){
            result = mul(result, bn10);
            result = sum(result, new bigNumber(s[0].charAt(i) - '0'));
        }
        for(int i = s[1].length() - 1; i >= 0; i--){
            auxBN = sum(auxBN, new bigNumber(s[1].charAt(i) - '0'));
            auxBN = mul(auxBN, bn01);
        }
        result = sum(result, auxBN);
        result.isNegative = str.contains("-");
        return result;
    }

    /**только для сложения**/
    private bigNumber(BitSet bs){
        intBitSet = bitsetCopy(bs);
        dotBitSet = new BitSet(1);
    }

    private void clearFree(){
        BitSet iBS = new BitSet(getLeadId());
        for(int i = 0; i < iBS.size(); i++) iBS.set(i, intBitSet.get(i));
        intBitSet = iBS;

        BitSet dBS = new BitSet(getLastId());
        for(int i = 0; i < dBS.size(); i++) dBS.set(i, dotBitSet.get(i));
        dotBitSet = dBS;
    }
    private BitSet bitsetCopy(BitSet bitSet){
        BitSet result = new BitSet(bitSet.size());
        for(int i = 0; i < bitSet.size(); i++) result.set(i, bitSet.get(i));
        return result;
    }

    private static int getBiggestId(BitSet bitSet){
        for(int i = bitSet.size() - 1; i >= 0; i--){
            if(bitSet.get(i)) return i;
        }
        return 0;
    }
    private static int getLowestId(BitSet bitSet) {
        for (int i = 0; i < bitSet.size(); i++) {
            if (bitSet.get(i)) return i;
        }
        return bitSet.size() - 1;
    }
    private int getLeadId(){
        return getBiggestId(intBitSet);
    }
    private int getLastId(){
        return getBiggestId(dotBitSet);
    }
    private boolean getI(int i){
        return intBitSet.get(i);
    }
    private boolean getD(int i){
        return dotBitSet.get(i);
    }
    private int getAsInt(int id){
        return intBitSet.get(id)?1:0;
    }
    private int getAsDot(int id){
        return dotBitSet.get(id)?1:0;
    }
    private void copyFrom(bigNumber bd){
        for(int i = 0; i<= bd.getLeadId(); i++){
            intBitSet.set(i, bd.intBitSet.get(i));
        }
        for(int i = bd.getLastId(); i >= 0; i--){
            dotBitSet.set(i, bd.dotBitSet.get(i));
        }
    }
    private boolean isAbsBigger(bigNumber bd){
        if(getLeadId() > bd.getLeadId()) return true;
        if(getLeadId() < bd.getLeadId()) return false;
        for(int i = getLeadId(); i >= 0; i--){
            if(intBitSet.get(i)&&!bd.intBitSet.get(i)) return true;
            if(!intBitSet.get(i)&&bd.intBitSet.get(i)) return false;
        }
        for(int i = 0; i <= Math.min(getLastId(), bd.getLastId()); i++){
            if(dotBitSet.get(i)&&!bd.dotBitSet.get(i)) return true;
            if(!dotBitSet.get(i)&&bd.dotBitSet.get(i)) return false;
        }
        return getLastId() >= bd.getLastId();
    }

    public int toInt(){
        int biggestId = getBiggestId(intBitSet);
        if(biggestId > 31) throw new IllegalStateException();
        int result = 0;
        int mod = 1;
        for(int i = 0; i <= biggestId; i++){
            result += getAsInt(i)*mod;
            mod*=2;
        }
        return result * (isNegative?-1:1);
    }
    public double toDouble(){
        int biggestId = getBiggestId(intBitSet);
        int lowestId = getBiggestId(dotBitSet);
        if(biggestId > 31) throw new IllegalStateException();
        int intResult = 0;
        double dotResult = 0;
        int mod = 1;
        for(int i = 0; i <= biggestId; i++){
            intResult += getAsInt(i)*mod;
            mod*=2;
        }
        double modD = 0.5;
        for (int i = 0; i <= lowestId; i++){
            dotResult += getAsDot(i) * modD;
            modD/=2.0;
        }
        double result = (double) intResult + dotResult;
        return result * (isNegative?-1:1);
    }

    @Override
    public String toString() {
        String s = ".";
        for(int i = 0; i <= getLeadId(); i++) s = (getI(i)?"1":"0") + s;
        for(int i = 0; i <= getLastId(); i++) s = s + (getD(i)?"1":"0");
        return s;
    }

    private boolean isIntAbsBigger(bigNumber bd){
        if(getLeadId() > bd.getLeadId()) return true;
        if(getLeadId() < bd.getLeadId()) return false;
        for(int i = getLeadId(); i >= 0; i--){
            if(intBitSet.get(i)&&!bd.intBitSet.get(i)) return true;
            if(!intBitSet.get(i)&&bd.intBitSet.get(i)) return false;
        }
        return getLastId() >= bd.getLastId();
    }
    private static bigNumber intSum(bigNumber bd1, bigNumber bd2){
        bigNumber resultBD = new bigNumber(
                Math.max(bd1.getLeadId(), bd2.getLeadId())
                        + (bd1.getLeadId() == bd2.getLeadId() && (bd1.isNegative == bd2.isNegative)?1:0),
                1
        );
        if (bd2.isIntAbsBigger(bd1)){
            bigNumber temp = bd1;
            bd1 = bd2;
            bd2 = temp;
        }
        resultBD.copyFrom(bd1);
        boolean minded = false;

        if(bd1.isNegative == bd2.isNegative){
            int i;
            for(i = 0; i <= bd2.getLeadId(); i++){
                boolean bd2i = bd2.getI(i);
                boolean resBDi = resultBD.getI(i);
                boolean toAdd = (bd2i == minded) == resBDi;
                minded = resBDi && bd2i || resBDi && minded || bd2i && minded;
                resultBD.intBitSet.set(i, toAdd);
            }
            while (minded && resultBD.getI(i)){
                resultBD.intBitSet.set(i, false);
                i++;
            }
            if (minded) resultBD.intBitSet.set(i, true);
        }else{
            int i;
            for(i = 0; i <= bd2.getLeadId(); i++){
                boolean bd2i = bd2.getI(i);
                boolean resBDi = resultBD.getI(i);
                boolean toAdd = bd2i == (minded == resBDi);
                minded = bd2i && minded && resBDi || !resBDi && (minded || bd2i);
                resultBD.intBitSet.set(i, toAdd);
            }
            while (minded && !resultBD.getI(i)){
                resultBD.intBitSet.set(i, true);
                i++;
            }
            if (minded) resultBD.intBitSet.set(i, false);
        }
        resultBD.isNegative = bd1.isNegative;
        return resultBD;
    }
    private static bigNumber intMul2(bigNumber bd){
        return intSum(bd, bd);
    }
    private static bigNumber intDiv2(bigNumber bd){
        return new bigNumber(bd.intBitSet.get(1, bd.intBitSet.size()));
    }

    public static bigNumber sum(bigNumber bd1, bigNumber bd2){
        bigNumber resultBD = new bigNumber(
                Math.max(bd1.getLeadId(), bd2.getLeadId())
                        + (bd1.getLeadId() == bd2.getLeadId() && (bd1.isNegative == bd2.isNegative)?1:0),
                Math.max(bd1.getLastId(), bd2.getLastId())
        );
        if (bd2.isAbsBigger(bd1)){
            bigNumber temp = bd1;
            bd1 = bd2;
            bd2 = temp;
        }
        resultBD.copyFrom(bd1);
        boolean minded = false;

        if(bd1.isNegative == bd2.isNegative){
            int i;
            for(i = bd2.getLastId(); i >= 0; i--){
                boolean bd2d = bd2.getD(i);
                boolean resBDd = resultBD.getD(i);
                boolean toAdd = (bd2d == minded) == resBDd;
                minded = resBDd && bd2d || resBDd && minded || bd2d && minded;
                resultBD.dotBitSet.set(i, toAdd);
            }
            for(i = 0; i <= bd2.getLeadId(); i++){
                boolean bd2i = bd2.getI(i);
                boolean resBDi = resultBD.getI(i);
                boolean toAdd = (bd2i == minded) == resBDi;
                minded = resBDi && bd2i || resBDi && minded || bd2i && minded;
                resultBD.intBitSet.set(i, toAdd);
            }
            while (minded && resultBD.getI(i)){
                resultBD.intBitSet.set(i, false);
                i++;
            }
            if (minded) resultBD.intBitSet.set(i, true);
        }else{
            int i;
            for(i = bd2.getLastId(); i >= 0; i--){
                boolean bd2d = bd2.getD(i);
                boolean resBDd = resultBD.getD(i);
                boolean toAdd = bd2d == (minded == resBDd);
                minded = bd2d && minded && resBDd || !resBDd && (minded || bd2d);
                resultBD.dotBitSet.set(i, toAdd);
            }

            for(i = 0; i <= bd2.getLeadId(); i++){
                boolean bd2i = bd2.getI(i);
                boolean resBDi = resultBD.getI(i);
                boolean toAdd = bd2i == (minded == resBDi);
                minded = bd2i && minded && resBDi || !resBDi && (minded || bd2i);
                resultBD.intBitSet.set(i, toAdd);
            }
            while (minded && !resultBD.getI(i)){
                resultBD.intBitSet.set(i, true);
                i++;
            }
            if (minded) resultBD.intBitSet.set(i, false);
        }
        resultBD.isNegative = bd1.isNegative;
        return resultBD;
    }
    public static bigNumber sub(bigNumber bd1, bigNumber bd2){
        bigNumber temp = new bigNumber(bd2);
        temp.isNegative = !bd2.isNegative;
        return sum(bd1, temp);
    }
    public static bigNumber mul(bigNumber bd1, bigNumber bd2){

        int posInt = bd1.getLeadId() + bd2.getLeadId() + 1;
        int posDot = bd1.getLastId() + bd2.getLastId() + 1;

        bigNumber resultBD = new bigNumber(posInt, posDot);
        bigNumber auxBD = new bigNumber(1, 1);

        BitSet totalBS1 = new BitSet(bd1.getLeadId() + bd1.getLastId() + 2);
        BitSet totalBS2 = new BitSet(bd2.getLeadId() + bd2.getLastId() + 2);

        int pos = 0;
        for(int i = bd1.getLastId(); i >= 0; i--) totalBS1.set(pos++, bd1.getD(i));
        for(int i = 0; i <= bd1.getLeadId(); i++) totalBS1.set(pos++, bd1.getI(i));

        pos = 0;
        for(int i = bd2.getLastId(); i >= 0; i--) totalBS2.set(pos++, bd2.getD(i));
        for(int i = 0; i <= bd2.getLeadId(); i++) totalBS2.set(pos++, bd2.getI(i));

        bigNumber totalBD2 = new bigNumber(totalBS2);

        for(int i = 0; i < totalBS1.size(); i++){
            if(totalBS1.get(i)){
                auxBD = intSum(auxBD , totalBD2);
            }
            totalBD2 = intMul2(totalBD2);
        }
        int j = 0;

        for(int i = posDot; i >= 0; i--) resultBD.dotBitSet.set(i, auxBD.getI(j++));
        for(int i = 0; i <= posInt; i++) resultBD.intBitSet.set(i, auxBD.getI(j++));
        resultBD.isNegative = bd1.isNegative ^ bd2.isNegative;
        resultBD.clearFree();
        return resultBD;
    }
    public static bigNumber div(bigNumber bd1, bigNumber bd2, int precision){

        bigNumber resultBD = new bigNumber(1);//

        int auxInt = Math.max(bd1.getLastId(), bd2.getLastId());
        BitSet totalBS1 = new BitSet(auxInt + bd1.getLeadId() + 1);
        BitSet totalBS2 = new BitSet(auxInt + bd1.getLeadId() + 1);

        int pos;

        totalBS1.set(0, auxInt - bd1.getLastId(), false);
        pos = auxInt - bd1.getLastId();
        for(int i = bd1.getLastId(); i >= 0; i--) totalBS1.set(pos++, bd1.getD(i));
        for(int i = 0; i <= bd1.getLeadId(); i++) totalBS1.set(pos++, bd1.getI(i));

        pos = auxInt - bd2.getLastId();
        totalBS2.set(0, auxInt - bd2.getLastId(), false);
        for(int i = bd2.getLastId(); i >= 0; i--) totalBS2.set(pos++, bd2.getD(i));
        for(int i = 0; i <= bd2.getLeadId(); i++) totalBS2.set(pos++, bd2.getI(i));

        bigNumber totalBD1 = new bigNumber(totalBS1);
        bigNumber totalBD2 = new bigNumber(totalBS2);

        int j = 0;
        while (totalBD1.isIntAbsBigger(totalBD2)) {
            totalBD2 = intMul2(totalBD2);
            j++;
        }

        while(j >= 0){
            if(totalBD1.isIntAbsBigger(totalBD2)){
                totalBD1 = sub(totalBD1, totalBD2);
                resultBD.intBitSet.set(j--, true);
            }else{
                resultBD.intBitSet.set(j--, false);
            }
            totalBD2 = intDiv2(totalBD2);
        }

        j = 0;
        while(j <= precision){
            if(totalBD1.isIntAbsBigger(totalBD2)){
                totalBD1 = sub(totalBD1, totalBD2);
                resultBD.dotBitSet.set(j++, true);
            }else{
                resultBD.dotBitSet.set(j++, false);
            }
            totalBD1 = intMul2(totalBD1);
        }

        resultBD.isNegative = bd1.isNegative ^ bd2.isNegative;
        return resultBD;
    }

    public static void main(String[] args){
        bigNumber bd1 = bigNumberFromString("13265.234");
        bigNumber bd2 = new bigNumber(10.23525435, 32);
        bigNumber bdDiv = mul(bd1,bd2);
        System.out.println(bd1.toDouble());
        System.out.println(bd1);
        System.out.println("-----------");
        System.out.println(bd2.toDouble());
        System.out.println(bd2);
        System.out.println("-----------");
        System.out.println(bdDiv.toDouble());
        System.out.println(bdDiv);
        System.out.println("-----------");
        //System.out.println(bd1.toDouble()/bd2.toDouble());
        //System.out.println(bdDiv.toDouble());
        //System.out.println(Math.abs(bd1.toDouble()/bd2.toDouble() - bdDiv.toDouble()) < 1e-8);
        System.out.println("end");
    }
}

