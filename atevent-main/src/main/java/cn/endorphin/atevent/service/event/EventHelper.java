package cn.endorphin.atevent.service.event;

import java.util.Set;

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

}
