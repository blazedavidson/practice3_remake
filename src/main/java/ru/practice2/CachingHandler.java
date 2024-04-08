package ru.practice2;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.HashMap;

public class CachingHandler implements InvocationHandler, Cacheable{
    private final Object obj;
    private final HashMap<String, Object> objectsHash = new HashMap<>();
    private boolean fromCache;

    public boolean checkCache() {
        return fromCache;
    }
    public CachingHandler(Object obj) {
        this.obj = obj;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        //принимает обьект, принимает метод для вызова, принимает аргумент
        //метод - нужно название и список параметров
        Method currentMethod;
        String methodString;
        Object tempObject;

        try{
            currentMethod = obj.getClass().getMethod(method.getName(), method.getParameterTypes());
            //getDecaredMethod описывает те методы которые есть в данном классе (все)
            //getMethod - возвращает методы public
        } catch (Exception e) {
            return method.invoke(obj, args);
        }

        if (currentMethod.isAnnotationPresent(Mutator.class)){ //если аннотация Mutator - удаляем из кэша все элементы
            objectsHash.clear();
        }


        //после того как нашли метод то проверяем есть на нем аннотация
        if (currentMethod.isAnnotationPresent(Cache.class)) { //если аннотация Cache

            fromCache = false;
            Field getCache = obj.getClass().getDeclaredField("fromCache");
            if (getCache != null) {getCache.setAccessible(true); getCache.set(obj, (Boolean) false); }


            methodString = obj.getClass().getName() + "/" + method.getName() + ":";
            for (Parameter param : currentMethod.getParameters()) { methodString = methodString + param.getName() + "-" + param.getType() + ","; }

            tempObject = objectsHash.get(methodString);

            if (tempObject != null) {
                if (getCache != null) { fromCache = true; getCache.set(obj, true); }
                return tempObject;
            }
            else {
                tempObject = method.invoke(obj, args);
                objectsHash.put(methodString, tempObject);
                return tempObject;
            }
             /*
             вариант семинара, HashMap<Method, Object> отказался работать=(
            if (objectsHash.containsKey(currentMethod)){
                return objectsHash.get(currentMethod);
            }
            */
            //tmp = method.invoke(obj, args);
            //objectsHash.put(cacheKey, tmp);
            //return objectsHash;

        }

        return method.invoke( obj, args);
    }
}


/*вариант из семинара
public class FractionCache implements Fractionable {
    private Fraction fraction;

    private double tmp;

    boolean isChanged = true;

    public FractionCache(Fraction fraction){this.fraction = fraction;}

    public double doubleValue(){
        //*****рефлексия
        //сущности используются решения:
        //class
        //contructor
        //method
        //field
        //annotation
        //proxy
        if(isChanged){ tmp = fraction.doubleValue();}
        isChanged=false;
        return tmp;
    }

    @Override
    public void setNum(int num) {
        isChanged=true;
        fraction.setNum(num);
    }
    public void setDenum(int denum){
        isChanged=true;
        fraction.setNum(denum);
    }
}
 */