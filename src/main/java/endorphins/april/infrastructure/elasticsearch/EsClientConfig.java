package endorphins.april.infrastructure.elasticsearch;

import java.time.Duration;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.WritingConverter;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchClients;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchConfiguration;
import org.springframework.data.elasticsearch.core.convert.ElasticsearchCustomConversions;

import endorphins.april.domain.workflow.action.Action;
import endorphins.april.domain.workflow.converter.ActionPropertyValueConverter;

import com.google.common.collect.Lists;

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
