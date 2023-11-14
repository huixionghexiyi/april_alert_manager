package cn.endorphin.atevent.service.workflow;

import java.util.List;
import java.util.Map;

import cn.endorphin.atevent.entity.Workflow;
import cn.endorphin.atevent.repository.AlarmRepository;
import cn.endorphin.atevent.service.workflow.event.EventBlockingQueue;
import lombok.Builder;
import lombok.Data;

/**
 * @author timothy
 * @DateTime: 2023/8/31 19:59
 **/
@Data
@Builder
public class WorkflowExecutorContext {
    private final AlarmRepository alarmRepository;
    private final EventBlockingQueue eventQueue;
    private final List<Workflow> workflowList;
    /**
     * ingestion 相关的工作流存储在这里
     */
    private final Map<String, List<Workflow>> ingestionWorkflowMap;
}
