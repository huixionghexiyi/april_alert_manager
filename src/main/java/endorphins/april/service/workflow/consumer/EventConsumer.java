package endorphins.april.service.workflow.consumer;

import java.util.List;
import java.util.concurrent.BlockingQueue;

import endorphins.april.entity.Workflow;
import endorphins.april.infrastructure.json.JsonUtils;
import endorphins.april.infrastructure.thread.ThreadPoolManager;
import endorphins.april.service.workflow.WorkflowEvent;
import endorphins.april.service.workflow.WorkflowExecutor;
import endorphins.april.service.workflow.queue.BlockingEventQueue;
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

    private BlockingEventQueue eventQueue;

    private List<Workflow> workflowList;

    private WorkflowExecutorContext workflowExecutorContext;

    public void run() {
        while (true) {
            try {
                WorkflowEvent workflowEvent = eventQueue.take();
                threadPoolManager.getEventConsumerThreadPool().execute(
                    new WorkflowExecutor(workflowEvent, workflowList, workflowExecutorContext)
                );
                log.info("event处理完成，event:{}", JsonUtils.toJSONString(workflowEvent));
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}
