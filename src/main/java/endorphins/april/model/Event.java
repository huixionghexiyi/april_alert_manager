package endorphins.april.model;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

import org.apache.commons.lang3.StringUtils;

import lombok.Data;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.sun.istack.internal.NotNull;

/**
 * @author timothy
 * @DateTime: 2023/8/17 17:27
 **/
@Data
public class Event implements Serializable {

    /**
     * 被检查项 在 cmdb 中，通常为 配置项 在 指标中，通常为 指标名
     */
    @NotNull
    private String check;

    private String type;

    private String kind;

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
}
