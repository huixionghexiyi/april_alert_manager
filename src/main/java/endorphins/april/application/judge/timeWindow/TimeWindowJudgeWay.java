package endorphins.april.application.judge.timeWindow;

import endorphins.april.core.judge.JudgeData;
import endorphins.april.core.judge.JudgeResult;
import endorphins.april.core.judge.JudgeWay;
import endorphins.april.core.judge.JudgeContext;
import endorphins.april.core.judge.JudgeRule;
import endorphins.april.core.judge.SortWay;
import endorphins.april.infrastructure.ConstantKey;

/**
 * 时间窗口判定
 * 一段时间内的数据全部满足阈值情况，则触发告警
 *
 * @author timothy.yang cloudwise
 * @since 2022-12-24 12:22
 */
public class TimeWindowJudgeWay implements JudgeWay<TimeWindowJudgeRule> {

    @Override
    public JudgeResult judge(TimeWindowJudgeRule judgeRule, JudgeData judgeData) {

        long startTime = 0;
        long endTime = 0;
        if (judgeData.getSortWay() == SortWay.asc) {
            startTime = judgeData.getTimes().get(0);
            endTime = judgeData.getTimes().get(judgeData.getTimes().size() - 1);
        } else {
            startTime = judgeData.getTimes().get(judgeData.getTimes().size() - 1);
            endTime = judgeData.getTimes().get(0);
        }

        long minSize = judgeRule.expectationValueSizeRange()[0];
        // 如果数量不足，则不进行判定

        if (minSize == 0) {
            return JudgeResult.builder()
                    .labelSets(judgeData.getLabelSet().merge(judgeRule.getLabels()))
                    .annotations(judgeRule.getAnnotations()
                            .put(ConstantKey.DATA_SIZE, String.valueOf(minSize))
                    )
                    .startTime(startTime)
                    .endTime(endTime)
                    .status(JudgeResult.JudgeStatus.data_invalid)
                    .build();
        }

        if (minSize < judgeData.getSize()) {
            return JudgeResult.builder()
                    .labelSets(judgeData.getLabelSet().merge(judgeRule.getLabels()))
                    .annotations(judgeRule.getAnnotations()
                            .put(ConstantKey.DATA_SIZE, String.valueOf(minSize))
                    )
                    .startTime(startTime)
                    .endTime(endTime)
                    .status(JudgeResult.JudgeStatus.data_lack)
                    .build();
        }

        // 数量充足，开始进行判定
        boolean isAbnormal = judgeRule.isAbnormal(judgeData);

        JudgeResult.JudgeResultBuilder result = JudgeResult.builder()
                .labelSets(judgeData.getLabelSet().merge(judgeRule.getLabels()))
                .startTime(startTime)
                .endTime(endTime)
                .annotations(judgeRule.getAnnotations())
                .times(judgeData.getTimes())
                .values(judgeData.getValues());
        // 返回判定结果
        if (isAbnormal) {
            result.status(JudgeResult.JudgeStatus.abnormal);
        } else {
            result.status(JudgeResult.JudgeStatus.normal)
                    .build();
        }

        if (judgeRule.appendJudgeData()) {
            result.values(judgeData.getValues())
                    .times(judgeData.getTimes());
        }
        return result.build();
    }

    @Override
    public void inhibit(JudgeContext judgeContext, JudgeResult judgeResult) {
        // TODO: 2022/12/28
    }

    @Override
    public void silence(JudgeContext judgeContext, JudgeResult judgeResult) {
        // TODO: 2022/12/28
    }


    @Override
    public void notification(JudgeContext judgeContext, JudgeResult judgeResult) {
        // TODO: 2022/12/28
    }
}
