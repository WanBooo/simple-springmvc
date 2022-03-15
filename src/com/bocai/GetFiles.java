package com.bocai;

import java.io.File;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

/***
 *扫描文件
 */
public class GetFiles {
    //文件
    public static List<String> classFiles = new ArrayList<>();
    //类
    public static List<String> className = new ArrayList<>();
    //beans
    public static Map<String, Object> beans = new HashMap<>();

    //获取目录下的文件
    public void getFile(String filePath) {
        File file = new File(filePath);
        File[] files = file.listFiles();
        if (files==null) {
            return;
        }
        for (File readFile : files) {
            if (readFile.isDirectory()) {
                getFile(readFile.toString());
            } else {
                classFiles.add(readFile.toString());
            }
        }
    }

    //将文件保存为类名
    public void getClassName() {
        for (String classFile : classFiles) {
            String[] split = classFile.split(".\\\\src\\\\");
            split[1] = split[1].replace("\\", ".").replace(".java", "");
            className.add(split[1]);
        }
    }

    //获取类实例化存储到bean
    public void getClassMethod() {
        for (String c : className) {
            String[] split = c.split(".");
            String name = split[split.length - 1];
            try {
                Class<?> forName = Class.forName(c);
                Object o = forName.newInstance();
                //判断类上是否有注解
                Annotation[] annotations = forName.getAnnotations();
                for (Annotation annotation : annotations) {
                    if (annotation instanceof BoConAnno) {
                        beans.put(name, o);
                    }
                }
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            }

        }
    }


    public static void main(String[] args) {
        GetFiles getFiles = new GetFiles();
        getFiles.getFile("./src");
        System.out.println();
        getFiles.getClassName();
    }

    public static void main2(String[] args) throws IOException {
        List<Path> collect = Files.walk(Paths.get("./src"), 1).collect(Collectors.toList());
        System.out.println(collect);
    }
}
