package cn.endorphin.atevent.repository.base;

import cn.endorphin.atevent.entity.ApiKey;
import cn.endorphin.atevent.entity.IngestionInstance;
import cn.endorphin.atevent.model.ingestion.IngestionInstanceStatus;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

/**
 * @author timothy
 * @DateTime: 2023/8/31 15:31
 **/
public interface IngestionInstanceCustomizedRepository
        extends CrudRepository<IngestionInstance, String>, PagingAndSortingRepository<IngestionInstance, String>, cn.endorphin.atevent.repository.IngestionInstanceCustomizedRepository {

    List<ApiKey> findByName(String defaultEventKey);

    List<IngestionInstance> findByStatus(IngestionInstanceStatus ingestionInstanceStatus);
}
