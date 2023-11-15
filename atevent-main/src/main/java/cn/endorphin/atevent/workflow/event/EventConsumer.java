package cn.endorphin.atevent.workflow.event;

import java.util.List;

import cn.endorphin.atevent.entity.Workflow;
import cn.endorphin.atevent.infrastructure.json.JsonUtils;
import cn.endorphin.atevent.infrastructure.thread.ThreadPoolManager;
import cn.endorphin.atevent.workflow.WorkflowExecutorContext;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;

/**
 * @author timothy
 * @DateTime: 2023/8/24 17:32
 **/
@Slf4j
@Builder
public class EventConsumer implements Runnable {

    private ThreadPoolManager threadPoolManager;

    private EventBlockingQueue eventQueue;

    private List<Workflow> workflowList;

    private WorkflowExecutorContext workflowExecutorContext;

    public void run() {
        while (true) {
            try {
                WorkflowEvent workflowEvent = eventQueue.take();
                threadPoolManager.getWorkerThreadPool().execute(
                    new EventWorkflowExecutor(workflowEvent, workflowExecutorContext)
                );
                log.info("event处理完成，event:{}", JsonUtils.toJSONString(workflowEvent));
            } catch (InterruptedException e) {
                log.error(e.getMessage(), e);
                Thread.currentThread().interrupt();
            }
        }
    }
}
