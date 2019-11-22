package com.mei.util;

import java.util.concurrent.*;

/**
 * @Description:
 * @Author: Administrator
 * @Date: 2019/11/22 17:09
 * @Version: V1.0
 **/
public class TaskPool {
    /**
     * 核心线程池大小：始终存活
     */
    private int corePoolSize = 10;
    /**
     * 最大线程池大小：空闲时间达到会销毁
     */
    private int maximumPoolSize = 20;
    /**
     * 线程最大空闲时间
     */
    private int keepAliveTime = 60;
    /**
     * 时间单位
     */
    private TimeUnit unit = TimeUnit.SECONDS;
    /**
     * 线程等待队列：任务缓存池
     */
    private BlockingQueue workQueue = new ArrayBlockingQueue<>(10);

    /**
     * 控制运行中的线程数
     */
    private int runSize = 0;

    /**
     * 线程池接口
     */
    private ThreadPoolExecutor threadPoolExecutor = null;

    public ThreadPoolExecutor getThreadPoolExecutor() {
        return threadPoolExecutor;
    }

    /**
     * 有界队列
     *
     * @param corePoolSize
     * @param maximumPoolSize
     * @param keepAliveTime
     * @param unit
     * @param workQueue
     */
    public TaskPool(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue) {
        this.runSize = maximumPoolSize;
        threadPoolExecutor = new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, (r, executor) -> System.out.println("超出缓冲池大小,任务丢弃"));
    }

    /**
     * 无界队列：【超出Integer.MAX,才会执行拒绝策略】
     *
     * @param runSize
     */
    public TaskPool(int runSize) {
        this.runSize = runSize;
        threadPoolExecutor = new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime, unit, new LinkedBlockingDeque<>(), (r, executor) -> System.out.println("超出缓冲池大小,任务丢弃"));
    }


    public void submitTask(Runnable task) {
        /**
         * 控制运行的线程
         */
        if (threadPoolExecutor.getActiveCount() <= runSize) {
            threadPoolExecutor.submit(task);
        } else {
            System.out.println("任务过多，稍后重试！");
        }

    }


}
