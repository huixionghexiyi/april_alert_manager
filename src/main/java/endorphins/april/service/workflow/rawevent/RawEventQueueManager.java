package endorphins.april.service.workflow.rawevent;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import endorphins.april.config.AtEventConfig;
import endorphins.april.entity.IngestionInstance;
import endorphins.april.model.ingestion.IngestionDataScaleType;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Set;

/**
 * @author timothy
 * @DateTime: 2023/8/24 15:40
 **/
@Component
@Data
@Slf4j
public class RawEventQueueManager {

    @Autowired
    private AtEventConfig atEventConfig;

    private final Map<String, RawEventBlockingQueue> queueMap = Maps.newHashMap();

    private RawEventBlockingQueue basicRawEventQueue = null;

    private final Object basicLock = new Object();

    Set<String> basicIngestionIds = Sets.newConcurrentHashSet();

    public RawEventBlockingQueue getQueueByIngestionId(String ingestionId) {
        if (basicIngestionIds.contains(ingestionId)) {
            return getBasicRawEventQueue();
        }
        return queueMap.get(ingestionId);
    }

    public void addRawEventQueue(IngestionInstance instance, Long userId, Long tenantId) {
        if (instance == null) {
            log.debug("the ingestion instance is null,userId:{},tenantId:{}", userId, tenantId);
            return;
        }
        if (instance.getScaleType() == null || instance.getScaleType() == IngestionDataScaleType.BASIC) {
            basicIngestionIds.add(instance.getId());
            getBasicRawEventQueue();
            return;
        }

        String ingestionId = instance.getId();
        if (queueMap.containsKey(ingestionId)) {
            return;
        }
        RawEventBlockingQueue eventQueue = new RawEventBlockingQueue(atEventConfig.getDefaultRawEventQueueSize());
        eventQueue.setUserId(userId);
        eventQueue.setTenantId(tenantId);
        queueMap.put(ingestionId, eventQueue);
    }

    private RawEventBlockingQueue getBasicRawEventQueue() {
        if (basicRawEventQueue == null) {
            synchronized (basicLock) {
                if (basicRawEventQueue == null) {
                    basicRawEventQueue = new RawEventBlockingQueue(atEventConfig.getDefaultRawEventQueueSize());
                }
            }
        }
        return basicRawEventQueue;
    }
}
