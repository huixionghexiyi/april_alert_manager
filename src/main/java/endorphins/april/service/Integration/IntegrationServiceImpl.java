package endorphins.april.service.Integration;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.BlockingQueue;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import endorphins.april.entity.ApiKey;
import endorphins.april.entity.Event;
import endorphins.april.repository.ApiKeyRepository;
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
    public boolean events(String apiKey, List<Event> event) {
        Optional<ApiKey> apiKeyEntity = apiKeyRepository.findById(apiKey);
        if (apiKeyEntity.isPresent()) {
            Long tenantId = apiKeyEntity.get().getTenantId();
            BlockingQueue<Event> eventBlockingQueue = eventQueueManager.getQueueByTenantId(tenantId);
            if (eventBlockingQueue == null) {
                throw new NullPointerException("this tenant [" + tenantId + "] dont have a queue");
            }
            eventBlockingQueue.addAll(event);
            return true;
        }
        return false;
    }
}
