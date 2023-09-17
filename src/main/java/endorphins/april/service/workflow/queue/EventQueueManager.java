package endorphins.april.service.workflow.queue;

import java.util.Map;
import java.util.concurrent.BlockingQueue;

import org.springframework.context.annotation.Configuration;

import endorphins.april.service.workflow.WorkflowEvent;

import com.google.common.collect.Maps;

/**
 * @author timothy
 * @DateTime: 2023/8/24 15:40
 **/
@Configuration
public class EventQueueManager {
    private final Map<Long, BlockingQueue<WorkflowEvent>> queueMap = Maps.newConcurrentMap();

    public BlockingQueue<WorkflowEvent> getQueueByTenantId(Long tenantId) {
        return queueMap.get(tenantId);
    }

    public Map<Long, BlockingQueue<WorkflowEvent>> getQueueMap() {
        return queueMap;
    }
}
