package cn.endorphin.atevent.workflow.executor;

import cn.endorphin.atevent.infrastructure.json.JsonUtils;
import cn.endorphin.atevent.workflow.WorkflowExecutorContext;
import cn.endorphin.atevent.workflow.event.WorkflowEvent;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

/**
 * @author timothy
 * @DateTime: 2023/8/24 17:23
 **/
@Data
@Component(ClassifyActionExecutor.name)
public class ClassifyActionExecutor implements ActionExecutor<WorkflowExecutorContext, WorkflowEvent> {

    public static final String name = "classify";

    @Override
    public void execute(String actionParams, WorkflowExecutorContext context, WorkflowEvent workflowData) {
        ClassifyActionParams params = JsonUtils.parse(actionParams, ClassifyActionParams.class);
        // 如果有 这两个字段，就不再 进行分类
        if (StringUtils.isNotBlank(workflowData.getKind()) || StringUtils.isNotBlank(workflowData.getType())) {
            return;
        }
        if (params.getClassifyFields().contains("cpu")) {
            // TODO 类似这样的分类
            workflowData.setKind("Compute");
        }
    }
}
