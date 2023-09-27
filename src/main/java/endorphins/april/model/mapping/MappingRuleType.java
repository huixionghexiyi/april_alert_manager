package endorphins.april.model.mapping;

/**
 * @author timothy
 * @DateTime: 2023/9/27 15:34
 **/
public enum MappingRuleType {

    BASIC,
    /**
     * 目标字段是个列表
     */
    LIST,
    /**
     * 条件映射
     */
    CONDITIONAL,

    /**
     * 关联，用于构建去重字段
     */
    CONCATENATE

}
