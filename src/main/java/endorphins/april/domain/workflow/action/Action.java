package endorphins.april.domain.workflow.action;

import org.springframework.data.annotation.Transient;

import endorphins.april.domain.workflow.WorkflowEvent;
import endorphins.april.domain.workflow.action.context.ActionContext;
import endorphins.april.infrastructure.json.JsonUtils;
import lombok.Data;

/**
 * @author timothy
 * @DateTime: 2023/8/24 16:32
 **/
@Data
public class Action {

    private String name;

    private String context;

    @Transient
    private ActionExecutor executor;

    public Action() {
    }

    public Action(String name, String context) {
        this.name = name;
        this.context = context;
    }
    public void initExecutor() {
        executor = ActionContext.getExecutorByName(name, context);
    }

    public void action(WorkflowEvent event) {
        executor.action(event);
    }
}
