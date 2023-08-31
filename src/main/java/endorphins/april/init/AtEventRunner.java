package endorphins.april.init;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

import endorphins.april.config.AtEventConfig;
import endorphins.april.entity.ApiKey;
import endorphins.april.entity.Event;
import endorphins.april.entity.Workflow;
import endorphins.april.infrastructure.json.JsonUtils;
import endorphins.april.repository.ApiKeyRepository;
import endorphins.april.repository.WorkflowRepository;
import endorphins.april.service.workflow.Term;
import endorphins.april.service.workflow.WorkflowStatus;
import endorphins.april.service.workflow.WorkflowType;
import endorphins.april.service.workflow.action.Action;
import endorphins.april.service.workflow.action.context.ClassifyEventActionActionContext;
import endorphins.april.service.workflow.action.context.DeduplicationEventActionActionContext;
import endorphins.april.service.workflow.queue.EventQueueManager;
import endorphins.april.service.workflow.trigger.Trigger;
import endorphins.april.service.workflow.trigger.TriggerType;
import lombok.AllArgsConstructor;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

/**
 * @author timothy
 * @DateTime: 2023/8/29 15:19
 **/
@AllArgsConstructor
@Configuration
@Order(1)
public class AtEventRunner implements ApplicationRunner {

    private AtEventConfig atEventConfig;

    private WorkflowRepository workflowRepository;

    private ApiKeyRepository apiKeyRepository;

    private EventQueueManager eventQueueManager;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        initData();
        initEventQueue();
    }

    private void initEventQueue() {
        Iterable<ApiKey> allApiKey = apiKeyRepository.findAll();
        Set<Long> tenantSet = Sets.newHashSet();
        for (ApiKey apiKey : allApiKey) {
            tenantSet.add(apiKey.getTenantId());
        }
        Map<Long, BlockingQueue<Event>> queueMap = eventQueueManager.getQueueMap();
        for (Long tenantId : tenantSet) {
            queueMap.put(tenantId, new ArrayBlockingQueue<>(atEventConfig.getDefaultEventQueue()));
        }
    }

    private void initData() {
        initApiKeyData();
        initWorkflowData();
    }

    private void initApiKeyData() {
        // TODO 增加用户管理时，再移除此处，apiKey有用户来拥有，而不是默认初始化的，一个用户至少有一个 apiKey
        String defaultEventKey = "defaultEventKey";
        List<ApiKey> byName = apiKeyRepository.findByName(defaultEventKey);
        if (CollectionUtils.isNotEmpty(byName)) {
            return;
        }
        ApiKey entity = new ApiKey();
        entity.setDescription("default api key");
        entity.setName(defaultEventKey);
        entity.setCreateUserId(1L);
        entity.setTenantId(atEventConfig.getDefaultTenantId());
        apiKeyRepository.save(entity);
    }

    private void initWorkflowData() {
        Optional<Workflow> workflow = workflowRepository.findById("1");
        if (workflow.isPresent()) {
            return;
        }

        long now = System.currentTimeMillis();
        Trigger trigger = getDefaultEventTrigger();
        List<Action> steps = getDefaultEventStepList();
        Workflow entity = Workflow.builder()
            .id("1")
            .type(WorkflowType.EVENT)
            .createTime(now)
            .updateTime(now)
            .status(WorkflowStatus.RUNNING)
            .priority(0)
            .trigger(trigger)
            .tenantId(atEventConfig.getDefaultTenantId())
            .steps(steps)

            .description("default event workflow")
            .build();

        workflowRepository.save(entity);
    }

    public List<Action> getDefaultEventStepList() {
        List<Action> steps = Lists.newArrayList();
        // 分类 action
        ClassifyEventActionActionContext classifyActionContext = new ClassifyEventActionActionContext();
        classifyActionContext.setClassifyFields(atEventConfig.getDefaultClassifyFields());
        Action a = new Action(classifyActionContext.getName(), JsonUtils.toJSONString(classifyActionContext));
        steps.add(a);

        // 去重 action
        DeduplicationEventActionActionContext dedupActionContext = new DeduplicationEventActionActionContext();
        dedupActionContext.setDedupFields(atEventConfig.getDefaultDeduplicationFields());
        dedupActionContext.setDefaultAggs(atEventConfig.getDefaultAggs());
        Action action =
            new Action(DeduplicationEventActionActionContext.name, JsonUtils.toJSONString(dedupActionContext));
        steps.add(action);

        return steps;
    }

    public Trigger getDefaultEventTrigger() {
        List<Term> terms = Lists.newArrayList();

        return Trigger.builder()
            .type(TriggerType.EVENT_CREATED)
            .terms(terms).build();
    }
}
