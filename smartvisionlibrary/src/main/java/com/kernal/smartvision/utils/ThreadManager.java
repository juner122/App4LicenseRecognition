package com.kernal.smartvision.utils;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by Wintone on 2017/12/8.
 */

public class ThreadManager {

    private static ThreadPoolProxy threadPoolProxy;


    /**
     * 获取单例的线程池对象
     *
     * @return
     */
    public static ThreadPoolProxy getInstance() {
        if (threadPoolProxy == null) {
            synchronized (ThreadManager.class) {
                if (threadPoolProxy == null) {
                    // 获取处理器数量
                    int cpuNum = Runtime.getRuntime().availableProcessors();
                    // 根据cpu数量,计算出合理的线程并发数
                    int threadNum = 1;
                    //默认是双核的cpu 每个核心走一个线程 一个等待线程
                    threadPoolProxy = new ThreadPoolProxy(threadNum - 1, threadNum, Integer.MAX_VALUE);
                }
            }
        }
        return threadPoolProxy;
    }

    /**
     * 代理设计模式类似一个中介，所以在中介这里有我们真正想获取的对象
     * 所以要先获取代理，再获取这个线程池
     */
    public static class ThreadPoolProxy {
        /**
         * 核心线程数
         */
        private int mCorePoolSize;
        /**
         * 最大线程数
         */
        private int mMaximumPoolSize;
        /**
         * 所有任务执行完毕后普通线程回收的时间间隔
         */
        private long mKeepAliveTime;
        /**
         * 代理对象内部保存的是原来类的对象
         */
        private ThreadPoolExecutor mPool;

        /**
         * 赋值
         *
         * @param corePoolSize
         * @param maximumPoolSize
         * @param keepAliveTime
         */
        public ThreadPoolProxy(int corePoolSize, int maximumPoolSize, long keepAliveTime) {
            this.mCorePoolSize = corePoolSize;
            this.mMaximumPoolSize = maximumPoolSize;
            this.mKeepAliveTime = keepAliveTime;
        }
        TimeUnit unit = TimeUnit.MILLISECONDS;
        //阻塞队列,FIFO,大小有限制，为3个
        BlockingQueue<Runnable> workQueue = new ArrayBlockingQueue<Runnable>(3);
        //线程工厂
        ThreadFactory threadFactory = Executors.defaultThreadFactory();
        //异常捕获器,不做任何处理
        RejectedExecutionHandler handler = new ThreadPoolExecutor.DiscardPolicy();
        private void initPool() {
            if (mPool == null || mPool.isShutdown()) {
                // 创建线程池
                mPool = new ThreadPoolExecutor(mCorePoolSize,
                        mMaximumPoolSize,
                        mKeepAliveTime,
                        unit,
                        workQueue,
                        threadFactory,
                        handler);
            }
        }


        /**
         * 执行任务
         *
         * @param task
         */
        public void execute(Runnable task) {
            initPool();
            //执行任务
            mPool.execute(task);
        }

        /**
         * 提交任务
         *
         * @param task
         * @return
         */
        public Future<?> submit(Runnable task) {
            initPool();
            return mPool.submit(task);
        }

        /**
         * 取消任务
         *
         * @param task
         */
        public void remove(Runnable task) {
            if (mPool != null && !mPool.isShutdown()) {
                mPool.getQueue()
                        .remove(task);
            }
        }
    }
}
