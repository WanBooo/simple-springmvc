package com.bocai.controller;


import com.bocai.BoConAnno;
import com.bocai.BoSerAnno;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@BoConAnno("/b")
@BoSerAnno
public class BoController {

    @BoConAnno("/1")
    public String a() {
        return "abc";
    }

    @BoConAnno("/3")
    public String aa() {
        return "abc3";
    }

    @BoConAnno("/5")
    public String a1() {
        return "abc5";
    }

    private static String getRes(String path) throws Exception {
        Class<?> aClass = Class.forName("com.bocai.controller.BoController");
        Object instance = aClass.newInstance();
        Annotation[] annotations = aClass.getAnnotations();
        String urlFromCls = Arrays.stream(annotations).filter(a -> a instanceof BoConAnno)
                .findAny()
                .map(ano -> ((BoConAnno) ano).value())
                .orElse("");
        //getMethods
        Method[] methods = aClass.getMethods();
        Map<String, Method> map = new HashMap<>();
        Arrays.stream(methods)
                .filter(m -> Arrays.stream(m.getAnnotations()).anyMatch(a -> a instanceof BoConAnno))
                .forEach(m -> {
                    String allUrl = urlFromCls + m.getAnnotation(BoConAnno.class).value();
                    map.put(allUrl, m);
                });
        Method method = map.get(path);
        if (method == null) {
            return "not found";
        }

        return (String) method.invoke(instance);
    }
    public static void main(String[] args) throws Exception {
        String path = "/b/1";
        String tr = getRes(path);
        System.out.println(tr);
    }
}
