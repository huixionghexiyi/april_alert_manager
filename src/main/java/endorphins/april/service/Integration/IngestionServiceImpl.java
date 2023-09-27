package endorphins.april.service.Integration;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.BlockingQueue;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import endorphins.april.entity.ApiKey;
import endorphins.april.entity.IngestionInstance;
import endorphins.april.model.Event;
import endorphins.april.model.ingestion.IngestionInstanceVo;
import endorphins.april.model.PostStatus;
import endorphins.april.repository.ApiKeyRepository;
import endorphins.april.repository.IngestionInstanceRepository;
import endorphins.april.service.workflow.WorkflowEvent;
import endorphins.april.service.workflow.queue.EventQueueManager;

/**
 * @author timothy
 * @DateTime: 2023/8/22 17:32
 **/
@Service
public class IngestionServiceImpl implements IngestionService {

    @Autowired
    private EventQueueManager eventQueueManager;

    @Autowired
    private ApiKeyRepository apiKeyRepository;

    @Autowired
    private IngestionInstanceRepository instanceRepository;

    @Override
    public boolean events(String apiKey, List<Event> events) {
        // TODO 对 events 的合法性进行校验
        Optional<ApiKey> apiKeyEntity = apiKeyRepository.findById(apiKey);
        if (apiKeyEntity.isPresent()) {
            Long tenantId = apiKeyEntity.get().getTenantId();
            BlockingQueue<WorkflowEvent> eventBlockingQueue = eventQueueManager.getQueueByTenantId(tenantId);
            if (eventBlockingQueue == null) {
                throw new NullPointerException("this tenant [" + tenantId + "] dont have a queue");
            }
            List<WorkflowEvent> workflowEventList =
                WorkflowEvent.createByEvent(events, tenantId, apiKeyEntity.get().getCreateUserId());
            eventBlockingQueue.addAll(workflowEventList);
            return true;
        } else {
            throw new IllegalArgumentException("apiKey is invalid,please check.");
        }
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
        IngestionInstance instance = new IngestionInstance();
        instance.setName(ingestionInstanceVo.getName());
        instance.setDescription(ingestionInstanceVo.getDescription());
        instance.setApiKey(ingestionInstanceVo.getApiKey());
        instance.setSourceId(ingestionInstanceVo.getSourceId());
        instance.setSourceType(ingestionInstanceVo.getSourceType());
        instanceRepository.save(instance);
        // TODO create consumer

        return true;
    }
}
