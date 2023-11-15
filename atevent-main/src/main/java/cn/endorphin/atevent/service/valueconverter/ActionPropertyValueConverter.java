package cn.endorphin.atevent.service.valueconverter;

import java.util.Map;

import cn.endorphin.atevent.workflow.Action;
import org.springframework.data.elasticsearch.core.mapping.PropertyValueConverter;

/**
 * workflow 中每一个action的转换
 * @author timothy
 * @DateTime: 2023/8/30 09:45
 **/
public class ActionPropertyValueConverter implements PropertyValueConverter {

    @Override
    public Object write(Object value) {
        return value;
    }

    @Override
    public Object read(Object value) {
        Action action = null;
        if (value instanceof Map) {
            Map<String, String> valueMap = (Map<String, String>) value;
            String name = valueMap.get("name");
            String context = valueMap.get("params");
            action = new Action(name, context);
        }
        return action;
    }

}
