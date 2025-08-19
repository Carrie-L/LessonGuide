package com.concurrency.c01;

public class MemoryVisibilityDemo {

    private static volatile boolean flag = false;
    public static void main(String[] args){
        // 线程1：读取数据
        Thread readThread = new Thread(()->{
            System.out.println("Reader thread started. Waiting for flag to become true...");
            while (!flag){
                // 忙等待循环，它会持续消耗CPU资源
            }
            System.out.println("Reader thread finished. Flag is now true.");
        });

        // 线程2： 写入数据
        Thread writerThread = new Thread(()->{
            System.out.println("Writer thread started. Will set flag to true after 2 seconds.");
            try{
                Thread.sleep(2000); // 模拟数据准备过程
            }catch(InterruptedException e){
                e.printStackTrace();
            }
            flag = true;
            System.out.println("Writer thread finished. Flag set to true");
        });

        readThread.start();
        writerThread.start();

    }

}
