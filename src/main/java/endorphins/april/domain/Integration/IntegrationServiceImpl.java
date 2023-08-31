package endorphins.april.domain.Integration;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import endorphins.april.domain.workflow.queue.EventQueueManager;
import endorphins.april.domain.event.model.Event;

/**
 * @author timothy
 * @DateTime: 2023/8/22 17:32
 **/
@Service
public class IntegrationServiceImpl implements IntegrationService {

    @Autowired
    private EventQueueManager eventQueueManager;
    @Override
    public boolean events(List<Event> event) {
        eventQueueManager.putAll(event);
        return true;
    }
}
