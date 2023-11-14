package cn.endorphin.atevent.service.workflow.event;

import java.util.List;
import java.util.Map;

import cn.endorphin.atevent.entity.Workflow;
import cn.endorphin.atevent.service.workflow.WorkflowExecutorContext;
import cn.endorphin.atevent.service.workflow.Action;
import cn.endorphin.atevent.service.workflow.Term;
import cn.endorphin.atevent.service.workflow.Trigger;
import org.apache.commons.collections4.CollectionUtils;

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
                    action.execute(context, workflowEvent);
                }
            }
        }
    }

    private boolean isMatch(Trigger trigger) {
        List<Term> terms = trigger.getTerms();
        boolean triggerWorkflow = true;
        Map<String, Object> tags = workflowEvent.getTags();
        // 如果没有term，则认为该  trigger 满足所有的条件
        if (CollectionUtils.isEmpty(terms)) {
            return true;
        }
        for (Term term : terms) {
            Object fieldValue;
            if ((fieldValue = workflowEvent.getByFieldName(term.getKey())) == null) {
                fieldValue = tags.get(term.getKey());
            }
            // 只要有一个条件不满足, 则跳过
            if (!term.checkValue(fieldValue)) {
                triggerWorkflow = false;
                break;
            }
        }
        return triggerWorkflow;
    }
}