package com.zys.myClass;

public class jiaZaiClass {
    public  void  hello()
    {
        System.out.println("-------------3--");
        System.out.println("hello我是被："+this.getClass().getClassLoader()+"创建出来的");
        System.out.println("kjj");
    }
}
