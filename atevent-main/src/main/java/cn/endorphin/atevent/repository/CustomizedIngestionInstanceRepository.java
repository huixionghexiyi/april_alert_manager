package cn.endorphin.atevent.repository;

import cn.endorphin.atevent.entity.IngestionInstance;
import cn.endorphin.atevent.model.ingestion.IngestionQueryParam;
import org.springframework.data.domain.Page;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchPage;

/**
 * @author timothy
 * @DateTime: 2023/11/15 16:36
 **/
public interface CustomizedIngestionInstanceRepository {
    Page<IngestionInstance> page(IngestionQueryParam queryParam, int pageNum, int pageSize);
}
