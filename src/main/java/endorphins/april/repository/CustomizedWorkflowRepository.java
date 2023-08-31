package endorphins.april.repository;

import java.util.List;

import endorphins.april.entity.Workflow;
import endorphins.april.service.workflow.trigger.TriggerType;

/**
 * @author timothy
 * @DateTime: 2023/8/31 15:31
 **/
public interface CustomizedWorkflowRepository {
    /**
     * 根据 触发类型查询 workflow
     * @param triggerType
     * @return
     */
    List<Workflow> findByTriggerTypeOrderByPriorityAsc(TriggerType triggerType);
}
