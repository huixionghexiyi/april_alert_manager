package endorphins.april.service.workflow;

import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;

import endorphins.april.entity.Workflow;
import endorphins.april.service.workflow.action.Action;
import endorphins.april.service.workflow.consumer.WorkflowExecutorContext;
import endorphins.april.service.workflow.trigger.Trigger;

/**
 * @author timothy
 * @DateTime: 2023/8/24 19:02
 **/
public class WorkflowExecutor implements Runnable {

    private WorkflowEvent workflowEvent;

    private List<Workflow> workflowList;

    private WorkflowExecutorContext context;

    public WorkflowExecutor(WorkflowEvent workflowEvent, List<Workflow> workflowList, WorkflowExecutorContext context) {
        this.workflowEvent = workflowEvent;
        this.workflowList = workflowList;
        this.context = context;
    }

    @Override
    public void run() {
        for (Workflow workflow : workflowList) {
            // 如果满足触发器的条件，则继续完成 workflow
            if (isMatch(workflow.getTrigger())) {
                for (Action action : workflow.getSteps()) {
                    action.action(context, workflowEvent);
                }
            }
        }
    }

    private boolean isMatch(Trigger trigger) {
        List<Term> terms = trigger.getTerms();
        boolean triggerWorkflow = true;
        Map<String, Object> insideFieldsMap = workflowEvent.getInsideFieldsMap();
        Map<String, Object> tags = workflowEvent.getTags();
        // 如果没有term，则认为该  trigger 满足所有的条件
        if(CollectionUtils.isEmpty(terms)) {
            return true;
        }
        for (Term term : terms) {
            Object fieldValue;
            if (insideFieldsMap.containsKey(term.getKey())) {
                fieldValue = insideFieldsMap.get(term.getKey());
            } else {
                fieldValue = tags.get(term.getKey());
            }
            // 如果不满足条件, 则跳过
            if (!term.checkValue(fieldValue)) {
                triggerWorkflow = false;
                break;
            }
        }
        return triggerWorkflow;
    }
}