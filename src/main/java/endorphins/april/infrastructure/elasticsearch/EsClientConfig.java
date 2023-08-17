package endorphins.april.infrastructure.elasticsearch;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchConfiguration;
import org.springframework.http.HttpHeaders;

/**
 * @author timothy
 * @DateTime: 2023/7/19 15:13
 **/
@Configuration
public class EsClientConfig extends ElasticsearchConfiguration {

    @Value("${es.host:localhost:9200}")
    private String esHost;

    @Override
    public ClientConfiguration clientConfiguration() {
        return ClientConfiguration.builder()
            .connectedTo(esHost)
            .build();
    }

}
