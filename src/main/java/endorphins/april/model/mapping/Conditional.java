package endorphins.april.model.mapping;

import java.util.List;
import java.util.Map;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * @author timothy
 * @DateTime: 2023/9/27 15:38
 **/
@Data
@Slf4j
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

    public Boolean checkValue(List<Boolean> conditionResult) {
        if (operator == OperatorType.AND) {
            Boolean ret = true;
            for (Boolean r : conditionResult) {
                ret = ret && r;
            }
            return ret;
        } else if (operator == OperatorType.OR) {
            Boolean ret = false;
            for (Boolean r : conditionResult) {
                ret = ret || r;
            }
            return ret;
        }
        throw new UnsupportedOperationException("Not Support operator[" + operator + "]");
    }

    /**
     * @author timothy
     * @DateTime: 2023/9/27 19:28
     **/
    @Data
    public static class Condition {

        private String sourceKey;

        private OperatorType operator;

        private String value;

        public boolean checkValue(Object value) {
            switch (operator) {
                case EQUALS:
                    return value.equals(this.value);
                case NOT_EQUALS:
                    return !value.equals(this.value);
                case CONTAINS:
                    return value.toString().contains(this.value);
                case NOT_CONTAINS:
                    return !value.toString().contains(this.value);
                case BEGINS_WITH:
                    return value.toString().startsWith(this.value);
                case NOT_BEGIN_WITH:
                    return !value.toString().startsWith(this.value);
                case ENDS_WITH:
                    return value.toString().endsWith(this.value);
                case NOT_END_WITH:
                    return !value.toString().endsWith(this.value);
                case EXIST:
                    return value != null;
                default:
                    throw new UnsupportedOperationException("Not Support operator[" + operator + "]");
            }
        }

    }
}
