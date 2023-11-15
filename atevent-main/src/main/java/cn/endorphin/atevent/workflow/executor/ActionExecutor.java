package cn.endorphin.atevent.workflow.executor;

/**
 * 定义了 action的执行接口
 * 每一种 action 都需要有一个 executor
 * @author timothy
 * @DateTime: 2023/8/30 17:19
 **/
public interface ActionExecutor<C, D extends WorkflowData> {

    void execute(Object actionParams, C context, D workflowData);
}
