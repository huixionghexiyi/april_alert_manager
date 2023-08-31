package endorphins.april.domain.workflow.converter;

import java.util.Map;

import org.springframework.data.elasticsearch.core.mapping.PropertyValueConverter;

import endorphins.april.domain.workflow.action.Action;

/**
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
            String context = valueMap.get("context");
            action = new Action(name, context);
            action.initExecutor();
        }
        return action;
    }

}
