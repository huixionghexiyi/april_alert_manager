package endorphins.april.model.mapping;

import java.util.List;

import lombok.Data;

/**
 * @author timothy
 * @DateTime: 2023/9/27 17:34
 **/
@Data
public class IngestionConfig {

    /**
     * 如果一个消息中包含多个事件，设置该字段
     */
    private String batchKey;

    private List<MappingRule> mappings;
}
