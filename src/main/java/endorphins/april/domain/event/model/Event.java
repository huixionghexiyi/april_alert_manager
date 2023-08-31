package endorphins.april.domain.event.model;

import java.io.Serializable;
import java.util.Map;

import endorphins.april.domain.event.EventUtils;
import lombok.Data;

import com.google.common.collect.Maps;
import com.sun.istack.internal.NotNull;

/**
 * @author timothy
 * @DateTime: 2023/8/17 17:27
 **/
@Data
public class Event implements Serializable {

    /**
     * 被检查项
     * 在 cmdb 中，通常为 配置项
     * 在 指标中，通常为 指标名
     */
    @NotNull
    private String check;

    private String type;

    private String clazz;

    /**
     * 告警描述
     */
    @NotNull
    private String description;

    @NotNull
    private String source;

    /**
     * 服务
     */
    private String service;

    private Integer severity;

    private String deduplicationKey;

    private long time;

    private Map<String, Object> tags;

    public Map<String, Object> insideFieldsMap() {
        return EventUtils.getInsiderFieldsMap(this);
    }
}
