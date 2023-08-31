package endorphins.april.domain.workflow.action;

import endorphins.april.domain.workflow.WorkflowEvent;

/**
 * @author timothy
 * @DateTime: 2023/8/30 17:19
 **/
public interface ActionExecutor {
    String getName();

    void action(WorkflowEvent event);
}
