package endorphins.april.service.workflow.rawevent;

import java.util.List;

import com.google.common.collect.Lists;
import endorphins.april.infrastructure.json.JsonUtils;
import endorphins.april.infrastructure.thread.ThreadPoolManager;
import endorphins.april.service.workflow.WorkflowExecutorContext;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;

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
                List<WorkflowRawEvent> workflowEvents = Lists.newArrayList();
                rawEventQueue.drainTo(workflowEvents, 100);
                if(CollectionUtils.isNotEmpty(workflowEvents)) {
                    for (WorkflowRawEvent workflowEvent : workflowEvents) {
                        threadPoolManager.getWorkerThreadPool().execute(
                                new RawEventWorkflowExecutor(workflowEvent, workflowExecutorContext)
                        );
                    }
                    log.debug("raw event execute，raw events:{}", JsonUtils.toJSONString(workflowEvents));
                } else {
                    Thread.sleep(1000);
                }
            } catch (Exception e) {
                log.error("raw event execute error", e);
                Thread.currentThread().interrupt();
            }
        }
    }
}
