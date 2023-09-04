package endorphins.april.service.workflow.action;

import java.util.Map;

import endorphins.april.service.workflow.WorkflowEvent;
import endorphins.april.service.workflow.action.params.ClassifyActionParams;
import endorphins.april.service.workflow.consumer.WorkflowExecutorContext;
import lombok.Data;

/**
 * @author timothy
 * @DateTime: 2023/8/24 17:23
 **/
@Data
public class ClassifyActionExecutor implements ActionExecutor {


    private ClassifyActionParams params;

    public ClassifyActionExecutor(ClassifyActionParams params) {
        this.params = params;
    }


    @Override
    public void execute(WorkflowExecutorContext context, WorkflowEvent workflowEvent) {
        Map<String, Object> insideFieldsMap = workflowEvent.getInsideFieldsMap();
        // 如果有 这两个字段，就不再 进行分类
        if (insideFieldsMap.containsKey("class") || insideFieldsMap.containsKey("type")) {
            return;
        }
        if (params.getClassifyFields().contains("cpu")) {
            // TODO 类似这样的分类
            insideFieldsMap.put("class", "Compute");
        }
    }
}
