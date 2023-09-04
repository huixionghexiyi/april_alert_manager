package endorphins.april.service.workflow;

import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;

import endorphins.april.entity.Event;
import endorphins.april.service.event.EventHelper;
import lombok.Data;

import com.google.common.collect.Lists;

/**
 * @author timothy
 * @DateTime: 2023/8/24 23:18
 **/
@Data
public class WorkflowEvent {

    /**
     * 内部定义的字段
     */
    private Map<String, Object> insideFieldsMap;

    private Map<String, Object> tags;

    private Long tenantId;

    private Long userId;

    private long eventTime;

    public WorkflowEvent(Map<String, Object> insideFieldsMap, Map<String, Object> tags) {
        this.insideFieldsMap = insideFieldsMap;
        this.tags = tags;
    }

    public static WorkflowEvent createByEvent(Event event, Long tenantId, Long userId) {
        Map<String, Object> insideFieldsMap = EventHelper.getInsiderFieldsMap(event);
        Map<String, Object> tags = event.getTags();
        WorkflowEvent workflowEvent = new WorkflowEvent(insideFieldsMap, tags);
        workflowEvent.setTenantId(tenantId);
        workflowEvent.setUserId(userId);
        return workflowEvent;
    }

    public static List<WorkflowEvent> createByEvent(List<Event> events, Long tenantId, Long userId) {
        List<WorkflowEvent> result = Lists.newArrayList();
        for (Event event : events) {
            Map<String, Object> insideFieldsMap = EventHelper.getInsiderFieldsMap(event);
            Map<String, Object> tags = event.getTags();
            WorkflowEvent workflowEvent = new WorkflowEvent(insideFieldsMap, tags);
            workflowEvent.setTenantId(tenantId);
            workflowEvent.setUserId(userId);
            result.add(workflowEvent);
        }

        return result;
    }
}
