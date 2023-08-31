package endorphins.april.service.workflow.consumer;

import endorphins.april.repository.AlarmRepository;
import endorphins.april.repository.WorkflowRepository;
import lombok.Builder;
import lombok.Data;

/**
 * @author timothy
 * @DateTime: 2023/8/31 19:59
 **/
@Data
@Builder
public class WorkflowExecutorContext {
    private AlarmRepository alarmRepository;
}
