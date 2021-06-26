package com.xxxx.seckill.vo;

public class Singleton {
    private static Singleton instacne;

    private Singleton(){

    }
    public static Singleton getInstace1(){
        if(instacne==null)
            instacne=new Singleton();
        return instacne;
    }
    public Singleton getInstance2(){
        if(instacne==null){
            synchronized (this){
                if(instacne==null)
                    instacne=new Singleton();
            }
        }
        return instacne;

    }
    private static class GetInstance{

            private static final Singleton Instacne=new Singleton();

    }
    public  Singleton getInstacne3(){
        return GetInstance.Instacne;
    }
}
