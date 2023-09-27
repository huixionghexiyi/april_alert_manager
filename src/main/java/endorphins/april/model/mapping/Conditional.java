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

    private ConditionalType operator;
    /**
     * 当前映射规则的 值的映射
     */
    private Map<MappingConverterType, List<String>> converter;

}
