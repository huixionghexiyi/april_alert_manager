package endorphins.april.config;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import lombok.Data;

/**
 * @author timothy
 * @DateTime: 2023/8/29 21:18
 **/
@Configuration
@Data
public class AtEventConfig {
    /**
     * 默认分类字段
     */
    @Value("${at.event.default.classify.fields:service,description,check,source}")
    private List<String> defaultClassifyFields;

    /**
     * 默认去重字段
     */
    @Value("${at.event.default.deduplication.fields:source,class,check,service}")
    private List<String> defaultDeduplicationFields;

    /**
     * 默认聚合方式
     */
    @Value("${at.event.default.aggs.rule:last}")
    private String defaultAggs;

    @Value("${at.event.default.event.queue.size:16}")
    private Integer defaultEventQueue;

    private long defaultTenantId = 1L;
}
