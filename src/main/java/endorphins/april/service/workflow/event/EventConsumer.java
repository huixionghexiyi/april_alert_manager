package endorphins.april.service.workflow.event;

import java.util.List;

import endorphins.april.entity.Workflow;
import endorphins.april.infrastructure.json.JsonUtils;
import endorphins.april.infrastructure.thread.ThreadPoolManager;
import endorphins.april.service.workflow.WorkflowExecutorContext;
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
                threadPoolManager.getEventConsumerThreadPool().execute(
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
