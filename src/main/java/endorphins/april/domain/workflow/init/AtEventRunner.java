package endorphins.april.domain.workflow.init;

import java.util.List;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.Criteria;
import org.springframework.data.elasticsearch.core.query.CriteriaQueryBuilder;

import endorphins.april.domain.model.Workflow;
import endorphins.april.domain.workflow.Term;
import endorphins.april.domain.workflow.WorkflowStatus;
import endorphins.april.domain.workflow.WorkflowType;
import endorphins.april.domain.workflow.action.Action;
import endorphins.april.domain.workflow.action.context.ClassifyEventActionActionContext;
import endorphins.april.domain.workflow.action.context.DeduplicationEventActionActionContext;
import endorphins.april.domain.workflow.config.AtEventConfig;
import endorphins.april.domain.workflow.trigger.Trigger;
import endorphins.april.domain.workflow.trigger.TriggerType;
import endorphins.april.infrastructure.json.JsonUtils;
import lombok.AllArgsConstructor;

import com.google.common.collect.Lists;

/**
 * @author timothy
 * @DateTime: 2023/8/29 15:19
 **/
@AllArgsConstructor
@Configuration
@Order(1)
public class AtEventRunner implements ApplicationRunner {

    private ElasticsearchOperations elasticsearchOperations;

    private AtEventConfig atEventConfig;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        CriteriaQueryBuilder builder = new CriteriaQueryBuilder(new Criteria("id").is("1"));
        SearchHits<Workflow> workflowSearchHits =
            elasticsearchOperations.search(builder.build(), Workflow.class);
        if (workflowSearchHits.hasSearchHits()) {
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
            .priority(1)
            .trigger(trigger)
            .steps(steps)
            .description("default event workflow")
            .build();

        elasticsearchOperations.save(entity);
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
