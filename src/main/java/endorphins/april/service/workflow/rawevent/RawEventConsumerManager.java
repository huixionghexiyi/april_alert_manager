package endorphins.april.service.workflow.rawevent;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import endorphins.april.entity.IngestionInstance;
import endorphins.april.entity.Workflow;
import endorphins.april.infrastructure.json.JsonUtils;
import endorphins.april.infrastructure.thread.ThreadPoolManager;
import endorphins.april.repository.AlarmRepository;
import endorphins.april.repository.WorkflowRepository;
import endorphins.april.service.workflow.*;
import endorphins.april.service.workflow.event.EventQueueManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * @author timothy
 * @DateTime: 2023/8/31 19:50
 **/
@Component
@Slf4j
public class RawEventConsumerManager {

    @Autowired
    private RawEventQueueManager rawEventQueueManager;
    @Autowired
    private EventQueueManager eventQueueManager;
    @Autowired
    private ThreadPoolManager threadPoolManager;
    @Autowired
    private WorkflowRepository workflowRepository;
    @Autowired
    private AlarmRepository alarmRepository;

    private final Map<String, List<Workflow>> ingestionWorkflowMap = Maps.newConcurrentMap();

    private RawEventConsumer basicWorkflowConsumer;

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

    public void runConsumer(IngestionInstance instance, List<Workflow> workflowLists) {
        if (instance.isBasicScale()) {
            runBasicConsumer(instance, workflowLists);
        } else if (instance.isSingleConsumerScale()) {
            runSingleConsumer(instance, workflowLists);
        } else {
            runMultiConsumer(instance, workflowLists);
        }
    }

    private void runBasicConsumer(IngestionInstance instance, List<Workflow> workflowLists) {
        ingestionWorkflowMap.put(instance.getId(), workflowLists);
        if (basicWorkflowConsumer == null) {
            synchronized (this) {
                WorkflowExecutorContext executorContext = WorkflowExecutorContext.builder()
                        .alarmRepository(alarmRepository)
                        .ingestionWorkflowMap(ingestionWorkflowMap)
                        // 处理完成的 rawEvent 需要放到 eventQueue 中
                        .eventQueue(eventQueueManager.getQueueByUserId(instance.getCreateUserId()))
                        .build();
                basicWorkflowConsumer = RawEventConsumer.builder()
                        .rawEventQueue(rawEventQueueManager.getBasicRawEventQueue())
                        .threadPoolManager(threadPoolManager)
                        .workflowExecutorContext(executorContext)
                        .build();
                threadPoolManager.getRawEventConsumerThreadPool().execute(basicWorkflowConsumer);
            }
        }
    }

    private void runSingleConsumer(IngestionInstance instance, List<Workflow> workflows) {
        WorkflowExecutorContext executorContext = WorkflowExecutorContext.builder()
                .alarmRepository(alarmRepository)
                .workflowList(workflows)
                // 处理完成的 rawEvent 需要放到 eventQueue 中
                .eventQueue(eventQueueManager.getQueueByUserId(instance.getCreateUserId()))
                .build();
        RawEventConsumer consumer = RawEventConsumer.builder()
                .rawEventQueue(rawEventQueueManager.getQueueByIngestionId(instance.getId()))
                .threadPoolManager(threadPoolManager)
                .workflowExecutorContext(executorContext)
                .build();
        threadPoolManager.getRawEventConsumerThreadPool().execute(consumer);
    }

    private void runMultiConsumer(IngestionInstance instance, List<Workflow> workflowLists) {
        // TODO
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
