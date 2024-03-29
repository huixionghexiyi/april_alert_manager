package endorphins.april.init;

import com.google.common.collect.Lists;
import endorphins.april.config.AtEventConfig;
import endorphins.april.entity.ApiKey;
import endorphins.april.entity.IngestionInstance;
import endorphins.april.entity.Workflow;
import endorphins.april.infrastructure.json.JsonUtils;
import endorphins.april.infrastructure.thread.ThreadPoolManager;
import endorphins.april.model.ingestion.IngestionInstanceStatus;
import endorphins.april.repository.AlarmRepository;
import endorphins.april.repository.ApiKeyRepository;
import endorphins.april.repository.IngestionInstanceRepository;
import endorphins.april.repository.WorkflowRepository;
import endorphins.april.service.workflow.*;
import endorphins.april.service.workflow.event.ClassifyActionParams;
import endorphins.april.service.workflow.event.DeduplicationActionParams;
import endorphins.april.service.workflow.event.EventConsumer;
import endorphins.april.service.workflow.event.EventQueueManager;
import endorphins.april.service.workflow.rawevent.RawEventConsumer;
import endorphins.april.service.workflow.rawevent.RawEventConsumerManager;
import endorphins.april.service.workflow.rawevent.RawEventQueueManager;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author timothy
 * @DateTime: 2023/8/29 15:19
 **/
@AllArgsConstructor
@Configuration
@Order(1)
@Slf4j
public class AtEventRunner implements ApplicationRunner {

    private AtEventConfig atEventConfig;

    private WorkflowRepository workflowRepository;

    private ApiKeyRepository apiKeyRepository;

    private EventQueueManager eventQueueManager;

    private AlarmRepository alarmRepository;

    private ThreadPoolManager threadPoolManager;

    private RawEventQueueManager rawEventQueueManager;

    private RawEventConsumerManager rawEventConsumerManager;

    private IngestionInstanceRepository ingestionInstanceRepository;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        /**
         * 初始化 服务需要的基本数据
         */
        initApiKeyData();

        /**
         *  为每个用户增加一个默认的 workflow
         *  如果已经存在，则不做操作
         */
        initWorkflowData();

        /**
         * 初始化 event queue
         */
        initEventQueue();

        /**
         *  初始化 event consumer
         */
        initEventConsumer();

