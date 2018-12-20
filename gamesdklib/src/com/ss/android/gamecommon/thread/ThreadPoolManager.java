package com.ss.android.gamecommon.thread;

import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 线程池管理
 */
public class ThreadPoolManager {
    private static ThreadPoolManager sInstance;

    public static ThreadPoolManager getInstance() {
        if (null == sInstance) {
            synchronized (ThreadPoolManager.class) {
                if (null == sInstance) {
                    sInstance = new ThreadPoolManager();
                }
            }
        }
        return sInstance;
    }

    private int corePoolSize;//核心线程数
    private int maximumPoolSize;//最大线程数
    private long keepAliveTime = 3;//存活时间
    private TimeUnit unit = TimeUnit.MINUTES;//时间单位分钟
    private ThreadPoolExecutor executor;

    private ThreadPoolManager() {
        /**
         * 给核心线程赋值，当前设备可用处理器核心数*2+1，能够让cpu的效率得到最大程度执行
         */
        corePoolSize = Runtime.getRuntime().availableProcessors() * 2 + 1;
        maximumPoolSize = corePoolSize;
        executor = new ThreadPoolExecutor(
                corePoolSize,
                maximumPoolSize,
                keepAliveTime,
                unit,
                new LinkedBlockingDeque<Runnable>(),//缓冲队列，用来存放等待任务，Lineked的先进先出（无界队列）
                Executors.defaultThreadFactory(),//创建线程工厂
                new ThreadPoolExecutor.AbortPolicy()//用来对超过最大线程的任务的处理策略
        );
        executor.allowCoreThreadTimeOut(true);
    }

    /**
     * 执行任务
     *
     * @param runnable
     */
    public void executor(Runnable runnable) {
        if (null == runnable) {
            return;
        }
        executor.execute(runnable);

    }

    /**
     * 从线程池中移除任务
     */

    public void remove(Runnable runnable) {
        if (null == runnable) {
            return;
        }
        executor.remove(runnable);
    }


}
