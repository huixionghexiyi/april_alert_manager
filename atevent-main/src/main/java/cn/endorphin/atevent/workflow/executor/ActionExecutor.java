package cn.endorphin.atevent.workflow.executor;

/**
 * @author timothy
 * @DateTime: 2023/8/30 17:19
 **/
public interface ActionExecutor<W extends WorkflowContext, D extends WorkflowData> {

    void execute(String actionParams, W context, D workflowData);
}
