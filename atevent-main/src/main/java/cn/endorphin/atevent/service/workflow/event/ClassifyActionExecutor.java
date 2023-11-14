package cn.endorphin.atevent.service.workflow.event;

import cn.endorphin.atevent.service.workflow.ActionExecutor;
import cn.endorphin.atevent.service.workflow.WorkflowExecutorContext;
import org.apache.commons.lang3.StringUtils;

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
        // 如果有 这两个字段，就不再 进行分类
        if (StringUtils.isNotBlank(workflowEvent.getKind()) || StringUtils.isNotBlank(workflowEvent.getType())) {
            return;
        }
        if (params.getClassifyFields().contains("cpu")) {
            // TODO 类似这样的分类
            workflowEvent.setKind("Compute");
        }
    }
}
