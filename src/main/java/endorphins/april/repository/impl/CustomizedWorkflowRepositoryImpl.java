package endorphins.april.repository.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.stereotype.Service;

import co.elastic.clients.elasticsearch._types.query_dsl.TermQuery;
import endorphins.april.entity.Workflow;
import endorphins.april.repository.CustomizedWorkflowRepository;
import endorphins.april.service.workflow.trigger.TriggerType;

import com.google.common.collect.Lists;

/**
 * @author timothy
 * @DateTime: 2023/8/31 17:29
 **/
@Service
public class CustomizedWorkflowRepositoryImpl implements CustomizedWorkflowRepository {

    @Autowired
    private ElasticsearchOperations operations;

    @Override
    public List<Workflow> findByTriggerTypeOrderByPriorityAsc(TriggerType triggerType) {
        //        CriteriaQuery query = CriteriaQuery.builder(
        //            new Criteria("trigger.type")
        //                .is(TriggerType.EVENT_CREATED)
        //        ).build();
        List<Workflow> result = Lists.newArrayList();
        NativeQuery query = NativeQuery.builder()
            .withQuery(q -> q.term(TermQuery.of(a -> a.field("trigger.type")
                .value(TriggerType.EVENT_CREATED.name()))))
            .withSort(Sort.by("priority").ascending())
            .build();
        SearchHits<Workflow> workflowSearchHits = operations.search(query, Workflow.class);
        if (workflowSearchHits.hasSearchHits()) {
            workflowSearchHits.getSearchHits().forEach(
                workflow -> {
                    Workflow currWorkflow = workflow.getContent();
                    result.add(currWorkflow);
                }
            );
        }
        return result;
    }
}
