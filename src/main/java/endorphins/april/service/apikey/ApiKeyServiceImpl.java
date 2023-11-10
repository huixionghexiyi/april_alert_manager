package endorphins.april.service.apikey;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import endorphins.april.entity.ApiKey;
import endorphins.april.repository.ApiKeyRepository;

/**
 * @author timothy
 * @DateTime: 2023/10/20 10:14
 **/
@Service
public class ApiKeyServiceImpl implements ApiKeyService {

    @Autowired
    private ApiKeyRepository repository;

    @Override
    public List<ApiKey> getApiKeyByIngestion(String ingestionId) {
        return null;
    }

    @Override
    public Optional<ApiKey> findById(String apiKey) {
        return repository.findById(apiKey);
    }
}
