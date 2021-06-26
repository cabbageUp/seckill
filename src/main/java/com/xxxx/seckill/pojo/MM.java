package com.xxxx.seckill.pojo;

public class MM {
    public static int f(int value) {
        try {
            return value * value;
        } finally {
            value=3;
            return 5;
        }
    }
    public static void main(String[] args){
        System.out.println(f(2));
        }


}
