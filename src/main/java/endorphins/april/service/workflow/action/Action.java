package endorphins.april.service.workflow.action;

import org.springframework.data.annotation.Transient;

import endorphins.april.service.workflow.WorkflowEvent;
import endorphins.april.service.workflow.action.params.ActionParams;
import endorphins.april.service.workflow.consumer.WorkflowExecutorContext;
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

    public Action() {
    }

    public Action(String name, String params) {
        this.name = name;
        this.params = params;
    }
    public void initExecutor() {
        executor = ActionParams.getExecutorByName(name, params);
    }

    public void executor(WorkflowExecutorContext context, WorkflowEvent event) {
        executor.execute(context, event);
    }
}
