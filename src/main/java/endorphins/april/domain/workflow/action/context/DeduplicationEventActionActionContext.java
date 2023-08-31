package endorphins.april.domain.workflow.action.context;

import java.util.List;
import java.util.Map;

import endorphins.april.domain.workflow.action.AggsRule;
import lombok.Data;

/**
 * @author timothy
 * @DateTime: 2023/8/29 15:56
 **/
@Data
public class DeduplicationEventActionActionContext implements ActionContext {

    public static final String name = "deduplication";

    /**
     * 去重字段列表
     */
    private List<String> dedupFields;

    /**
     * key 需要聚合的字段, value 聚合的类型
     */
    private Map<String, AggsRule> aggsRule;

    private String defaultAggs;

    @Override
    public String getName() {
        return name;
    }
}
