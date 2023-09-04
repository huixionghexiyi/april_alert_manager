package endorphins.april.service.Integration;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.BlockingQueue;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import endorphins.april.entity.ApiKey;
import endorphins.april.entity.Event;
import endorphins.april.repository.ApiKeyRepository;
import endorphins.april.service.workflow.WorkflowEvent;
import endorphins.april.service.workflow.queue.EventQueueManager;

/**
 * @author timothy
 * @DateTime: 2023/8/22 17:32
 **/
@Service
public class IntegrationServiceImpl implements IntegrationService {

    @Autowired
    private EventQueueManager eventQueueManager;

    @Autowired
    private ApiKeyRepository apiKeyRepository;

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
}
