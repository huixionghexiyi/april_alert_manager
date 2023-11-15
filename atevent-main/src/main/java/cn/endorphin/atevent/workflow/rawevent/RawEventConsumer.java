package cn.endorphin.atevent.workflow.rawevent;

import cn.endorphin.atevent.infrastructure.json.JsonUtils;
import cn.endorphin.atevent.infrastructure.thread.ThreadPoolManager;
import cn.endorphin.atevent.workflow.WorkflowExecutorContext;
import com.google.common.collect.Lists;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;

import java.util.List;

/**
 * @author timothy
 * @DateTime: 2023/8/24 17:32
 **/
@Slf4j
@Builder
public class RawEventConsumer implements Runnable {

    private String name;

    private int index;

    private ThreadPoolManager threadPoolManager;

    private RawEventBlockingQueue rawEventQueue;

    private WorkflowExecutorContext workflowExecutorContext;

    private boolean stop;

    public void stop() {
        this.stop = true;
    }

    public void run() {
        while (true) {
            try {
                List<WorkflowRawEvent> workflowEvents = Lists.newArrayList();
                rawEventQueue.drainTo(workflowEvents, 100);
                if (CollectionUtils.isNotEmpty(workflowEvents)) {
                    for (WorkflowRawEvent workflowEvent : workflowEvents) {
                        threadPoolManager.getWorkerThreadPool().execute(
                                new RawEventWorkflowExecutor(workflowEvent, workflowExecutorContext)
                        );
                    }
                    log.debug("raw event execute，raw events:{}", JsonUtils.toJSONString(workflowEvents));
                } else if (stop) {
                    break;
                }
                Thread.sleep(1000);
            } catch (Exception e) {
                log.error("raw event execute error", e);
                Thread.currentThread().interrupt();
            }
        }
    }
}
