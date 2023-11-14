package cn.endorphin.atevent.infrastructure.elasticsearch;

import java.time.Duration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchClients;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchConfiguration;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

/**
 * @author timothy
 * @DateTime: 2023/7/19 15:13
 **/
@Configuration
@EnableElasticsearchRepositories
public class EsClientConfig extends ElasticsearchConfiguration {

    @Value("${es.host:localhost:9200}")
    private String esHost;

    @Override
    public ClientConfiguration clientConfiguration() {
        return ClientConfiguration.builder()
            .connectedTo(esHost)
            .withConnectTimeout(Duration.ofSeconds(10))
            .withSocketTimeout(Duration.ofSeconds(5))
            .withClientConfigurer(
                ElasticsearchClients.ElasticsearchClientConfigurationCallback.from(
                    httpAsyncClientBuilder -> {
                        return httpAsyncClientBuilder;
                    }
                )
            )
            .build();
    }

    //    @Override
    //    @Bean
    //    public ElasticsearchCustomConversions elasticsearchCustomConversions() {
    //        return new ElasticsearchCustomConversions(Lists.newArrayList(new ActionPropertyValueConverter(null)));
    //    }
    //
    //    @WritingConverter
    //    static class ListToAction implements Converter<List<String>, List<Action>> {
    //
    //        @Override
    //        public List<Action> convert(List<String> source) {
    //            List<Action> result = Lists.newArrayList();
    //            return result;
    //        }
    //    }
}
