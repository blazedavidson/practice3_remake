package ru.practice2;

import java.lang.reflect.Proxy;

public class Utils {
    public static Object cache( Object obj) {
        Object proxyObj = Proxy.newProxyInstance(
                obj.getClass().getClassLoader(),
                obj.getClass().getInterfaces(),
                new CachingHandler(obj));
        return proxyObj;

        //Enhancer - обьект конструирует proxy классы
        //Enhancer enhancer =new Enhancer();
        //enhancer.setSuperclass(object.getClass());
        //enhancer.setCallback(()->new ObjectInvocationHandlerAdv<>(object));
        //return(T) enhancer.create();
    }
}
