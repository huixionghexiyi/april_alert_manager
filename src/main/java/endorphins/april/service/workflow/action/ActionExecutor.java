package endorphins.april.service.workflow.action;

import endorphins.april.service.workflow.WorkflowEvent;
import endorphins.april.service.workflow.consumer.WorkflowExecutorContext;

/**
 * @author timothy
 * @DateTime: 2023/8/30 17:19
 **/
public interface ActionExecutor {
    /**
     * action 执行器
     *
     * 用于将 event 处理成需要的样子，及生成 alarm
     *
     * @param context
     * @param event
     */
    void execute(WorkflowExecutorContext context, WorkflowEvent event);
}
