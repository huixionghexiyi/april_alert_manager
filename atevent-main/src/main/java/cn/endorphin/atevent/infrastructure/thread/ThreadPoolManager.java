package cn.endorphin.atevent.infrastructure.thread;

import org.springframework.stereotype.Component;

import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author timothy
 * @DateTime: 2023/8/22 17:34
 **/
@Component
public class ThreadPoolManager {

    private ThreadPoolExecutor rawEventThreadPool;
    private ThreadPoolExecutor rawEventWorkerThreadPool;

    private ThreadPoolExecutor eventConsumer;

    private Object rawEventLock = new Object();
    private Object workerLock = new Object();

    private Object eventConsumerLock = new Object();

    public ThreadPoolExecutor getRawEventConsumerThreadPool() {
        if (rawEventThreadPool == null) {
            synchronized (rawEventLock) {
                if (rawEventThreadPool == null) {
                    rawEventThreadPool =
                            new ThreadPoolExecutor(2, 4, 0, TimeUnit.NANOSECONDS,
                                    new LinkedBlockingDeque<>());
                }
            }
        }
        return rawEventThreadPool;
    }

    public ThreadPoolExecutor getWorkerThreadPool() {
        if (rawEventWorkerThreadPool == null) {
            synchronized (workerLock) {
                if (rawEventWorkerThreadPool == null) {
                    rawEventWorkerThreadPool =
                            new ThreadPoolExecutor(2, 4, 0, TimeUnit.NANOSECONDS,
                                    new LinkedBlockingDeque<>());
                }
            }
        }
        return rawEventWorkerThreadPool;
    }

    public ThreadPoolExecutor getEventConsumerThreadPool() {
        if (eventConsumer == null) {
            synchronized (eventConsumerLock) {
                if (eventConsumer == null) {
                    eventConsumer = new ThreadPoolExecutor(2, 4, 0, TimeUnit.NANOSECONDS,
                            new LinkedBlockingDeque<>());
                }
            }
        }
        return eventConsumer;
    }
}
