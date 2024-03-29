package endorphins.april.service.workflow;

import org.springframework.data.annotation.Transient;

import endorphins.april.service.workflow.event.WorkflowEvent;
import endorphins.april.service.workflow.rawevent.WorkflowRawEvent;
import lombok.Data;

/**
 * @author timothy
 * @DateTime: 2023/8/24 16:32
 **/
@Data
public class Action {

    private String name;

    private String params;

    @Transient
    private ActionExecutor executor;

    public Action(String name, String params) {
        this.name = name;
        this.params = params;
        this.executor = ActionParams.getExecutorByName(name, params);
    }

    public void execute(WorkflowExecutorContext context, WorkflowEvent workflowEvent) {
        executor.execute(context, workflowEvent);
    }

    public void execute(WorkflowExecutorContext context, WorkflowRawEvent workflowRawEvent) {
        executor.execute(context, workflowRawEvent);
    }
}
