package cn.endorphin.atevent.model.mapping;

import java.util.List;

import lombok.Data;

/**
 * @author timothy
 * @DateTime: 2023/9/27 17:34
 **/
@Data
public class IngestionConfig {

    /**
     * 如果一个消息中包含多个event，设置该字段
     * 指定一批 alerts 存储在哪个key
     */
    private String batchKey;

    private List<MappingRule> mappings;
}
