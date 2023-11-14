package cn.endorphin.atevent.repository;

import java.util.List;

import cn.endorphin.atevent.entity.ApiKey;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * @author timothy
 * @DateTime: 2023/8/31 15:31
 **/
public interface ApiKeyRepository
    extends CrudRepository<ApiKey, String>, PagingAndSortingRepository<ApiKey, String> {

    List<ApiKey> findByName(String defaultEventKey);
}
