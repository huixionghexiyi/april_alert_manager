package cn.endorphin.atevent.service.workflow.event;

import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

import lombok.Data;

/**
 * @author timothy
 * @DateTime: 2023/10/20 14:29
 **/
@Data
public class EventBlockingQueue {
    private BlockingQueue<WorkflowEvent> instance;
    private Long userId;
    private Long tenantId;

    public EventBlockingQueue(int size) {
        instance = new LinkedBlockingDeque<>(size);
    }

    public void addAll(List<WorkflowEvent> workflowEvents) {
        instance.addAll(workflowEvents);

    }

    public void add(WorkflowEvent workflowEvent) {
        instance.add(workflowEvent);
    }

    public void put(WorkflowEvent workflowEvent) throws InterruptedException {
        instance.put(workflowEvent);
    }

    public WorkflowEvent take() throws InterruptedException {
        return instance.take();
    }

    public void offer(WorkflowEvent workflowEvent) {
        instance.offer(workflowEvent);
    }
}
