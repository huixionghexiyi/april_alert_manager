package cn.endorphin.atevent.repository;

import cn.endorphin.atevent.entity.IngestionInstance;
import cn.endorphin.atevent.model.ingestion.IngestionQueryParam;
import org.springframework.data.domain.Page;

/**
 * @author timothy
 * @DateTime: 2023/11/15 16:36
 **/
public interface IngestionInstanceCustomizedRepository {
    Page<IngestionInstance> page(IngestionQueryParam queryParam, int pageNum, int pageSize);

    IngestionInstance update(IngestionInstance ingestionInstance);
}