        initRawEventQueueAndConsumer();

//        initRawEventConsumer();
    }


    private void initEventConsumer() {
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

    private void initEventQueue() {
        Iterable<ApiKey> allApiKey = apiKeyRepository.findAll(); // 这个要改成查用户，一个用户一个 consumer queue
        for (ApiKey apiKey : allApiKey) {
            Long tenantId = apiKey.getTenantId();
            Long userId = apiKey.getCreateUserId();
            String apiKeyId = apiKey.getId();
            eventQueueManager.addEventQueue(apiKeyId, userId, tenantId);
        }
    }

    private void initApiKeyData() {
        // TODO 默认用户临时增加，后续该代码需要删除
        String defaultEventKey = "defaultEventKey";
        List<ApiKey> byName = apiKeyRepository.findByName(defaultEventKey);
        if (CollectionUtils.isNotEmpty(byName)) {
            return;
        }
        ApiKey entity = new ApiKey();
        entity.setDescription("default api key");
        entity.setName(defaultEventKey);
        entity.setCreateUserId(AtEventConfig.defaultTenantId);
        entity.setTenantId(AtEventConfig.defaultUserId);
        apiKeyRepository.save(entity);
    }

    private void initWorkflowData() {
        // TODO 需要对所有用户增加 workflow，这里暂时只对默认用户增加
        Optional<Workflow> workflow = workflowRepository.findByTags(Workflow.DEFAULT_TAG);
        if (workflow.isPresent()) {
            return;
        }

        long now = System.currentTimeMillis();
        // 创建 分类 workflow
        Trigger trigger = getDefaultEventTrigger();
        Workflow classify = Workflow.builder()
                .type(WorkflowType.EVENT)
                .createTime(now)
                .updateTime(now)
                .status(WorkflowStatus.RUNNING)
                .priority(0)
                .trigger(trigger)
                .tags(Workflow.DEFAULT_TAG)
                .tenantId(AtEventConfig.defaultTenantId)
                .createUserId(AtEventConfig.defaultUserId)
                .steps(getDefaultClassifySteps())
                .description("default classify event workflow")
                .build();

        // 创建 去重 workflow，放到最后执行
        Workflow deduplication = Workflow.builder()
                .type(WorkflowType.EVENT)
                .createTime(now)
                .updateTime(now)
                .status(WorkflowStatus.RUNNING)
                .priority(99)
                .trigger(getDefaultEventTrigger())
                .tags(Workflow.DEFAULT_TAG)
                .tenantId(AtEventConfig.defaultTenantId).createUserId(AtEventConfig.defaultUserId)
                .steps(getDefaultDeduplicationSteps())
                .description("default deduplication event workflow")
                .build();

        // 落库
        workflowRepository.saveAll(Lists.newArrayList(classify, deduplication));
    }

    public List<Action> getDefaultClassifySteps() {
        List<Action> steps = Lists.newArrayList();
        // 分类 action
        ClassifyActionParams classifyActionContext = new ClassifyActionParams();
        classifyActionContext.setClassifyFields(atEventConfig.getDefaultClassifyFields());
        Action a = new Action(classifyActionContext.getName(), JsonUtils.toJSONString(classifyActionContext));
        steps.add(a);

        return steps;
    }

    private List<Action> getDefaultDeduplicationSteps() {
        // 去重 action
        DeduplicationActionParams dedupActionContext = new DeduplicationActionParams();
        dedupActionContext.getDedupeFields().addAll(atEventConfig.getDefaultDeduplicationFields());
        // TODO 不是所有字段 都使用 last函数，service 使用 concat 连接
        dedupActionContext.setDefaultAggs(atEventConfig.getDefaultAggs());
        Action action =
                new Action(DeduplicationActionParams.name, JsonUtils.toJSONString(dedupActionContext));
        return Lists.newArrayList(action);
    }

    public Trigger getDefaultEventTrigger() {
        List<Term> terms = Lists.newArrayList();

        return Trigger.builder()
                .type(TriggerType.EVENT_CREATED)
                .terms(terms).build();
    }

    private void initRawEventQueueAndConsumer() {
        List<IngestionInstance> all = ingestionInstanceRepository.findByStatus(IngestionInstanceStatus.Running);
        if(CollectionUtils.isEmpty(all)) {
            return;
        }
        all.forEach(
                instance -> {

                    rawEventQueueManager.addRawEventQueue(instance, instance.getCreateUserId(), instance.getTenantId());

                    Workflow workflow = rawEventConsumerManager.createWorkflowByIngestion(instance);

                    rawEventConsumerManager.runConsumer(instance,Lists.newArrayList(workflow));
                }
        );
    }

//    private void initRawEventConsumer() {
//        List<Workflow> allWorkflow =
//                workflowRepository.findByTriggerTypeOrderByPriorityAsc(TriggerType.RAW_EVENT_COLLECT);
//        if (CollectionUtils.isNotEmpty(allWorkflow)) {
//            // 一个用户一个 workflow
//            Map<String, List<Workflow>> workflowByIngestionId = allWorkflow.stream()
//                    .collect(Collectors.groupingBy(Workflow::getIngestionId, LinkedHashMap::new, Collectors.toList()));
//            workflowByIngestionId.forEach(
//                    (ingestionId, workflows) -> {
//                        WorkflowExecutorContext executorContext = WorkflowExecutorContext.builder()
//                                .workflowList(workflows)
//                                .alarmRepository(alarmRepository)
//                                .eventQueue(eventQueueManager.getQueueByUserId(workflows.get(0).getCreateUserId()))
//                                .build();
//                        RawEventConsumer consumer = RawEventConsumer.builder()
//                                .rawEventQueue(rawEventQueueManager.getQueueByIngestionId(ingestionId))
//                                .threadPoolManager(threadPoolManager)
//                                .workflowExecutorContext(executorContext)
//                                .build();
//                        threadPoolManager.getRawEventConsumerThreadPool().execute(consumer);
//                    }
//            );
//        } else {
//            log.warn("not find any workflow");
//        }
//    }


}
