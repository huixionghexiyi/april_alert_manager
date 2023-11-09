package endorphins.april.service.workflow.event;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

import endorphins.april.model.Event;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import com.google.common.collect.Lists;

/**
 * @author timothy
 * @DateTime: 2023/8/24 23:18
 **/
@Getter
@Builder
@Slf4j
public class WorkflowEvent {
    private String description;
    private Integer severity;
    private String source;
    private String check;

    private String service;
    @Setter
    private String kind;
    private String type;

    @Setter
    private Long time;
    private String manager;
    private String managerId;
    /**
     * 去重字段
     */
    private String deduplicationKey;

    private Long tenantId;

    private Long userId;

    private Map<String, Object> tags;
    /**
     * 接收到 event 时的时间，如果 原始 event 中没有 time 字段，则该字段作为 eventTime
     */
    @Setter
    private long receivedTime;

    public static WorkflowEvent createByEvent(Event event) {
        Map<String, Object> tags = event.getTags();
        long now = System.currentTimeMillis();
        WorkflowEvent workflowEvent = WorkflowEvent.builder()
            .check(event.getCheck())
            .kind(event.getKind())
            .type(event.getType())
            .source(event.getSource())
            .description(event.getDescription())
            .severity(event.getSeverity())
            .service(event.getService())
            .tags(tags)
            .build();
        workflowEvent.setReceivedTime(now);
        return workflowEvent;
    }

    public static List<WorkflowEvent> createByEvent(List<Event> events) {
        List<WorkflowEvent> result = Lists.newArrayList();
        for (Event event : events) {
            result.add(createByEvent(event));
        }
        return result;
    }

    public Object getByFieldName(String dedupeField) {
        // TODO 增加支持从tags 中获取去重字段的逻辑
        Object result = null;
        String methodName = null;
        try {
            methodName = capitalize(dedupeField);
            Method method = this.getClass().getMethod(methodName);
            result = method.invoke(this);
        } catch (Exception e) {
            log.error("Not found method:{}", methodName);
        }
        return result;
    }

    private String capitalize(String fieldName) {
        return "get" + Character.toUpperCase(fieldName.charAt(0)) + fieldName.substring(1);
    }
}
