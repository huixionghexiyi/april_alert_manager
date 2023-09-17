package endorphins.april.service.event;

import java.util.Map;
import java.util.Set;

import endorphins.april.model.Event;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

/**
 * @author timothy
 * @DateTime: 2023/8/24 22:24
 **/
public class EventHelper {
    public static final Set<String> defaultKeySet = Sets.newHashSet(
        "source",
        "check",
        "kind",
        "type",
        "description",
        "severity",
        "service"
    );

    public static Map<String, Object> getInsiderFieldsMap(Event event) {
        Map<String, Object> ret = Maps.newHashMap();
        ret.put("source", event.getSource());
        ret.put("check", event.getCheck());
        ret.put("kind", event.getKind());
        ret.put("type", event.getType());
        ret.put("description", event.getDescription());
        ret.put("severity", event.getSeverity());
        ret.put("service", event.getService());
        return  ret;
    }
}
