import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Random;

public class mainTests {

    @Test
    public void sumTest(){
        Random rnd = new Random();
        for(int i = 0; i < 100000; i++){
            double a1 = (rnd.nextDouble() * 100000000.0 - rnd.nextDouble() * 100000000.0)/100000.0;
            double a2 = (rnd.nextDouble() * 100000000.0 - rnd.nextDouble() * 100000000.0)/100000.0;
            double sum = bigNumber.sum(new bigNumber(a1, 128), new bigNumber(a2, 128)).toDouble();
            Assertions.assertEquals(a1 + a2, sum, 1e-9);
        }
    }

    @Test
    public void subTest(){
        Random rnd = new Random();
        for(int i = 0; i < 100000; i++){
            double a1 = (rnd.nextDouble() * 100000000.0 - rnd.nextDouble() * 100000000.0)/100000.0;
            double a2 = (rnd.nextDouble() * 100000000.0 - rnd.nextDouble() * 100000000.0)/100000.0;
            double sub = bigNumber.sub(new bigNumber(a1, 128), new bigNumber(a2, 128)).toDouble();
            Assertions.assertEquals(a1 - a2, sub, 1e-9);
        }
    }

    @Test
    public void mulTest(){
        Random rnd = new Random();
        for(int i = 0; i < 100000; i++){
            double a1 = (rnd.nextDouble() * 100000000.0 - rnd.nextDouble() * 100000000.0)/100000.0;
            double a2 = (rnd.nextDouble() * 100000000.0 - rnd.nextDouble() * 100000000.0)/100000.0;
            double mul = bigNumber.mul(new bigNumber(a1, 128), new bigNumber(a2, 128)).toDouble();
            if(Math.abs(a1 * a2 - mul) > 1e-8) System.out.println(a1 + " " + a2 + " " + mul);
            Assertions.assertEquals(a1 * a2, mul, 1e-9);
        }
    }

    @Test
    public void divTest(){
        Random rnd = new Random();
        for(int i = 0; i < 100; i++){
            double a1 = (rnd.nextDouble() * 100000000.0 - rnd.nextDouble() * 100000000.0)/100000.0;
            double a2 = (rnd.nextDouble() * 100000000.0 - rnd.nextDouble() * 100000000.0)/100000.0;
            if (Math.abs(a2) < 1e-5) a2 = a2 + 2.0;
            double div = bigNumber.div(new bigNumber(a1, 128), new bigNumber(a2, 128), 128).toDouble();
            //Assertions.assertEquals(a1 / a2, div, 1e-9);
            if(Math.abs(a1/a2 - div) > 1e-8) System.out.println(a1 + " " + a2 + " " + div);
            Assertions.assertEquals(a1 / a2, div, 1e-9);
        }
    }

    @Test
    public void div2Test(){
        Assertions.assertEquals(
                298.0823111620618 / 321.6695093932683,
                bigNumber.div(
                        new bigNumber(298.0823111620618, 128),
                        new bigNumber(321.6695093932683, 128),
                        128).toDouble(),
                1e-9);
        Assertions.assertEquals(
                51462.0 / 14440.234,
                bigNumber.div(
                        new bigNumber(51462.0, 128),
                        new bigNumber(14440.234, 128),
                        128).toDouble(),
                1e-9);
        Assertions.assertEquals(
                727.9110521642292 / 624.7727417330805,
                bigNumber.div(
                        new bigNumber(727.9110521642292, 128),
                        new bigNumber(624.7727417330805, 128),
                        128).toDouble(),
                1e-9);
    }

    @Test
    public void strTest(){
        Random rnd = new Random();
        for(int i = 0; i < 10; i++){
            double a1 = (rnd.nextDouble() * 100000000.0 - rnd.nextDouble() * 100000000.0)/100000.0;
            double a2 = (rnd.nextDouble() * 100000000.0 - rnd.nextDouble() * 100000000.0)/100000.0;
            double sum = bigNumber.sum(
                    new bigNumber(String.valueOf(a1)),
                    new bigNumber(String.valueOf(a2))
            ).toDouble();
            Assertions.assertEquals(a1 + a2, sum, 1e-9);
        }
    }
}
