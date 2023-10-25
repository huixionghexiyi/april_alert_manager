package endorphins.april.service.apikey;

import java.util.List;
import java.util.Optional;

import endorphins.april.entity.ApiKey;

/**
 * @author timothy
 * @DateTime: 2023/10/19 17:46
 **/
public interface ApiKeyService {
    /**
     * 根据 ingestion instance 获取APIkey 列表
     * @param id
     * @return
     */
    List<ApiKey> getApiKeyByIngestion(String id);

    Optional<ApiKey> findById(String apiKey);
}
