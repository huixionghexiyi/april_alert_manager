package cn.endorphin.atevent.model.mapping;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Map;

/**
 * @author timothy
 * @DateTime: 2023/9/21 19:49
 **/
@Data
public class MappingRule {

    /**
     * 映射类型
     */
    private MappingRuleType type;

    /**
     * 映射目标
     */
    private String targetKey;

    /**
     * 映射来源
     * <p>
     * 支持多个来源
     */
    private List<String> sourceKeys;

    /**
     * 当 type 为 CONDITIONAL 时，使用该字段
     * 依据列表顺序，依次判断，满足一个第一个conditional后，执行后就不再执行后面的  conditional
     */
    private List<Conditional> conditional;

    /**
     * 当前映射规则的 值的映射
     */
    private Map<String, String> converter;

    private String defaultValue;

    /**
     * 当 type 为 CONCATENATE时，使用该字段
     */
    private String delimiter;

    public Object convertValue(Object sourceValue) {
        String ret = converter.get((String) sourceValue);
        if (StringUtils.isEmpty(ret)) {
            return defaultValue;
        }
        return ret;
    }
}
