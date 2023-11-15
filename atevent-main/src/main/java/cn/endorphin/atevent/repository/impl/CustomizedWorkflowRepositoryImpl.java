package cn.endorphin.atevent.repository.impl;

import java.util.List;

import cn.endorphin.atevent.entity.Workflow;
import cn.endorphin.atevent.repository.CustomizedWorkflowRepository;
import cn.endorphin.atevent.workflow.TriggerType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.stereotype.Service;

import co.elastic.clients.elasticsearch._types.query_dsl.TermQuery;

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
        List<Workflow> result = Lists.newArrayList();
        NativeQuery query = NativeQuery.builder()
            .withQuery(q -> q.term(TermQuery.of(a -> a.field("trigger.type")
                .value(triggerType.name()))))
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
