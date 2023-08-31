package endorphins.april.service.workflow.queue;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import org.springframework.stereotype.Component;

import endorphins.april.model.RawEvent;
import lombok.Data;

/**
 * @author timothy
 * @DateTime: 2023/8/24 15:40
 **/
@Component
@Data
public class RawEventQueueManager {
    private BlockingQueue<RawEvent> queue =new ArrayBlockingQueue<>(16);

    public void put(RawEvent rawEvent) throws InterruptedException {
        queue.put(rawEvent);
    }
}
