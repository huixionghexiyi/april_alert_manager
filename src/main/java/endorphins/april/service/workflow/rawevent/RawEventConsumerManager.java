package endorphins.april.service.workflow.rawevent;

import com.google.common.collect.Lists;
import endorphins.april.entity.IngestionInstance;
import endorphins.april.entity.Workflow;
import endorphins.april.infrastructure.json.JsonUtils;
import endorphins.april.infrastructure.thread.ThreadPoolManager;
import endorphins.april.repository.AlarmRepository;
import endorphins.april.repository.WorkflowRepository;
import endorphins.april.service.workflow.*;
import endorphins.april.service.workflow.event.EventQueueManager;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author timothy
 * @DateTime: 2023/8/31 19:50
 **/
@Component
@AllArgsConstructor
@Slf4j
public class RawEventConsumerManager {

    private RawEventQueueManager rawEventQueueManager;

    private EventQueueManager eventQueueManager;

    private ThreadPoolManager threadPoolManager;

    private WorkflowRepository workflowRepository;

    private AlarmRepository alarmRepository;

    public void addAndRunConsumer(IngestionInstance instance) {
        List<Workflow> workflows = Lists.newArrayList();

        Workflow workflow = createWorkflowByIngestion(instance);
        workflows.add(workflow);

        workflowRepository.save(workflow);

        // 添加 rawEventQueue
        rawEventQueueManager.addRawEventQueue(instance, instance.getCreateUserId(), instance.getTenantId());

        // 添加到 consumerManager
        runConsumer(instance, workflows);
    }

    public void runConsumer(IngestionInstance instance, List<Workflow> workflows) {
        WorkflowExecutorContext executorContext = WorkflowExecutorContext.builder()
                .alarmRepository(alarmRepository)
                .workflowList(workflows)
                .eventQueue(eventQueueManager.getQueueByUserId(instance.getCreateUserId()))
                .build();
        RawEventConsumer consumer = RawEventConsumer.builder()
                .rawEventQueue(rawEventQueueManager.getQueueByIngestionId(instance.getId()))
                .threadPoolManager(threadPoolManager)
                .workflowExecutorContext(executorContext)
                .build();
        threadPoolManager.getRawEventConsumerThreadPool()
                .execute(consumer);
    }

    /**
     * 根据 ingestion instance 创建一个 workflow
     *
     * @param instance
     * @return
     */
    public Workflow createWorkflowByIngestion(IngestionInstance instance) {
        long now = System.currentTimeMillis();
        Trigger trigger = Trigger.builder()
                .type(TriggerType.RAW_EVENT_COLLECT)
                .build();
        List<Action> steps = Lists.newArrayList();
        Action action = new Action(RawEventMappingActionParams.name, JsonUtils.toJSONString(instance.getConfig()));
        steps.add(action);
        return Workflow.builder()
                .tags(Workflow.INGESTION_TAG)
                .type(WorkflowType.RAW_EVENT)
                .status(WorkflowStatus.RUNNING)
                .priority(1)
                .createTime(now)
                .updateTime(now)
                .ingestionId(instance.getId())
                .tenantId(instance.getTenantId())
                .createUserId(instance.getCreateUserId())
                .trigger(trigger)
                .steps(steps)
                .build();
    }
}
