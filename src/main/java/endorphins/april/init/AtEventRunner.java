package endorphins.april.init;

import java.util.List;
import java.util.Optional;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

import endorphins.april.config.AtEventConfig;
import endorphins.april.entity.ApiKey;
import endorphins.april.entity.Workflow;
import endorphins.april.infrastructure.json.JsonUtils;
import endorphins.april.repository.ApiKeyRepository;
import endorphins.april.repository.WorkflowRepository;
import endorphins.april.service.workflow.Term;
import endorphins.april.service.workflow.WorkflowStatus;
import endorphins.april.service.workflow.WorkflowType;
import endorphins.april.service.workflow.Action;
import endorphins.april.service.workflow.event.ClassifyActionParams;
import endorphins.april.service.workflow.event.DeduplicationActionParams;
import endorphins.april.service.workflow.event.EventQueueManager;
import endorphins.april.service.workflow.Trigger;
import endorphins.april.service.workflow.TriggerType;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.google.common.collect.Lists;

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

    @Override
    public void run(ApplicationArguments args) throws Exception {
        /**
         * 初始化 服务需要的基本数据
         */
        initData();
        /**
         * 初始化 event queue
         */
        initEventQueue();
        /**
         *  初始化 event consumer
         */
        initEventConsumer();
    }

    private void initEventConsumer() {
        // TODO 多个 apiKey 对应一个 event Consumer

    }

    @Deprecated
    private void initEventQueue() {
        Iterable<ApiKey> allApiKey = apiKeyRepository.findAll();
        for (ApiKey apiKey : allApiKey) {
            Long tenantId = apiKey.getTenantId();
            Long userId = apiKey.getCreateUserId();
            String apiKeyId = apiKey.getId();
            eventQueueManager.addEventQueue(apiKeyId, userId, tenantId, atEventConfig.getDefaultEventQueue());
        }
    }

    private void initData() {
        initApiKeyData();
        initWorkflowData();
    }

    private void initApiKeyData() {
        // TODO 一个用户至少一个 apiKey
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

        Optional<Workflow> workflow = workflowRepository.findByTags(Workflow.DEFAULT_TAG);
        if (workflow.isPresent()) {
            // log.error("not find DEFAULT workflow, please check");
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

        // 创建 去重 workflow，放到最后执行，包含了落库
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
}
