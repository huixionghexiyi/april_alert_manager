package endorphins.april.service.workflow;

import java.util.List;

import endorphins.april.entity.Workflow;
import endorphins.april.repository.AlarmRepository;
import endorphins.april.service.workflow.event.EventBlockingQueue;
import endorphins.april.service.workflow.rawevent.RawEventBlockingQueue;
import lombok.Builder;
import lombok.Data;

/**
 * @author timothy
 * @DateTime: 2023/8/31 19:59
 **/
@Data
@Builder
public class WorkflowExecutorContext {
    private final AlarmRepository alarmRepository;
    private final EventBlockingQueue eventQueue;
    private final List<Workflow> workflowList;
}
