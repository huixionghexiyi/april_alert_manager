package endorphins.april.service.Integration;

import endorphins.april.entity.IngestionInstance;
import endorphins.april.model.Event;
import endorphins.april.model.ingestion.IngestionInstanceVo;
import endorphins.april.model.ingestion.PostStatus;
import endorphins.april.repository.IngestionInstanceRepository;
import endorphins.april.service.workflow.event.EventBlockingQueue;
import endorphins.april.service.workflow.event.EventQueueManager;
import endorphins.april.service.workflow.event.WorkflowEvent;
import endorphins.april.service.workflow.rawevent.RawEventBlockingQueue;
import endorphins.april.service.workflow.rawevent.RawEventConsumerManager;
import endorphins.april.service.workflow.rawevent.RawEventQueueManager;
import endorphins.april.service.workflow.rawevent.WorkflowRawEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

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

    @Autowired
    private RawEventQueueManager rawEventQueueManager;

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
        // TODO 停止 ingestion instance cosumer
        return true;
    }

    /**
     * 自定义原始告警
     *
     * @param apiKey
     * @param ingestionId
     * @param rawEvent
     * @return
     */
    @Override
    public boolean custom(String apiKey, String ingestionId, Map<String, Object> rawEvent) {
        WorkflowRawEvent workflowRawEvent = new WorkflowRawEvent(rawEvent);
        RawEventBlockingQueue queue = rawEventQueueManager.getQueueByIngestionId(ingestionId);
        queue.add(workflowRawEvent);
        return true;
    }

    @Override
    public boolean create(IngestionInstanceVo ingestionInstanceVo) {
        // TODO 规范性验证
        IngestionInstance instance = instanceRepository.save(IngestionInstance.createByVo(ingestionInstanceVo));
        rawEventConsumerManager.addConsumer(instance);
        return true;
    }
}
