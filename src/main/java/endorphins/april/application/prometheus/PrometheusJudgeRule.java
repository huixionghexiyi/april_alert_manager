package endorphins.april.application.prometheus;

import endorphins.april.application.judge.timeWindow.TimeWindowJudgeRule;
import endorphins.april.core.judge.JudgeData;
import endorphins.april.core.judge.JudgeDataReviewResult;
import endorphins.april.core.judge.JudgeResult;
import endorphins.april.core.judge.JudgeState;
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
    private final String expr;

    /**
     * 持续时间长度
     * 5m
     */
    private final String forInterval;

    /**
     * 每次评估间隔
     * 1m
     */
    private final String evaluationInterval;

    /**
     * 为 告警追加标签
     */
    private final LabelSet labels;

    /**
     * 为 告警追加的信息
     * severity: page
     * description: 描述
     */
    private final LabelSet annotations;

    /**
     * 是否在返回结果中追加判定的数据
     *
     * @return
     */
    private final boolean appendJudgeData;

    /**
     * 是否需要 检查数据的有效性
     */
    private final boolean needReviewJudgeData;

    @Override
    public LabelSet getAnnotations() {
        return annotations;
    }

    @Override
    public LabelSet getLabels() {
        return labels;
    }

    @Override
    public JudgeResult isAbnormal(JudgeData judgeData) {
//        JudgeDataReviewResult judgeDataReviewResult = this.reviewJudgeData(judgeData);
//
//        // 如果数量不足，则不进行判定
//        if (judgeDataReviewResult.isDataLack()) {
//            return JudgeResult.builder()
//                    .labelSets(judgeData.getLabelSet().merge(this.getLabels()))
//                    .annotations(this.getAnnotations()
//                            .put(ConstantKey.VALID_DATA_SIZE, String.valueOf(judgeDataReviewResult.validDataSize))
//                            .put(ConstantKey.ESTIMATE_DATA_SIZE, String.valueOf(judgeDataReviewResult.estimateDataSize))
//                    )
//                    .startTime(startTime)
//                    .endTime(endTime)
//                    .state(JudgeState.data_lack)
//                    .build();
//        }
        JudgeResult.JudgeResultBuilder result = JudgeResult.builder()
                .labelSets(judgeData.getLabelSet().merge(this.getLabels()))
                .annotations(this.getAnnotations())
                .times(judgeData.getTimes())
                .values(judgeData.getValues());

        JudgeState state = buildResultState(judgeData);

        result.state(state);

        if (this.appendJudgeData()) {
            result.values(judgeData.getValues())
                    .times(judgeData.getTimes());
        }

        return result.build();
    }

    /**
     * 构建 判定状态
     * @param judgeData
     * @return
     */
    private JudgeState buildResultState(JudgeData judgeData) {
        long evaluationIntervalSec = TimeIntervalUtils.getSeconds(evaluationInterval);
        long forIntervalSec = TimeIntervalUtils.getSeconds(forInterval);
        long estimateSize = evaluationIntervalSec / forIntervalSec;
        Long validDataSize = judgeData.getValidDataSize();
        // 返回判定结果
        if (estimateSize >= validDataSize) {
            return JudgeState.abnormal;
        } else {
            return JudgeState.normal;
        }
    }

    @Override
    public boolean appendJudgeData() {
        return appendJudgeData;
    }

    @Override
    @Deprecated
    public JudgeDataReviewResult reviewJudgeData(JudgeData judgeData) {
        if (needReviewJudgeData()) {
            return JudgeDataReviewResult.builder()
                    .result(JudgeDataReviewResult.Result.data_not_review)
                    .build();
        }
        long evaluationIntervalSec = TimeIntervalUtils.getSeconds(evaluationInterval);
        long forIntervalSec = TimeIntervalUtils.getSeconds(forInterval);
        long estimateSize = evaluationIntervalSec / forIntervalSec;
        Long validDataSize = judgeData.getValidDataSize();
        JudgeDataReviewResult.Result reviewResult;
        if (estimateSize >= validDataSize) {
            reviewResult = JudgeDataReviewResult.Result.data_enough;
        } else {
            reviewResult = JudgeDataReviewResult.Result.data_lack;
        }
        return JudgeDataReviewResult.builder()
                .result(reviewResult)
                .estimateDataSize(estimateSize)
                .validDataSize(validDataSize)
                .build();
    }

    @Override
    public boolean dataLackNotify() {
        return false;
    }

    @Override
    public boolean needReviewJudgeData() {
        return needReviewJudgeData;
    }
}
