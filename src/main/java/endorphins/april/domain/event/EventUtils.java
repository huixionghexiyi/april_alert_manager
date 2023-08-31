package endorphins.april.domain.event;

import java.util.Map;
import java.util.Set;

import endorphins.april.domain.event.model.Event;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

/**
 * @author timothy
 * @DateTime: 2023/8/24 22:24
 **/
public class EventUtils {
    public static final Set<String> defaultKeySet = Sets.newHashSet(
        "source",
        "check",
        "class",
        "type",
        "description",
        "severity",
        "service"
    );

    public static Map<String, Object> getInsiderFieldsMap(Event event) {
        Map<String, Object> ret = Maps.newHashMap();
        ret.put("source", event.getSource());
        ret.put("check", event.getSource());
        ret.put("class", event.getClazz());
        ret.put("type", event.getType());
        ret.put("description", event.getDescription());
        ret.put("severity", event.getSeverity());
        ret.put("service", event.getService());
        return  ret;
    }
}
