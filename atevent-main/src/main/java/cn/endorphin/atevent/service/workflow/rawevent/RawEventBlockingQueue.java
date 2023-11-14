package cn.endorphin.atevent.service.workflow.rawevent;

import cn.endorphin.atevent.model.ingestion.IngestionDataScaleType;
import lombok.Data;

import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * @author timothy
 * @DateTime: 2023/10/20 14:29
 **/
@Data
public class RawEventBlockingQueue {
    private BlockingQueue<WorkflowRawEvent> instance;
    private Long userId;
    private Long tenantId;
    private IngestionDataScaleType scaleType;

    public RawEventBlockingQueue(int size) {
        instance = new LinkedBlockingDeque<>(size);
    }

    public void addAll(List<WorkflowRawEvent> workflowRawEvents) {
        instance.addAll(workflowRawEvents);
    }

    public void add(WorkflowRawEvent workflowRawEvent) {
        instance.add(workflowRawEvent);
    }

    public void put(WorkflowRawEvent workflowRawEvent) throws InterruptedException {
        instance.put(workflowRawEvent);
    }

    public WorkflowRawEvent take() throws InterruptedException {
        return instance.take();
    }

    public void drainTo(List<WorkflowRawEvent> workflowRawEventList, int maxElements) {
        instance.drainTo(workflowRawEventList, maxElements);
    }

    public void offer(WorkflowRawEvent workflowEvent) {
        instance.offer(workflowEvent);
    }
}
