package endorphins.april.service.workflow.rawevent;

import java.util.Map;

import endorphins.april.config.AtEventConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import lombok.Data;

import com.google.common.collect.Maps;

/**
 * @author timothy
 * @DateTime: 2023/8/24 15:40
 **/
@Component
@Data
public class RawEventQueueManager {

    @Autowired
    private AtEventConfig atEventConfig;

    private final Map<String, RawEventBlockingQueue> queueMap = Maps.newHashMap();

    public RawEventBlockingQueue getQueueByIngestionId(String ingestionId) {
        return queueMap.get(ingestionId);
    }

    public void addRawEventQueue(String ingestionId, Long userId, Long tenantId) {
        if (queueMap.containsKey(ingestionId)) {
            return;
        }
        RawEventBlockingQueue eventQueue = new RawEventBlockingQueue(atEventConfig.getDefaultRawEventQueueSize());
        eventQueue.setUserId(userId);
        eventQueue.setTenantId(tenantId);
        queueMap.put(ingestionId, eventQueue);
    }
}
