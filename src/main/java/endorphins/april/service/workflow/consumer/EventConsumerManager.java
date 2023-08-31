package endorphins.april.service.workflow.consumer;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import endorphins.april.entity.Workflow;
import endorphins.april.infrastructure.thread.ThreadPoolManager;
import endorphins.april.repository.AlarmRepository;
import endorphins.april.repository.ApiKeyRepository;
import endorphins.april.repository.WorkflowRepository;
import endorphins.april.service.workflow.queue.EventQueueManager;
import endorphins.april.service.workflow.trigger.TriggerType;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * @author timothy
 * @DateTime: 2023/8/31 19:50
 **/
@Component
@AllArgsConstructor
@Slf4j
public class EventConsumerManager implements ApplicationRunner {
    private EventQueueManager eventQueueManager;

    private ThreadPoolManager threadPoolManager;

    private ApiKeyRepository apiKeyRepository;

    private WorkflowRepository workflowRepository;

    private AlarmRepository alarmRepository;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        // 根据 tenant 区分 workflow
        List<Workflow> allWorkflow = workflowRepository.findByTriggerTypeOrderByPriorityAsc(TriggerType.EVENT_CREATED);
        if (CollectionUtils.isNotEmpty(allWorkflow)) {
            Map<Long, List<Workflow>> workflowByTenant = allWorkflow.stream()
                .collect(Collectors.groupingBy(Workflow::getTenantId));
            workflowByTenant.forEach(
                (tenantId, workflows) -> {
                    threadPoolManager.getEventConsumerThreadPool()
                        .execute(EventConsumer.builder()
                            .eventQueue(eventQueueManager.getQueueByTenantId(tenantId))
                            .threadPoolManager(threadPoolManager)
                            .workflowList(workflows)
                            .workflowExecutorContext(
                                WorkflowExecutorContext.builder()
                                    .alarmRepository(alarmRepository)
                                    .build())
                            .build());
                }
            );
        } else {
            log.error("not find any workflow");
        }

    }
}
