package com.zys.test;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;

public class HofixTest {
    public static void main(String[] args) throws IOException, ClassNotFoundException, IllegalAccessException, InstantiationException, InterruptedException, NoSuchMethodException, InvocationTargetException {
        //在没有使用自定义类加载器，无法卸载有Java默认的类加载器 加载的类
        //这样导致修改文件后，即使侦测到发送变化在加载更新后的文件时
        //直接变为获得有之前加载的文件，因为之前加载的，无法被卸载掉
        //故：要热更新，必须实现自定义类加载器，才能卸载之前加载的类
        //注：使用自定义类加载器卸载类是也是必须在当前上下午才能卸载成功，否则失败
        Long lastModi = 0L;
        while (true){
            File file = new File("E:\\daiMa_info\\intellij\\动态加载\\src\\com\\zys\\myClass\\jiaZaiClass.java");
            if (file.lastModified() > lastModi)//判断最后一次修改时间大于之前的时间
            {
                System.out.println("if内");
                lastModi = file.lastModified();


                URL[] paths = new URL[]{new URL("file:E:\\daiMa_info\\intellij\\动态加载\\src")};

                //创建一个类加载器，父加载器是AppClassLoader
                URLClassLoader urlClassLoader = new URLClassLoader(paths, HofixTest.class.getClassLoader());

                //阶段1.2.3.4加载
                Class<?> clazz = urlClassLoader.loadClass("com.zys.myClass.jiaZaiClass");

                System.out.println("helloService用的类加载器" + clazz.getClassLoader());
                System.out.println("heloService用的类加载器父加载器" + clazz.getClassLoader().getParent());


                //6.使用
                Object obj = clazz.newInstance();
                Method method = clazz.getMethod("hello", null);

                //调用其中的方法,参数为从obj中调用，该方法参数0个为null
                method.invoke(obj, null);
                //将所有使用过的对象都赋值为null，便于让垃圾回收器回收
                method = null;
                obj=null;
                clazz = null;
                urlClassLoader = null;
                paths = null;
                //提醒垃圾回收器，现在开始进行一次垃圾回收
                System.gc();
            }
            System.out.println("if外");
            //每次循环线程停顿1秒
            Thread.sleep(2000L);
        }
    }
}
