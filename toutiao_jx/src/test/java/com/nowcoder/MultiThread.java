package com.nowcoder;

import java.util.Random;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

class MyThread extends Thread {
    private int tid;
    public MyThread(int tid) {
        this.tid = tid;
    }

    @Override
    public void run() {
        try {
            for (int i = 0; i < 10; ++i) {
                Thread.sleep(1000);
                System.out.println(String.format("T%d:%d", tid, i));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

class Producer implements Runnable {
    private BlockingQueue<String> q;//BlockingQueue同步和redis异步，存储机制不一样，都可以通过多线程方式，效果一样
                                    //BlockingQueue的服务器只能是一台，两台的话没有办法公用
    public Producer(BlockingQueue<String> q) {
        this.q = q;
    }
    @Override
    public void run() {
        try {
            for (int i = 0; i < 100; ++i) {
                Thread.sleep(10);//睡一秒
                q.put(String.valueOf(i));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

class Consumer implements Runnable {
    private BlockingQueue<String> q;//BlockingQueue同步队列，不是线程相关的东西，可以通过线程放到BlockingQueue里面
                                    //线程就是为了有个队列，有进有出，提高效率，是我们原有的事情异步化
    public Consumer(BlockingQueue<String> q) {
        this.q = q;
    }
    @Override
    public void run() {
        try {
            while (true) {
                System.out.println(Thread.currentThread().getName() + ":" + q.take());//打印线程名字，有就取出来没有就卡着
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

public class MultiThread {
    public static void testThread() {
        for (int i = 0; i < 10; ++i) {
            //new MyThread(i).start();
        }

        for (int i = 0; i < 10; ++i) {
            final int tid = i;
            new Thread(new Runnable() {
                @Override
                public void run() {    //extends Thread,重载run()方法；implements Runnable(),实现run()方法
                    try {
                        for (int i = 0; i < 10; ++i) {
                            Thread.sleep(1000);
                            System.out.println(String.format("T2%d:%d", tid, i));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
    }

    private static Object obj = new Object();

    public static void testSynchronized1() {
        synchronized (obj) {
            try {
                for (int i = 0; i < 10; ++i) {
                    Thread.sleep(1000);
                    System.out.println(String.format("T3%d", i));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void testSynchronized2() {
        synchronized (new Object()) {
            try {
                for (int i = 0; i < 10; ++i) {
                    Thread.sleep(1000);
                    System.out.println(String.format("T4%d", i));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void testSynchronized() {
        for (int i = 0; i < 10; ++i) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    testSynchronized1();
                    testSynchronized2();
                }
            }).start();
        }
    }

    public static void testBlockingQueue() {//测试一下
        BlockingQueue<String> q = new ArrayBlockingQueue<String>(10);
        new Thread(new Producer(q)).start();
        new Thread(new Consumer(q), "Consumer1").start();
        new Thread(new Consumer(q), "Consumer2").start();
    }

    //多线程读写变量的时候
    private static int counter = 0;//变量
    private static AtomicInteger atomicInteger = new AtomicInteger(0);
    public static void sleep(int mills) {
        try {
         //Thread.sleep(new Random().nextInt(mills));
            Thread.sleep(mills);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    //开10个线程，每个线程对数字加10
    public static void testWithAtomic() {
        for (int i = 0; i < 10; ++i) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    sleep(1000);
                    for (int j = 0; j < 10; ++j) {
                        System.out.println(atomicInteger.incrementAndGet());//每个线程里随机对线程进行加减
                    }
                }
            }).start();
        }
    }
    //多线程变量进行加减
    public static void testWithoutAtomic() {
        for (int i = 0; i < 10; ++i) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    sleep(1000);
                    for (int j = 0; j < 10; ++j) {
                        counter++;
                        System.out.println(counter);
                    }
                }
            }).start();
        }
    }

    public static void testAtomic() {
        testWithAtomic();
        testWithoutAtomic();
    }

    private static ThreadLocal<Integer> threadLocalUserIds = new ThreadLocal<>();
    private static int userId;

    public static void testThreadLocal() {
        for (int i = 0; i < 10; ++i) {
            final int finalI = i;
            new Thread(new Runnable() {
                @Override
                public void run() {
                    threadLocalUserIds.set(finalI);
                    sleep(1000);
                    System.out.println("ThreadLocal: " + threadLocalUserIds.get());
                }
            }).start();
        }

        for (int i = 0; i < 10; ++i) {
            final int finalI = i;
            new Thread(new Runnable() {
                @Override
                public void run() {
                    userId = finalI;
                    sleep(1000);
                    System.out.println("NonThreadLocal: " + userId);
                }
            }).start();
        }
    }

    public static void testExecutor() {
        //ExecutorService service = Executors.newSingleThreadExecutor();//单线程
        ExecutorService service = Executors.newFixedThreadPool(2);//两条线程
        service.submit(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 10; ++i) {
                    sleep(1000);
                    System.out.println("Execute1 " + i);
                }
            }
        });

        service.submit(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 10; ++i) {
                    sleep(1000);
                    System.out.println("Execute2 " + i);
                }
            }
        });

        service.shutdown();
        while (!service.isTerminated()) {
            sleep(1000);
            System.out.println("Wait for termination.");
        }
    }

    //正常情况下也会抛异常
    public static void testFutrue() {
        ExecutorService service = Executors.newSingleThreadExecutor();//单线程
        Future<Integer> future = service.submit(new Callable<Integer>() {  //保存
            public Integer call() throws Exception {
                sleep(1000);  //延迟1秒
                return 1;
//                throw new IllegalArgumentException("异常");
            }
        });

        service.shutdown();

        try {
//            System.out.println(future.get());
            System.out.println(future.get(100, TimeUnit.MILLISECONDS));//在get时设置时间点，结果“超时”
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void main(String[] argv) {
//        testThread();//extends Thread,重载run()方法；implements Runnable(),实现run()方法
//        testSynchronized();//内置锁。放在方法上会锁住所有Synchronized方法；Synchronized（obj）锁住相关的代码段
//        testBlockingQueue();//同步队列
//        testAtomic();
//        testThreadLocal();/*线程局部变量，即使是一个static成员，每个线程访问的变量是不同的；
//                            常见于web中存储当前用户到一个静态工具类种，在线程的任何地方都可以访问到当前线程的用户；
//                            参考HostHolder.java里的users*/
//        testExecutor();/*提供一个运行任务的框架；
//                         将任务和如何运行任务解耦；
//                         常用于提供线程池或定时任务服务；*/
        testFutrue();/*返回异步结果；
                       阻塞等待返回结果；
                       timeout设置时间点等结果;
                       获取线程中的Exception*/
    }
}
