package com.eb.geaiche.util;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CameraThreadPool {

    /*
     * 线程池大小
     */
    private static int poolCount = Integer.MAX_VALUE;


//    private static ExecutorService fixedThreadPool = Executors.newFixedThreadPool(poolCount);
    private static ExecutorService fixedThreadPool = Executors.newCachedThreadPool();
//    private static ExecutorService fixedThreadPool = new ThreadPoolExecutor(0, Integer.MAX_VALUE, 0, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<>());

    /**
     * 给线程池添加任务
     *
     * @param runnable 任务
     */
    public static void execute(Runnable runnable) {
        if (null != runnable)
            fixedThreadPool.execute(runnable);
    }


    /**
     * 中止线程池中全部的线程的执行
     */
    public static void shutdownNow() {
//        fixedThreadPool.shutdownNow();
    }


}
