package endorphins.april.domain.workflow.action;

import java.util.Map;

import endorphins.april.domain.workflow.WorkflowEvent;
import endorphins.april.domain.workflow.action.context.ClassifyEventActionActionContext;
import lombok.Data;

/**
 * @author timothy
 * @DateTime: 2023/8/24 17:23
 **/
@Data
public class ClassifyEventActionExecutor implements ActionExecutor {

    private static final String name = "classifyEventAction";

    private ClassifyEventActionActionContext context;

    public ClassifyEventActionExecutor(ClassifyEventActionActionContext context) {
        this.context = context;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void action(WorkflowEvent workflowEvent) {
        Map<String, Object> insideFieldsMap = workflowEvent.getInsideFieldsMap();
        // 如果有 这两个字段，就不再 进行分类
        if (insideFieldsMap.containsKey("class") || insideFieldsMap.containsKey("type")) {
            return;
        }
        if (context.getClassifyFields().contains("cpu")) {
            // TODO 类似这样的分类
            insideFieldsMap.put("class", "Compute");
        }
    }
}
