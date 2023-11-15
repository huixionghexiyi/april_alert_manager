package cn.endorphin.atevent.service.Integration;

import cn.endorphin.atevent.entity.IngestionInstance;
import cn.endorphin.atevent.model.Event;
import cn.endorphin.atevent.model.ingestion.IngestionInstanceStatus;
import cn.endorphin.atevent.model.ingestion.IngestionInstanceVo;
import cn.endorphin.atevent.model.ingestion.PostStatus;
import cn.endorphin.atevent.repository.IngestionInstanceRepository;
import cn.endorphin.atevent.workflow.event.EventBlockingQueue;
import cn.endorphin.atevent.workflow.event.EventQueueManager;
import cn.endorphin.atevent.workflow.event.WorkflowEvent;
import cn.endorphin.atevent.workflow.rawevent.RawEventBlockingQueue;
import cn.endorphin.atevent.workflow.rawevent.RawEventConsumerManager;
import cn.endorphin.atevent.workflow.rawevent.RawEventQueueManager;
import cn.endorphin.atevent.workflow.rawevent.WorkflowRawEvent;
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
        if (status.getStatus() == IngestionInstanceStatus.Stopped) {
            rawEventConsumerManager.stopConsumer(ingestionInstance);
        } else {
            rawEventConsumerManager.startConsumer(ingestionInstance);
        }

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
        WorkflowRawEvent workflowRawEvent = new WorkflowRawEvent(ingestionId, rawEvent);
        RawEventBlockingQueue queue = rawEventQueueManager.getQueueByIngestionId(ingestionId);
        queue.add(workflowRawEvent);
        return true;
    }

    @Override
    public String create(IngestionInstanceVo ingestionInstanceVo) {
        // TODO 规范性验证
        IngestionInstance instance = instanceRepository.save(IngestionInstance.createByVo(ingestionInstanceVo));
        rawEventConsumerManager.startConsumer(instance);
        return instance.getId();
    }
}
