package cn.endorphin.atevent.workflow.event;

import java.util.List;

import lombok.Data;

/**
 * 聚合规则
 *
 * @author timothy
 * @DateTime: 2023/8/24 17:25
 **/
@Data
public class AggsRule {
    private Aggs aggs;
    private List<String> params;
    private String order;

    public AggsRule(Aggs aggs, List<String> params, String order) {
        this.aggs = aggs;
        this.params = params;
        this.order = order;

    }

    public static AggsRule last() {
        return new AggsRule(Aggs.last, null, null);
    }
    enum Aggs {
        last,
        concat,

        first,
        max,
        argMax,
        argMin,
        top
    }
}
