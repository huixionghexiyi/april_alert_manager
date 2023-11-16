package cn.endorphin.atevent.repository.impl;

import cn.endorphin.atevent.entity.IngestionInstance;
import cn.endorphin.atevent.model.ingestion.IngestionQueryParam;
import cn.endorphin.atevent.repository.CustomizedIngestionInstanceRepository;
import co.elastic.clients.elasticsearch._types.query_dsl.RegexpQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.TermQuery;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author timothy
 * @DateTime: 2023/11/15 16:35
 **/
@Service
public class IngestionInstanceRepositoryImpl implements CustomizedIngestionInstanceRepository {

    @Autowired
    private ElasticsearchOperations operations;

    @Override
    public Page<IngestionInstance> page(IngestionQueryParam queryParam, int pageNum, int pageSize) {
        PageRequest pageable = PageRequest.of(pageNum, pageSize);
        Sort by = null;
        if (queryParam.getSortField() == null) {
            if (queryParam.getSortDirection() != null) {
                Sort.by(queryParam.getSortDirection(), queryParam.getSortField());
            } else {
                Sort.by(queryParam.getSortField());
            }
            pageable.withSort(by);
        }
        NativeQuery query1 = NativeQuery.builder()
                .withQuery(
                        q -> q.bool(
                                must -> {
                                    if (queryParam.getStatus() != null) {
                                        must.must(m -> m.term(TermQuery.of(t -> t.field("status").value(queryParam.getStatus().name()))));
                                    }
                                    if (queryParam.getName() != null) {
                                        must.must(m -> m.regexp(RegexpQuery.of(t -> t.field("name").value(".*" + queryParam.getName() + ".*"))));
                                    }
                                    if (queryParam.getSourceType() != null) {
                                        must.must(m -> m.term(TermQuery.of(t -> t.field("sourceType").value(queryParam.getSourceType().name()))));
                                    }
                                    if (queryParam.getDescription() != null) {
                                        must.must(m -> m.regexp(RegexpQuery.of(t -> t.field("description").value(".*" + queryParam.getDescription() + ".*"))));
                                    }
                                    return must;
                                }
                        )
                )
                .withFields("id", "status")
                .withPageable(pageable)
                .build();
        SearchHits<IngestionInstance> search = operations.search(query1, IngestionInstance.class);
        List<SearchHit<IngestionInstance>> searchHits1 = search.getSearchHits();
        List<IngestionInstance> content = Lists.newArrayList();
        for (SearchHit<IngestionInstance> ingestionInstanceSearchHit : searchHits1) {
            content.add(ingestionInstanceSearchHit.getContent());
        }
        return new PageImpl<>(content, pageable, search.getTotalHits());
    }
}
