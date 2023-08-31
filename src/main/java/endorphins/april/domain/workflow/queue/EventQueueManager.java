package endorphins.april.domain.workflow.queue;

import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import endorphins.april.domain.event.model.Event;
import lombok.Data;

/**
 * @author timothy
 * @DateTime: 2023/8/24 15:40
 **/
@Configuration
public class EventQueueManager {
    private BlockingQueue<Event> eventQueue = new ArrayBlockingQueue<>(16);

    public void put(Event event) throws InterruptedException {
        eventQueue.put(event);
    }

    public void putAll(List<Event> eventList) {
        eventQueue.addAll(eventList);
    }

    public Event take() throws InterruptedException {
        return eventQueue.take();
    }

}
