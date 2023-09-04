package endorphins.april.service.workflow.action.params;

import java.util.List;
import java.util.Map;
import java.util.Set;

import endorphins.april.service.workflow.action.AggsRule;
import lombok.Data;
import lombok.Getter;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

/**
 * @author timothy
 * @DateTime: 2023/8/29 15:56
 **/
@Getter
public class DeduplicationActionParams implements ActionParams {

    public static final String name = "deduplication";

    /**
     * 去重字段列表
     * 有序
     */
    private Set<String> dedupeFields = Sets.newTreeSet();

    /**
     * key 需要聚合的字段, value 聚合的类型
     */
    private Map<String, AggsRule> aggsRule = Maps.newHashMap();

    private String defaultAggs;

    public void setDefaultAggs(String defaultAggs) {
        this.defaultAggs = defaultAggs;
    }

    @Override
    public String getName() {
        return name;
    }
}
