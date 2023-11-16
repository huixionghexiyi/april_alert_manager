package cn.endorphin.atevent.init;

import cn.endorphin.atevent.config.AtEventConfig;
import cn.endorphin.atevent.entity.ApiKey;
import cn.endorphin.atevent.entity.IngestionInstance;
import cn.endorphin.atevent.entity.Workflow;
import cn.endorphin.atevent.infrastructure.json.JsonUtils;
import cn.endorphin.atevent.infrastructure.thread.ThreadPoolManager;
import cn.endorphin.atevent.model.ingestion.IngestionInstanceStatus;
import cn.endorphin.atevent.repository.AlarmRepository;
import cn.endorphin.atevent.repository.ApiKeyRepository;
import cn.endorphin.atevent.repository.IngestionInstanceRepository;
import cn.endorphin.atevent.repository.WorkflowRepository;
import cn.endorphin.atevent.workflow.*;
import cn.endorphin.atevent.workflow.event.EventConsumer;
import cn.endorphin.atevent.workflow.event.EventQueueManager;
import cn.endorphin.atevent.workflow.executor.ActionExecutorManager;
import cn.endorphin.atevent.workflow.executor.ClassifyActionExecutor;
import cn.endorphin.atevent.workflow.executor.ClassifyActionParams;
import cn.endorphin.atevent.workflow.executor.DeduplicationActionParams;
import cn.endorphin.atevent.workflow.rawevent.RawEventConsumerManager;
import cn.endorphin.atevent.workflow.rawevent.RawEventQueueManager;
import com.google.common.collect.Lists;
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

    private ActionExecutorManager actionExecutorManager;

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
        initDefaultWorkflowData();

        /**
         * 初始化 event queue
         */
        initEventQueue();

        /**
         *  初始化 event consumer
         */
        initEventConsumer();

        initRawEventQueueAndConsumer();
    }


    private void initEventConsumer() {
        // 根据 tenant 区分 workflow
        List<Workflow> allWorkflow = workflowRepository.findByTriggerTypeOrderByPriorityAsc(TriggerType.EVENT_CREATED);
        if (CollectionUtils.isNotEmpty(allWorkflow)) {
            // 一个用户一个 workflow
            Map<Long, List<Workflow>> workflowByUserId = allWorkflow.stream()
                    .collect(Collectors.groupingBy(Workflow::getCreateUserId, LinkedHashMap::new, Collectors.toList()));
            workflowByUserId.forEach(
                    (createUserId, workflows) -> {
                        WorkflowExecutorContext executorContext = WorkflowExecutorContext.builder()
                                .workflowList(workflows)
                                .alarmRepository(alarmRepository)
                                .actionExecutorManager(actionExecutorManager)
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

    private void initDefaultWorkflowData() {
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
        Action a = new Action(ClassifyActionExecutor.name, JsonUtils.toJSONString(classifyActionContext));
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
        List<Condition> conditions = Lists.newArrayList();

        return Trigger.builder()
                .type(TriggerType.EVENT_CREATED)
                .conditions(conditions).build();
    }

    private void initRawEventQueueAndConsumer() {
        List<IngestionInstance> all = ingestionInstanceRepository.findByStatus(IngestionInstanceStatus.RUNNING);
        if (CollectionUtils.isEmpty(all)) {
            return;
        }
        all.forEach(
                instance -> {

                    rawEventQueueManager.addRawEventQueue(instance, instance.getCreateUserId(), instance.getTenantId());

                    Workflow workflow = rawEventConsumerManager.createWorkflowByIngestion(instance);

                    rawEventConsumerManager.runConsumer(instance, Lists.newArrayList(workflow));
                }
        );
    }
}
