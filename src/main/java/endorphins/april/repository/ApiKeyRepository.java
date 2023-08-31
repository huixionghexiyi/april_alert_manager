package endorphins.april.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import endorphins.april.entity.ApiKey;

/**
 * @author timothy
 * @DateTime: 2023/8/31 15:31
 **/
public interface ApiKeyRepository
    extends CrudRepository<ApiKey, String>, PagingAndSortingRepository<ApiKey, String> {

    List<ApiKey> findByName(String defaultEventKey);
}
