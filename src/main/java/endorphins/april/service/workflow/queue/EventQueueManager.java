package endorphins.april.service.workflow.queue;

import java.util.Map;

import org.springframework.context.annotation.Configuration;

import endorphins.april.entity.ApiKey;

import com.google.common.collect.Maps;

/**
 * @author timothy
 * @DateTime: 2023/8/24 15:40
 **/
@Configuration
public class EventQueueManager {
    /**
     * key: userId, value: eventQueue
     */
    private final Map<Long, BlockingEventQueue> queueMap = Maps.newHashMap();

    /**
     * key:apiKey, value:userId
     */
    private final Map<String, Long> apiKeyUserMap = Maps.newHashMap();

    public BlockingEventQueue getQueueByApiKey(String apiKey) {
        Long userId = apiKeyUserMap.get(apiKey);
        if (userId == null) {
            return null;
        }
        return queueMap.get(userId);
    }

    public BlockingEventQueue getQueueByUserId(Long userId) {
        return queueMap.get(userId);
    }

    public void addEventQueue(String apiKey, Long userId, Long tenantId, int queueSize) {
        apiKeyUserMap.put(apiKey, userId);
        if (queueMap.containsKey(userId)) {
            return;
        }
        BlockingEventQueue eventQueue = new BlockingEventQueue(queueSize);
        eventQueue.setUserId(userId);
        eventQueue.setTenantId(tenantId);
        queueMap.put(userId, eventQueue);
    }
}
