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
import endorphins.april.service.workflow.event.JoinerHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

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

    public static final String consumerNamePrefix = "raw-event-consumer-";


    private final Map<String, List<Workflow>> ingestionWorkflowMap = Maps.newConcurrentMap();

    private RawEventConsumer basicWorkflowConsumer;
    private Map<String, RawEventConsumer> consumerAuditMap = Maps.newConcurrentMap();

    private AtomicInteger consumerCount = new AtomicInteger(0);

    public void startConsumer(IngestionInstance instance) {

        Workflow workflow = createWorkflowByIngestion(instance);

        // 添加 rawEventQueue
        rawEventQueueManager.addRawEventQueue(instance, instance.getCreateUserId(), instance.getTenantId());

        // 添加到 consumerManager
        runConsumer(instance, Lists.newArrayList(workflow));
    }

    public void stopConsumer(IngestionInstance ingestionInstance) {
        if (ingestionInstance.isSingleConsumerScale()) {
            String consumerNameByIngestion = getConsumerNameByIngestion(ingestionInstance);
            if (consumerAuditMap.containsKey(consumerNameByIngestion)) {
                RawEventConsumer rawEventConsumer = consumerAuditMap.get(consumerNameByIngestion);
                rawEventConsumer.stop();
            }
        } else if (ingestionInstance.isMultiConsumerScale()) {
            // 多消费者模式
        }
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

    /**
     * basic consumer
     * 只有一个消费者
     * @param instance
     * @param workflowLists
     */
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
                basicWorkflowConsumer = getConsumer("basic", rawEventQueueManager.getBasicRawEventQueue(), executorContext);
                threadPoolManager.getRawEventConsumerThreadPool().execute(basicWorkflowConsumer);
                consumerAuditMap.put("basic", basicWorkflowConsumer);
            }
        }
    }

    private RawEventConsumer getConsumer(String name, RawEventBlockingQueue rawEventQueueManager, WorkflowExecutorContext executorContext) {
        return RawEventConsumer.builder()
                .name(name)
                .index(consumerCount.incrementAndGet())
                .rawEventQueue(rawEventQueueManager)
                .threadPoolManager(threadPoolManager)
                .workflowExecutorContext(executorContext)
                .build();
    }

    private void runSingleConsumer(IngestionInstance instance, List<Workflow> workflows) {
        WorkflowExecutorContext executorContext = WorkflowExecutorContext.builder()
                .alarmRepository(alarmRepository)
                .workflowList(workflows)
                // 处理完成的 rawEvent 需要放到 eventQueue 中，所以需要获取 当前 ingestion 对应的 eventQueue
                .eventQueue(eventQueueManager.getQueueByUserId(instance.getCreateUserId()))
                .build();

        String consumerName = getConsumerNameByIngestion(instance);
        RawEventConsumer consumer = getConsumer(consumerName, rawEventQueueManager.getQueueByIngestionId(instance.getId()), executorContext);
        threadPoolManager.getRawEventConsumerThreadPool().execute(consumer);
        consumerAuditMap.put(consumerName, consumer);
    }

    private static String getConsumerNameByIngestion(IngestionInstance instance) {
        return JoinerHelper.CONN_JOINER.join(instance.getId(), instance.getName());
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
