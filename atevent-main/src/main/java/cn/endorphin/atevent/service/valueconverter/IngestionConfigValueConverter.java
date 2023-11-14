package cn.endorphin.atevent.service.valueconverter;

import cn.endorphin.atevent.infrastructure.json.JsonUtils;
import cn.endorphin.atevent.model.mapping.IngestionConfig;
import org.springframework.data.elasticsearch.core.mapping.PropertyValueConverter;

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
