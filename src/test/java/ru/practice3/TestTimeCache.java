package ru.practice3;

import org.junit.jupiter.api.*;

import java.lang.reflect.Proxy;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TestTimeCache {

    private void newSleep( int t) {
        try {
            Thread.sleep(t);
        } catch (Exception ex) {
        }
    }
 @Test
 @DisplayName("Проверка работы кэша (мс>2000мс) ")
 public void TestCache(){
     Fraction fr = new Fraction( 2, 10);
     Fractionable num = (Fractionable) Utils.cache( fr);


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
     Fractionable num = (Fractionable) Utils.cache( fr);
     CacheHandlerProxy handler = (CacheHandlerProxy) Proxy.getInvocationHandler( num);
     //handler.recycleBin.setRunClear(true);
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

    @Test
    @DisplayName("Проверка работы очистки кэша в несколько потоков")
    public void TestCache3(){
        Fraction fr = new Fraction( 2, 10);
        Fractionable num = (Fractionable) Utils.cache( fr);
        CacheHandlerProxy handler = (CacheHandlerProxy) Proxy.getInvocationHandler( num);

        //handler.clearCacheStop();

        Runnable task = new Runnable() {
            public void run() {
                int minValue = 100;
                int maxValue = 500;
                int randomValue = minValue + (int) (Math.random() * (maxValue - minValue + 1));
                try {
                    if (num != null) {
                        //newSleep(100);
                        double i1 = num.doubleValue();
                        System.out.println ("---->1а"+Thread.currentThread());
                        double i2 = num.doubleValue();
                        System.out.println ("---->1б"+Thread.currentThread());
                        //Assertions.assertEquals(i1,i2);
                        //newSleep(1000);
                        num.setDenum(1);
                        System.out.println ("---->2 заменены данные /мутатор"+Thread.currentThread());
                        double i3 = num.doubleValue();
                        System.out.println ("---->2"+Thread.currentThread());
                        System.out.println ("---->2 заменены данные /мутатор"+Thread.currentThread());
                        num.setNum(8);
                        double i4 = num.doubleValue();
                        System.out.println ("---->3"+Thread.currentThread());
                        //Assertions.assertNotEquals(i3,i4);
                        //newSleep(1995 + randomValue);
                        double i5 = num.doubleValue();
                        System.out.println ("---->4"+Thread.currentThread());
                        //Assertions.assertNotEquals(i4,i5);
                        //newSleep(1000);

                    }
                }
                catch (Exception ex) {}
                //finally {handler.clearCacheStop();}
            }
        };

        Thread thread1 = new Thread(task);
        Thread thread3 = new Thread(task);
        Thread thread2 = new Thread(task);
        Thread thread4 = new Thread(task);
       // newSleep(100);
        thread1.start();
       // newSleep(100);
        thread3.start();
        //newSleep(100);
        thread2.start();
        //newSleep(100);
        thread4.start();
        //handler.clearCacheStop();

    }

}
