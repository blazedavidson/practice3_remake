package ru.practice3;

import java.lang.reflect.Proxy;


public class Main {
    public static void main(String[] args) throws InterruptedException {
/*        Fraction fr = new Fraction( 2, 5);
        CachingProxy proxyCache = new CachingProxy();
        Fractionable num = (Fractionable) proxyCache.create( fr);

        try {
            if (num != null) {
                System.out.println( new SimpleDateFormat("dd.MM.yyyy HH:mm:ss:SSS").format( new Date()));
                double i1 = num.doubleValue();
                double i2 = num.doubleValue();
                Thread.sleep(2000);
                double i3 = num.doubleValue();
            }
        }
        catch (Exception ex) { }
    }
*/
        Fraction fr = new Fraction(2, 10);
        Fractionable num = (Fractionable) Utils.cache(fr);
        CacheHandlerProxy handler = (CacheHandlerProxy) Proxy.getInvocationHandler(num);


        Runnable task = new Runnable() {
            public void run() {

                int minValue = 100;
                int maxValue = 500;
                int randomValue = minValue + (int) (Math.random() * (maxValue - minValue + 1));
                try {
                    if (num != null) {
                        Thread.sleep(100);
                        double i1 = num.doubleValue();
                        System.out.println("---->1 " + Thread.currentThread());
                        Thread.sleep(1000);
                        num.setDenum(1);
                        double i2 = num.doubleValue();
                        System.out.println("---->2 " + Thread.currentThread());

                        num.setNum(8);
                        double i4 = num.doubleValue();
                        System.out.println("---->3 " + Thread.currentThread());
                        Thread.sleep(1600 + randomValue);

                        double i3 = num.doubleValue();
                        System.out.println("---->4 " + Thread.currentThread());
                        Thread.sleep(1000);
                    }
                }
                catch (Exception ex) {
                }
                finally {
                    handler.stopClear();
                }
            }
        };

        Thread thread1 = new Thread(task);
        Thread thread2 = new Thread(task);
        Thread thread3 = new Thread(task);
        thread1.start();
        thread2.start();
        thread3.start();
    }
}