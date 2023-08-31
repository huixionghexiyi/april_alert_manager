package endorphins.april.service.workflow.action;

import endorphins.april.service.workflow.WorkflowEvent;
import endorphins.april.service.workflow.action.context.DeduplicationEventActionActionContext;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author timothy
 * @DateTime: 2023/8/24 17:23
 **/
@Data
@NoArgsConstructor
public class DeduplicationEventExecutor implements ActionExecutor {

    private DeduplicationEventActionActionContext context;

    public DeduplicationEventExecutor(DeduplicationEventActionActionContext context) {
        this.context = context;
    }

    @Override
    public void action(WorkflowEvent workflowEvent) {
        // 根据去重字段查询历史 alarm

        // 判断是否超时等操作
        // 根据字段聚合规则，设置聚合字段的值
        // 重新保存 alarm
    }
}
