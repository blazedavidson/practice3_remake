package ru.practice3;

import java.lang.reflect.Proxy;
import java.text.SimpleDateFormat;
import java.util.Date;


public class Main {
    public static void main(String[] args) throws InterruptedException {

        Fraction fr = new Fraction( 2, 5);
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
}