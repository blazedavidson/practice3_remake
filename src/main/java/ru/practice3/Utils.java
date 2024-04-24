package ru.practice3;

import java.lang.reflect.Proxy;

public class Utils {
    public static Object cache(Object obj) {
        Object proxyObj = Proxy.newProxyInstance(
                obj.getClass().getClassLoader(),
                obj.getClass().getInterfaces(),
                new CacheHandlerProxy(obj));
        return proxyObj;
    }
}