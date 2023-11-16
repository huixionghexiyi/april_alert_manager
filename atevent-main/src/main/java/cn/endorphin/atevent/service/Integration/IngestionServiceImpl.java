package cn.endorphin.atevent.service.Integration;

import cn.endorphin.atevent.entity.IngestionInstance;
import cn.endorphin.atevent.infrastructure.exception.ApplicationException;
import cn.endorphin.atevent.model.Event;
import cn.endorphin.atevent.model.ingestion.IngestionInstanceStatus;
import cn.endorphin.atevent.model.ingestion.IngestionInstanceVo;
import cn.endorphin.atevent.model.ingestion.IngestionQueryParam;
import cn.endorphin.atevent.model.ingestion.PostStatus;
import cn.endorphin.atevent.repository.base.IngestionInstanceCustomizedRepository;
import cn.endorphin.atevent.workflow.event.EventBlockingQueue;
import cn.endorphin.atevent.workflow.event.EventQueueManager;
import cn.endorphin.atevent.workflow.event.WorkflowEvent;
import cn.endorphin.atevent.workflow.rawevent.RawEventBlockingQueue;
import cn.endorphin.atevent.workflow.rawevent.RawEventConsumerManager;
import cn.endorphin.atevent.workflow.rawevent.RawEventQueueManager;
import cn.endorphin.atevent.workflow.rawevent.WorkflowRawEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * @author timothy
 * @DateTime: 2023/8/22 17:32
 **/
@Service
public class IngestionServiceImpl implements IngestionService {

    @Autowired
    private EventQueueManager eventQueueManager;

    @Autowired
    private IngestionInstanceCustomizedRepository instanceRepository;

    @Autowired
    private RawEventConsumerManager rawEventConsumerManager;

    @Autowired
    private RawEventQueueManager rawEventQueueManager;

    @Override
    public Page<IngestionInstance> list(IngestionQueryParam param, Integer page, Integer size) {

        return instanceRepository.page(param, page, size);
    }

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
        Optional<IngestionInstance> ingestionInstanceOpt = instanceRepository.findById(status.getIngestionInstanceId());
        if (ingestionInstanceOpt.isPresent()) {
            IngestionInstance ingestionInstance = ingestionInstanceOpt.get();
            ingestionInstance.setStatus(status.getStatus());
            instanceRepository.update(ingestionInstance);
            if (status.getStatus() == IngestionInstanceStatus.STOPPED) {
                rawEventConsumerManager.stopConsumer(ingestionInstance);
            } else {
                rawEventConsumerManager.startConsumer(ingestionInstance);
            }
        } else {
            throw new ApplicationException("the ingestion instance is not found");
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
        if (queue == null) {
            throw new ApplicationException("ingestion [" + ingestionId + "] is invalid, please check it was stopped or removed");
        }
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
