package endorphins.april.service.workflow.rawevent;

import endorphins.april.entity.Workflow;
import endorphins.april.service.workflow.Action;
import endorphins.april.service.workflow.Trigger;
import endorphins.april.service.workflow.TriggerType;
import endorphins.april.service.workflow.WorkflowExecutorContext;
import endorphins.april.service.workflow.event.WorkflowEvent;

import java.util.List;
import java.util.Map;

/**
 * @author timothy
 * @DateTime: 2023/8/24 19:02
 **/
public class RawEventWorkflowExecutor implements Runnable {

    private final WorkflowRawEvent workflowRawEvent;

    private final WorkflowExecutorContext context;

    public RawEventWorkflowExecutor(WorkflowRawEvent workflowRawEvent, WorkflowExecutorContext context) {
        this.workflowRawEvent = workflowRawEvent;
        this.context = context;
    }

    @Override
    public void run() {
        List<Workflow> workflows = context.getIngestionWorkflowMap().get(workflowRawEvent.getIngestionId());
        for (Workflow workflow : workflows) {
            // 如果满足触发器的条件，则继续完成 workflow
            if (isMatch(workflow.getTrigger())) {
                for (Action action : workflow.getSteps()) {
                    action.execute(context, workflowRawEvent);
                }
            }
        }
        Map<String, Object> targetRawEvent = workflowRawEvent.getTargetRawEvent();
        // TODO 构建 workflowEvent
        WorkflowEvent workflowEvent = WorkflowEvent.builder()
                .check(targetRawEvent.getOrDefault("check", "").toString())
                .description(targetRawEvent.getOrDefault("description", "").toString())
                .kind(targetRawEvent.getOrDefault("kind", "").toString())
                .source(targetRawEvent.getOrDefault("source", "").toString())
                .service(targetRawEvent.getOrDefault("service", "").toString())
                .severity((Integer) targetRawEvent.getOrDefault("severity", 0))
                .deduplicationKey(targetRawEvent.getOrDefault("dedupe_key", "").toString())
                .build();
        if (targetRawEvent.containsKey("time")) {
            if (targetRawEvent.get("time") instanceof Long) {
                workflowEvent.setTime((Long) targetRawEvent.get("time"));
            }
        }

        context.getEventQueue().add(workflowEvent);
    }

    private boolean isMatch(Trigger trigger) {
        return (trigger.getType() == TriggerType.RAW_EVENT_COLLECT);
    }
}