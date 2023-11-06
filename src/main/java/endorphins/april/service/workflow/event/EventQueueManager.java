package endorphins.april.service.workflow.event;

import java.util.Map;

import org.springframework.context.annotation.Configuration;

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
    private final Map<Long, EventBlockingQueue> queueMap = Maps.newHashMap();

    /**
     * key:apiKey, value:userId
     * 一个用户可能有多个 apiKey，所以需要多加一层映射表
     */
    private final Map<String, Long> apiKeyUserMap = Maps.newHashMap();

    public EventBlockingQueue getQueueByApiKey(String apiKey) {
        Long userId = apiKeyUserMap.get(apiKey);
        if (userId == null) {
            return null;
        }
        return queueMap.get(userId);
    }

    public EventBlockingQueue getQueueByUserId(Long userId) {
        return queueMap.get(userId);
    }

    public void addEventQueue(String apiKey, Long userId, Long tenantId, int queueSize) {
        apiKeyUserMap.put(apiKey, userId);
        if (queueMap.containsKey(userId)) {
            return;
        }
        EventBlockingQueue eventQueue = new EventBlockingQueue(queueSize);
        eventQueue.setUserId(userId);
        eventQueue.setTenantId(tenantId);
        queueMap.put(userId, eventQueue);
    }
}
