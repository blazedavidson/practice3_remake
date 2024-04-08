import org.junit.jupiter.api.Assertions;
import ru.practice2.Fraction;
import ru.practice2.Fractionable;
import ru.practice2.Utils;

public class Test {

    @org.junit.jupiter.api.Test

    public void Test() {
        Fraction frac = new Fraction(2, 3);
        Fractionable num = (Fractionable) Utils.cache(frac);
        if (num != null) {

            num.doubleValue();// sout сработал
            Assertions.assertFalse(frac.getCache());
            num.doubleValue();// sout молчит
            Assertions.assertTrue(frac.getCache());
            num.doubleValue();// sout молчит
            num.setNum(5);
            num.doubleValue();// sout сработал
            Assertions.assertFalse(frac.getCache());
            num.doubleValue();// sout молчит
            Assertions.assertTrue(frac.getCache());
            num.setDenum(12);

        }
    }
}
