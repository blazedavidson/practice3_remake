package ru.practice3;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class CacheHandlerProxy implements InvocationHandler{

    public class RecycleBin extends Thread {
        private volatile boolean clear, active = true;

        public RecycleBin(String name) {super(name); clear = true;}

        @Override
        public void run() {
            while (active) {
                try {
                    if (clear) {
                        HashSet<FractionRes> tempHash;
                        long minTime =  new Date().getTime() - 1000;
                        Iterator<Map.Entry<String, HashSet<FractionRes>>> i = cacheMap.entrySet().iterator();
                        while (i.hasNext()) { //чистим
                            tempHash = i.next().getValue();
                            for (Iterator<FractionRes> it = tempHash.iterator(); it.hasNext(); ) {
                                if (it.next().timeCache < minTime) {
                                    System.out.println("->очистка метода из кэша");
                                    it.remove();
                                }
                            }
                            if (tempHash.isEmpty()) {
                                i.remove();
                            }
                        }
                    }
                    sleep( 0);
                }
                catch (InterruptedException ex) { break; }
                catch (Exception ex) { }
            }
        }

        public void endClearing() {
            active = false;
        }
    }


    private static class FractionRes {
        public final Object objCache;
        public long timeCache;

        public Object res;



        public FractionRes(Object objCache, long timeCache, Object res) {
            this.objCache = objCache;
            this.timeCache = timeCache;
            this.res = res;
        }
    }

    private final Object obj;
    private final ConcurrentHashMap<String, HashSet<FractionRes>> cacheMap = new ConcurrentHashMap<>();
    private boolean fromCache;
    RecycleBin recycleBin;
    public void stopClear() {
        if( recycleBin != null) recycleBin.endClearing();
    }
    public CacheHandlerProxy(Object obj) {
        this.obj = obj;
    }

    public void clearFullCache(String className) {

        if (!cacheMap.isEmpty()) {
            Iterator<Map.Entry<String, HashSet<FractionRes>>> iterator = cacheMap.entrySet().iterator();
            while (iterator.hasNext()) {
                if (iterator.next().getKey().indexOf(className + "/") == 0) {
                    iterator.remove();
                    System.out.println("->очистка кэша");
                }
            }
        }
    }
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        //принимает обьект, принимает метод для вызова, принимает аргумент
        //метод - нужно название и список параметров
        Method currentMethod;
        String className = obj.getClass().getName();

        HashSet<FractionRes> tempHashFr;
        String methodString;
        Object tempObject;
        long deltaTimeCache;

        //очистка
        if (recycleBin == null) {

            recycleBin = new RecycleBin( "RecycleBin");
            recycleBin.setPriority( Thread.MIN_PRIORITY);

            recycleBin.start();
        }

        try{
            currentMethod = obj.getClass().getMethod(method.getName(), method.getParameterTypes());
            //getDecaredMethod описывает те методы которые есть в данном классе (все)
            //getMethod - возвращает методы public
        } catch (Exception e) {
            return method.invoke(obj, args);
        }

        if (currentMethod.isAnnotationPresent(Mutator.class)){ //если аннотация Mutator - удаляем из кэша все элементы
            clearFullCache(className);
            //objectsHash.clear();
        }


        //после того как нашли метод то проверяем есть на нем аннотация Cache
        if (currentMethod.isAnnotationPresent(Cache.class)) { //если аннотация Cache

            fromCache = false;

            deltaTimeCache = currentMethod.getAnnotation(Cache.class).value();
            long currentTimeCache = System.currentTimeMillis();
            long endingTimeCache = currentTimeCache + deltaTimeCache;

            methodString = obj.getClass().getName() + "/" + method.getName() + ":";
            for (Parameter param : currentMethod.getParameters()) {
                methodString = methodString + (String) param.getName() + "-" + (String) param.getType().toString() + ","; }

            if (cacheMap.containsKey(methodString)) {
                tempHashFr = cacheMap.get( methodString);
                //перебираем HashSet
                for (Iterator<FractionRes> i = tempHashFr.iterator(); i.hasNext(); ) {
                    FractionRes tempFr = i.next();
                    if(tempFr.objCache.equals(obj)) {
                                                     if(tempFr.timeCache >= currentTimeCache) {fromCache = true;
                                                                                               System.out.println("->использован кэш");
                                                                                               tempFr.timeCache = endingTimeCache;
                                                                                               return tempFr.res;
                                                                                                 }
                                                     else {
                                                            tempObject = method.invoke(obj, args);
                                                            tempFr.timeCache = endingTimeCache;
                                                            tempFr.res = tempObject;
                                                            return tempObject;
                                                           }

                                                     }
                }
                //------ если нет то добавляем
                tempObject = method.invoke(obj, args);
                tempHashFr.add( new FractionRes( ((Fraction) obj).getStamp(), endingTimeCache, tempObject ));
                return tempObject;

            }

            else {
                //если отсутствует добавляем
                tempObject = method.invoke(obj, args);
                tempHashFr = new HashSet<>();
                tempHashFr.add( new FractionRes( ((Fraction) obj).getStamp(), endingTimeCache, tempObject ));
                cacheMap.put( methodString, tempHashFr);
                return tempObject;

            }
        }

        return method.invoke( obj, args);
    }
}