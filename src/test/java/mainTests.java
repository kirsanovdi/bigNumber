import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Random;

public class mainTests {

    @Test
    public void sumTest() {
        Random rnd = new Random();
        for (int i = 0; i < 100000; i++) {
            double a1 = rnd.nextDouble() * 1000000000000000.0;
            double a2 = rnd.nextDouble() * 1000000000000000.0;
            double sum = BigNumber.sum(new BigNumber(a1, 128), new BigNumber(a2, 128)).toDouble();
            Assertions.assertEquals(a1 + a2, sum, 1e-9);
        }
    }

    @Test
    public void subTest() {
        Random rnd = new Random();
        for (int i = 0; i < 100000; i++) {
            double a1 = rnd.nextDouble() * 1000000000000000.0;
            double a2 = rnd.nextDouble() * 1000000000000000.0;
            double sub = BigNumber.sub(new BigNumber(a1, 128), new BigNumber(a2, 128)).toDouble();
            Assertions.assertEquals(a1 - a2, sub, 1e-9);
        }
    }

    @Test
    public void mulTest() {
        Random rnd = new Random();
        for (int i = 0; i < 10000; i++) {
            double a1 = rnd.nextDouble() * 1000000000000000.0;
            double a2 = rnd.nextDouble() * 1000000000000000.0;
            double mul = BigNumber.mul(new BigNumber(a1, 128), new BigNumber(a2, 128)).toDouble();
            if (Math.abs(a1 * a2 / mul - 1) > 1e-8) System.out.println(a1 + " " + a2 + " " + mul);
            Assertions.assertEquals(Math.abs(a1 * a2 / mul), 1, 1e-9);
        }
    }

    @Test
    public void divTest() {
        Random rnd = new Random();
        for (int i = 0; i < 10000; i++) {
            double a1 = rnd.nextDouble() * 1000000000000000.0;
            double a2 = rnd.nextDouble() * 1000000000000000.0;
            if (Math.abs(a2) < 1e-5) a2 = a2 + 2.0;
            double div = BigNumber.div(new BigNumber(a1, 128), new BigNumber(a2, 128), 128).toDouble();
            //Assertions.assertEquals(a1 / a2, div, 1e-9);
            if (Math.abs(a1 / a2 / div - 1) > 1e-8) System.out.println(a1 + " " + a2 + " " + div);
            Assertions.assertEquals(a1 / a2 / div, 1, 1e-9);
        }
    }

    @Test
    public void div2Test() {
        Assertions.assertEquals(
                218.23456 / 4.0,
                BigNumber.div(
                        new BigNumber(218.23456, 128),
                        new BigNumber(4.0, 128),
                        128).toDouble(),
                1e-9);
        Assertions.assertEquals(
                298.0823111620618 / 321.6695093932683,
                BigNumber.div(
                        new BigNumber(298.0823111620618, 128),
                        new BigNumber(321.6695093932683, 128),
                        128).toDouble(),
                1e-9);
        Assertions.assertEquals(
                51462.0 / 14440.234,
                BigNumber.div(
                        new BigNumber(51462.0, 128),
                        new BigNumber(14440.234, 128),
                        128).toDouble(),
                1e-9);
        Assertions.assertEquals(
                727.9110521642292 / 624.7727417330805,
                BigNumber.div(
                        new BigNumber(727.9110521642292, 128),
                        new BigNumber(624.7727417330805, 128),
                        128).toDouble(),
                1e-9);
    }

    @Test
    public void strTest() {
        Random rnd = new Random();
        for (int i = 0; i < 10; i++) {
            double a1 = (rnd.nextDouble() * 100000000.0 - rnd.nextDouble() * 100000000.0) / 100000.0;
            double a2 = (rnd.nextDouble() * 100000000.0 - rnd.nextDouble() * 100000000.0) / 100000.0;
            double sum = BigNumber.sum(
                    new BigNumber(String.valueOf(a1), 256),
                    new BigNumber(String.valueOf(a2), 256)
            ).toDouble();
            if (Math.abs(a1 + a2 - sum) > 1e-8) System.out.println(a1 + " " + a2 + " " + sum);
            Assertions.assertEquals(a1 + a2, sum, 1e-9);
        }
    }

    @Test
    public void str2Test() {
        BigNumber bd1 = new BigNumber("218.23456", 256);
        BigNumber bd2 = new BigNumber("4.0", 256);
        BigNumber bd = BigNumber.div(bd1, bd2, 128);
        Assertions.assertEquals(bd.toDouble(), 218.23456 / 4.0, 1e-9);
    }
}
