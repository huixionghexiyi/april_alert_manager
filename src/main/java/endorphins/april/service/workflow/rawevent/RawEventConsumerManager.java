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
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author timothy
 * @DateTime: 2023/8/31 19:50
 **/
@Component
@AllArgsConstructor
@Slf4j
@Order(2)
public class RawEventConsumerManager implements ApplicationRunner {

    private RawEventQueueManager rawEventQueueManager;

    private EventQueueManager eventQueueManager;

    private ThreadPoolManager threadPoolManager;

    private WorkflowRepository workflowRepository;

    private AlarmRepository alarmRepository;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        List<Workflow> allWorkflow =
                workflowRepository.findByTriggerTypeOrderByPriorityAsc(TriggerType.RAW_EVENT_COLLECT);
        if (CollectionUtils.isNotEmpty(allWorkflow)) {
            // 一个用户一个 workflow
            Map<String, List<Workflow>> workflowByIngestionId = allWorkflow.stream()
                    .collect(Collectors.groupingBy(Workflow::getIngestionId, LinkedHashMap::new, Collectors.toList()));
            workflowByIngestionId.forEach(
                    (ingestionId, workflows) -> {
                        WorkflowExecutorContext executorContext = WorkflowExecutorContext.builder()
                                .workflowList(workflows)
                                .alarmRepository(alarmRepository)
                                .eventQueue(eventQueueManager.getQueueByUserId(workflows.get(0).getCreateUserId()))
                                .build();
                        RawEventConsumer consumer = RawEventConsumer.builder()
                                .rawEventQueue(rawEventQueueManager.getQueueByIngestionId(ingestionId))
                                .threadPoolManager(threadPoolManager)
                                .workflowExecutorContext(executorContext)
                                .build();
                        threadPoolManager.getRawEventConsumerThreadPool().execute(consumer);
                    }
            );
        } else {
            log.warn("not find any workflow");
        }

    }

    public void addConsumer(IngestionInstance instance) {
        List<Workflow> workflows = Lists.newArrayList();
        long now = System.currentTimeMillis();

        Trigger trigger = Trigger.builder()
                .type(TriggerType.RAW_EVENT_COLLECT)
                .build();
        List<Action> steps = Lists.newArrayList();
        Action action = new Action();
        action.setName(RawEventMappingActionParams.name);
        action.setParams(JsonUtils.toJSONString(instance.getConfig()));
        steps.add(action);
        Workflow workflow = Workflow.builder()
                .createUserId(instance.getCreateUserId())
                .tags(Workflow.INGESTION_TAG)
                .type(WorkflowType.RAW_EVENT)
                .status(WorkflowStatus.RUNNING)
                .priority(1)
                .createTime(now)
                .updateTime(now)
                .ingestionId(instance.getId())
                .tenantId(instance.getTenantId())
                .trigger(trigger)
                .steps(steps)
                .build();
        workflows.add(workflow);

        workflowRepository.save(workflow);

        // 添加到 consumerManager
        threadPoolManager.getRawEventConsumerThreadPool()
                .execute(
                        RawEventConsumer.builder()
                                .rawEventQueue(rawEventQueueManager.getQueueByIngestionId(instance.getId()))
                                .threadPoolManager(threadPoolManager)
                                .workflowExecutorContext(
                                        WorkflowExecutorContext.builder()
                                                .alarmRepository(alarmRepository)
                                                .workflowList(workflows)
                                                .build()
                                )
                                .build()
                );
    }
}
