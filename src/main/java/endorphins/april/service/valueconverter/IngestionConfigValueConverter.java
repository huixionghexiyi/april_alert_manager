package endorphins.april.service.valueconverter;

import org.springframework.data.elasticsearch.core.mapping.PropertyValueConverter;

import endorphins.april.infrastructure.json.JsonUtils;
import endorphins.april.model.mapping.IngestionConfig;

import java.util.Map;

/**
 * 摄取配置值的 转换器
 * @author timothy
 * @DateTime: 2023/9/27 19:51
 **/
public class IngestionConfigValueConverter implements PropertyValueConverter {
    @Override
    public Object write(Object value) {
        return value;
    }

    @Override
    public Object read(Object value) {
        if(value instanceof Map) {
            return JsonUtils.getMapper().convertValue(value, IngestionConfig.class);
        }
        return value;
    }
}
