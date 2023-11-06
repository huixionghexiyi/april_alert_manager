package endorphins.april.service.Integration;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import endorphins.april.entity.IngestionInstance;
import endorphins.april.model.Event;
import endorphins.april.model.ingestion.IngestionInstanceVo;
import endorphins.april.model.ingestion.PostStatus;
import endorphins.april.repository.IngestionInstanceRepository;
import endorphins.april.service.workflow.event.WorkflowEvent;
import endorphins.april.service.workflow.rawevent.RawEventConsumerManager;
import endorphins.april.service.workflow.event.EventQueueManager;
import endorphins.april.service.workflow.event.EventBlockingQueue;

/**
 * @author timothy
 * @DateTime: 2023/8/22 17:32
 **/
@Service
public class IngestionServiceImpl implements IngestionService {

    @Autowired
    private EventQueueManager eventQueueManager;

    @Autowired
    private IngestionInstanceRepository instanceRepository;

    @Autowired
    private RawEventConsumerManager rawEventConsumerManager;

    @Override
    public boolean events(String apiKey, List<Event> events) {
        // TODO 对 events 的合法性进行校验 description,severity,source,check not be null
        EventBlockingQueue eventBlockingQueue = eventQueueManager.getQueueByApiKey(apiKey);
        if (eventBlockingQueue == null) {
            throw new NullPointerException("this apiKey [" + apiKey + "] dont have a queue");
        }

        List<WorkflowEvent> workflowEventList = WorkflowEvent.createByEvent(events);
        eventBlockingQueue.addAll(workflowEventList);
        return true;
    }

    @Override
    public boolean status(PostStatus status) {
        IngestionInstance ingestionInstance = new IngestionInstance();
        ingestionInstance.setId(status.getIngestionInstanceId());
        ingestionInstance.setStatus(status.getStatus());
        instanceRepository.save(ingestionInstance);
        // TODO 停止 ingestion instance
        return true;
    }

    @Override
    public boolean custom(String apiKey, String ingestionId, ArrayList<Event> events) {
        // TODO
        return false;
    }

    @Override
    public boolean create(IngestionInstanceVo ingestionInstanceVo) {
        // TODO 规范性验证
        IngestionInstance instance = instanceRepository.save(IngestionInstance.createByVo(ingestionInstanceVo));
        rawEventConsumerManager.addConsumer(instance);

        return true;
    }
}
