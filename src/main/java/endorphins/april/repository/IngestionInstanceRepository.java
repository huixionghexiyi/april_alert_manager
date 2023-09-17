package endorphins.april.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import endorphins.april.entity.ApiKey;
import endorphins.april.entity.IngestionInstance;

/**
 * @author timothy
 * @DateTime: 2023/8/31 15:31
 **/
public interface IngestionInstanceRepository
    extends CrudRepository<IngestionInstance, String>, PagingAndSortingRepository<IngestionInstance, String> {

    List<ApiKey> findByName(String defaultEventKey);
}
