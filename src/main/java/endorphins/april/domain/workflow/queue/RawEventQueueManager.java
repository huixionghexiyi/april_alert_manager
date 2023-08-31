package endorphins.april.domain.workflow.queue;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import endorphins.april.domain.rawevent.RawEvent;
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
