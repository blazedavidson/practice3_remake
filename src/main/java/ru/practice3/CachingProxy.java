package ru.practice3;

import java.lang.reflect.Proxy;

public class CachingProxy {
    private final Timeable timeDelta;

    public CachingProxy(Timeable timeDelta) {
        this.timeDelta = timeDelta;
    }

    public CachingProxy() {
        this.timeDelta = new CurrentTime();
    }
    private static class CurrentTime implements Timeable {
        @Override
        public long deltaTime() {
            return System.currentTimeMillis();
        }
    }
    //перенесено из Utils 2 задания
    public  Object create(Object obj) {
        CachingHandler handler = new CachingHandler(obj, timeDelta);
        Object proxy = Proxy.newProxyInstance(
                obj.getClass().getClassLoader(),
                obj.getClass().getInterfaces(),
                handler);
        return proxy;
    }
}
