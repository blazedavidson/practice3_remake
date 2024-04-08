package ru.practice2;

import java.lang.reflect.Proxy;


public class Main {
    public static void main(String[] args) {
        Fraction fr = new Fraction(2, 10);
        Fractionable num = (Fractionable) Utils.cache(fr);
        CachingHandler handler = (CachingHandler) Proxy.getInvocationHandler(num);

        num.doubleValue();// sout сработал
        num.doubleValue();// sout молчит
        num.doubleValue();// sout молчит
        num.setNum(5);
        num.doubleValue();// sout сработал
        num.doubleValue();// sout молчит
        num.setDenum(12);
        Utils.cache(fr);

    }
}