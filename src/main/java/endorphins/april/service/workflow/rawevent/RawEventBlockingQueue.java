package endorphins.april.service.workflow.rawevent;

import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import endorphins.april.service.workflow.rawevent.WorkflowRawEvent;
import lombok.Data;

/**
 * @author timothy
 * @DateTime: 2023/10/20 14:29
 **/
@Data
public class RawEventBlockingQueue {
    private BlockingQueue<WorkflowRawEvent> instance;
    private Long userId;
    private Long tenantId;

    public RawEventBlockingQueue(int size) {
        instance = new ArrayBlockingQueue<>(size);
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

    public void offer(WorkflowRawEvent workflowEvent) {
        instance.offer(workflowEvent);
    }
}
