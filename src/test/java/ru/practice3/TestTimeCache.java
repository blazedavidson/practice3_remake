package ru.practice3;

import org.junit.jupiter.api.*;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TestTimeCache {
 @Test
 @DisplayName("Проверка работы кэша (мс>2000мс) ")
 public void TestCache(){
     Fraction fr = new Fraction( 2, 5);
     CachingProxy proxyCache = new CachingProxy();
     Fractionable num = (Fractionable) proxyCache.create( fr);

     try {
         if (num != null) {
             System.out.println("Проверяем использование кэша <1000мс");
             System.out.println( new SimpleDateFormat("dd.MM.yyyy HH:mm:ss:SSS").format( new Date()));
             double i1 = num.doubleValue();
             double i2 = num.doubleValue();
             System.out.println( new SimpleDateFormat("dd.MM.yyyy HH:mm:ss:SSS").format( new Date()));
             Assertions.assertEquals(i1,i2);

             System.out.println("------");
             System.out.println("Проверяем использование кэша >2000мс");
             Thread.sleep(2000);
             System.out.println( new SimpleDateFormat("dd.MM.yyyy HH:mm:ss:SSS").format( new Date()));
             double i3 = num.doubleValue();
             Assertions.assertEquals(i2,i3);
         }
     }
     catch (Exception ex) {
         System.out.println(ex);
     }
 }
 @Test
 @DisplayName("Проверка работы кэша при изменении данных")
 public void TestCache2(){
     Fraction fr = new Fraction( 2, 10);
     CachingProxy proxyCache = new CachingProxy();
     Fractionable num = (Fractionable) proxyCache.create( fr);

     try {
         if (num != null) {
             System.out.println("------");
             System.out.println("Проверяем очистку кэша при замене данных");
             System.out.println( new SimpleDateFormat("dd.MM.yyyy HH:mm:ss:SSS").format( new Date()));
             double i5 = num.doubleValue();
             num.setNum(3);
             num.setDenum(4);
             System.out.println( new SimpleDateFormat("dd.MM.yyyy HH:mm:ss:SSS").format( new Date()));
             double i4 = num.doubleValue();
             Assertions.assertNotEquals(i5,i4);

         }
     }
     catch (Exception ex) {
         System.out.println(ex);
     }
 }
}
