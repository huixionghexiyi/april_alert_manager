package endorphins.april.application.prometheus;

import endorphins.april.application.judge.timeWindow.TimeWindowJudgeRule;
import endorphins.april.core.judge.JudgeData;
import endorphins.april.core.label.LabelSet;
import endorphins.april.infrastructure.TimeIntervalUtils;
import lombok.Builder;
import lombok.Data;

/**
 * prometheus 判定规则
 *
 * @author timothy.yang cloudwise
 * @since 2022-12-24 19:04
 */
@Data
@Builder
public class PrometheusJudgeRule implements TimeWindowJudgeRule {

    /**
     * 告警表达式
     * up == 0
     */
    private String expr;

    /**
     * 持续时间长度
     * 5m
     */
    private String forInterval;

    /**
     * 每次评估间隔
     * 1m
     */
    private String evaluationInterval;

    /**
     * 为 告警追加标签
     */
    private LabelSet labels;

    /**
     * 为 告警追加的信息
     * severity: page
     * description: 描述
     */
    private LabelSet annotations;

    /**
     * 是否在返回结果中追加判定的数据
     *
     * @return
     */
    private boolean appendJudgeData = true;

    @Override
    public LabelSet getAnnotations() {
        return annotations;
    }

    @Override
    public LabelSet getLabels() {
        return labels;
    }

    @Override
    public boolean isAbnormal(JudgeData judgeData) {
        // TODO: 2022/12/28
        return false;
    }

    @Override
    public boolean appendJudgeData() {
        return appendJudgeData;
    }

    @Override
    public long[] expectationValueSizeRange() {
        long evaluationIntervalSec = TimeIntervalUtils.getSeconds(evaluationInterval);
        long forIntervalSec = TimeIntervalUtils.getSeconds(forInterval);
        return new long[]{forIntervalSec / evaluationIntervalSec, Integer.MAX_VALUE};
    }
}
