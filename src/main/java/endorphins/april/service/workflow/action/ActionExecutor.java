package endorphins.april.service.workflow.action;

import endorphins.april.service.workflow.WorkflowEvent;

/**
 * @author timothy
 * @DateTime: 2023/8/30 17:19
 **/
public interface ActionExecutor {
    void action(WorkflowEvent event);
}
