package endorphins.april.service.workflow.event;

import com.google.common.collect.Maps;
import endorphins.april.config.AtEventConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author timothy
 * @DateTime: 2023/8/24 15:40
 **/
@Component
@Slf4j
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

    @Autowired
    private AtEventConfig atEventConfig;

    public EventBlockingQueue getQueueByApiKey(String apiKey) {
        Long userId = apiKeyUserMap.get(apiKey);
        if (userId == null) {
            log.debug("not find userId in apiKeyUserMap, apiKey:{}", apiKey);
            return null;
        }
        return queueMap.get(userId);
    }

    public EventBlockingQueue getQueueByUserId(Long userId) {
        return queueMap.get(userId);
    }

    public void addEventQueue(String apiKey, Long userId, Long tenantId) {
        apiKeyUserMap.put(apiKey, userId);
        if (queueMap.containsKey(userId)) {
            return;
        }
        EventBlockingQueue eventQueue = new EventBlockingQueue(atEventConfig.getDefaultEventQueueSize());
        eventQueue.setUserId(userId);
        eventQueue.setTenantId(tenantId);
        queueMap.put(userId, eventQueue);
    }

    private void checkAndAddQueueByUserId(Long userId) {
        if (!queueMap.containsKey(userId)) {
            synchronized (queueMap) {
                if (!queueMap.containsKey(userId)) {
                    EventBlockingQueue eventBlockingQueue = new EventBlockingQueue(atEventConfig.getDefaultEventQueueSize());
                    queueMap.put(userId, eventBlockingQueue);
                }
            }
        }
    }
}
