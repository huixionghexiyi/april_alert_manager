package cn.endorphin.atevent.repository;

import java.util.List;

import cn.endorphin.atevent.entity.ApiKey;
import cn.endorphin.atevent.model.ingestion.IngestionInstanceStatus;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import cn.endorphin.atevent.entity.IngestionInstance;

/**
 * @author timothy
 * @DateTime: 2023/8/31 15:31
 **/
public interface IngestionInstanceRepository
    extends CrudRepository<IngestionInstance, String>, PagingAndSortingRepository<IngestionInstance, String> {

    List<ApiKey> findByName(String defaultEventKey);

    List<IngestionInstance> findByStatus(IngestionInstanceStatus ingestionInstanceStatus);
}
