package endorphins.april.service.workflow.event;

import endorphins.april.entity.Workflow;
import endorphins.april.infrastructure.thread.ThreadPoolManager;
import endorphins.april.repository.AlarmRepository;
import endorphins.april.repository.WorkflowRepository;
import endorphins.april.service.workflow.TriggerType;
import endorphins.april.service.workflow.WorkflowExecutorContext;
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
@Order(1)
public class EventConsumerManager implements ApplicationRunner {
    private EventQueueManager eventQueueManager;

    private ThreadPoolManager threadPoolManager;

    private WorkflowRepository workflowRepository;

    private AlarmRepository alarmRepository;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        // 根据 tenant 区分 workflow
        List<Workflow> allWorkflow = workflowRepository.findByTriggerTypeOrderByPriorityAsc(TriggerType.EVENT_CREATED);
        if (CollectionUtils.isNotEmpty(allWorkflow)) {
            // 一个用户一个 workflow
            // TODO 根据 priority 排序
            Map<Long, List<Workflow>> workflowByUserId = allWorkflow.stream()
                    .collect(Collectors.groupingBy(Workflow::getCreateUserId, LinkedHashMap::new, Collectors.toList()));
            workflowByUserId.forEach(
                    (createUserId, workflows) -> {
                        WorkflowExecutorContext executorContext = WorkflowExecutorContext.builder()
                                .workflowList(workflows)
                                .alarmRepository(alarmRepository)
                                .build();
                        EventConsumer eventConsumer = EventConsumer.builder()
                                .eventQueue(eventQueueManager.getQueueByUserId(createUserId))
                                .threadPoolManager(threadPoolManager)
                                .workflowExecutorContext(executorContext)
                                .build();
                        threadPoolManager.getEventConsumerThreadPool().execute(eventConsumer);
                    }
            );
        } else {
            log.error("not find any workflow");
        }

    }
}
