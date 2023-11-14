package cn.endorphin.atevent.repository;

import java.util.List;

import cn.endorphin.atevent.entity.Workflow;
import cn.endorphin.atevent.service.workflow.TriggerType;

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
