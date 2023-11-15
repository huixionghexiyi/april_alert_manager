package cn.endorphin.atevent.workflow.event;

import cn.endorphin.atevent.entity.Workflow;
import cn.endorphin.atevent.workflow.Action;
import cn.endorphin.atevent.workflow.Condition;
import cn.endorphin.atevent.workflow.Trigger;
import cn.endorphin.atevent.workflow.WorkflowExecutorContext;
import cn.endorphin.atevent.workflow.executor.ActionExecutor;
import org.apache.commons.collections4.CollectionUtils;

import java.util.List;
import java.util.Map;

/**
 * @author timothy
 * @DateTime: 2023/8/24 19:02
 **/
public class EventWorkflowExecutor implements Runnable {

    private WorkflowEvent workflowEvent;

    private final WorkflowExecutorContext context;

    public EventWorkflowExecutor(WorkflowEvent workflowEvent, WorkflowExecutorContext context) {
        this.workflowEvent = workflowEvent;
        this.context = context;
    }

    @Override
    public void run() {
        for (Workflow workflow : context.getWorkflowList()) {
            // 如果满足触发器的条件，则继续完成 workflow
            if (isMatch(workflow.getTrigger())) {
                for (Action action : workflow.getSteps()) {
                    ActionExecutor executor = context.getActionExecutorManager().getByName(action.getName());
                    executor.execute(action.getParams(), context, workflowEvent);
                }
            }
        }
    }

    private boolean isMatch(Trigger trigger) {
        List<Condition> conditions = trigger.getConditions();
        boolean triggerWorkflow = true;
        Map<String, Object> tags = workflowEvent.getTags();
        // 如果没有term，则认为该  trigger 满足所有的条件
        if (CollectionUtils.isEmpty(conditions)) {
            return true;
        }
        for (Condition condition : conditions) {
            Object fieldValue;
            if ((fieldValue = workflowEvent.getByFieldName(condition.getKey())) == null) {
                fieldValue = tags.get(condition.getKey());
            }
            // 只要有一个条件不满足, 则跳过
            if (!condition.checkValue(fieldValue)) {
                triggerWorkflow = false;
                break;
            }
        }
        return triggerWorkflow;
    }
}