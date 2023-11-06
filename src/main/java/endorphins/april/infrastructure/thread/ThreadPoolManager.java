package endorphins.april.infrastructure.thread;

import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.springframework.stereotype.Component;

/**
 * @author timothy
 * @DateTime: 2023/8/22 17:34
 **/
@Component
public class ThreadPoolManager {

    private ThreadPoolExecutor rawEventThreadPool;

    private ThreadPoolExecutor eventConsumer;

    private Object rawEventLock = new Object();

    private Object eventConsumerLock = new Object();

    public ThreadPoolExecutor getRawEventConsumerThreadPool() {
        if (rawEventThreadPool == null) {
            synchronized (rawEventLock) {
                if (rawEventThreadPool == null) {
                    rawEventThreadPool =
                        new ThreadPoolExecutor(8, 16, 0, TimeUnit.NANOSECONDS,
                            new LinkedBlockingDeque<>());
                }
            }
        }
        return rawEventThreadPool;
    }

    public ThreadPoolExecutor getEventConsumerThreadPool() {
        if (eventConsumer == null) {
            synchronized (eventConsumerLock) {
                if (eventConsumer == null) {
                    eventConsumer = new ThreadPoolExecutor(4, 16, 0, TimeUnit.NANOSECONDS,
                        new LinkedBlockingDeque<>());
                }
            }
        }
        return eventConsumer;
    }
}
