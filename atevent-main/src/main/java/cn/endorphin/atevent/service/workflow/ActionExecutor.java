package cn.endorphin.atevent.service.workflow;

import org.apache.commons.lang3.NotImplementedException;

import cn.endorphin.atevent.service.workflow.event.WorkflowEvent;
import cn.endorphin.atevent.service.workflow.rawevent.WorkflowRawEvent;

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
     */
    default void execute(WorkflowExecutorContext context, WorkflowEvent event) {
        throw new NotImplementedException();

    };
    default void execute(WorkflowExecutorContext context, WorkflowRawEvent rawEvent) {
        throw new NotImplementedException();
    }

}
