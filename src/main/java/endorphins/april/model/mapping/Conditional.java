package endorphins.april.model.mapping;

import java.util.List;
import java.util.Map;

import lombok.Data;

/**
 * @author timothy
 * @DateTime: 2023/9/27 15:38
 **/
@Data
public class Conditional {

    /**
     * 决定下面每个 conditions 的 与/或 关系，只能是 and/or 两种 OperatorType
     */
    private OperatorType operator;

    /**
     * 筛选条件 满足这些条件的 events 会根据 mapping 进行映射
     */
    private List<Condition> conditions;

    /**
     * 在 conditional 中的 mapping 只能为 BASIC 类型
     */
    private MappingRule mapping;

    /**
     * 当前映射规则的 值的映射
     */
    private Map<MappingConverterType, List<String>> converter;

    /**
     * @author timothy
     * @DateTime: 2023/9/27 19:28
     **/
    @Data
    public static class Condition {

        private String sourceKey;

        private OperatorType operator;

        private String value;

    }
}
