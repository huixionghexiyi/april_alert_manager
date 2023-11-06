package endorphins.april.service.workflow.rawevent;

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
public class RawEventConsumer implements Runnable {

    private ThreadPoolManager threadPoolManager;

    private RawEventBlockingQueue rawEventQueue;

    private WorkflowExecutorContext workflowExecutorContext;

    public void run() {
        while (true) {
            try {
                WorkflowRawEvent workflowEvent = rawEventQueue.take();
                threadPoolManager.getEventConsumerThreadPool().execute(
                    new RawEventWorkflowExecutor(workflowEvent, workflowExecutorContext)
                );
                log.info("raw event execute，raw event:{}", JsonUtils.toJSONString(workflowEvent));
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}
