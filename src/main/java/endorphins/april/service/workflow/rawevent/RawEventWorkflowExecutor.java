package endorphins.april.service.workflow.rawevent;

import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;

import endorphins.april.entity.Workflow;
import endorphins.april.service.workflow.Term;
import endorphins.april.service.workflow.TriggerType;
import endorphins.april.service.workflow.WorkflowExecutorContext;
import endorphins.april.service.workflow.Action;
import endorphins.april.service.workflow.Trigger;

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
        for (Workflow workflow : context.getWorkflowList()) {
            // 如果满足触发器的条件，则继续完成 workflow
            if (isMatch(workflow.getTrigger())) {
                for (Action action : workflow.getSteps()) {
                    action.execute(context, workflowRawEvent);
                }
            }
        }
    }

    private boolean isMatch(Trigger trigger) {
        return (trigger.getType() == TriggerType.RAW_EVENT_COLLECT);
    }
}