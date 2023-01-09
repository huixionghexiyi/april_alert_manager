package endorphins.april.application.judge.timeWindow;

import endorphins.april.application.alert.Alert;
import endorphins.april.core.judge.JudgeData;
import endorphins.april.core.judge.JudgeResult;
import endorphins.april.core.judge.JudgeRule;
import endorphins.april.core.judge.JudgeWay;
import endorphins.april.core.judge.JudgeContext;

import java.util.List;

/**
 * 时间窗口判定
 * 一段时间内的数据全部满足阈值情况，则触发告警
 *
 * @author timothy.yang cloudwise
 * @since 2022-12-24 12:22
 */
public class TimeWindowJudgeWay implements JudgeWay<TimeWindowJudgeRule> {

    /**
     * 对一个序列的一批数据进行判定，返回一个 JudgeResult 的判定结果
     *
     * @param judgeRule
     * @param judgeData
     * @return
     */
    @Override
    public JudgeResult judge(TimeWindowJudgeRule judgeRule, JudgeData judgeData) {
        return judgeRule.isAbnormal(judgeData);
    }

    @Override
    public void inhibit(JudgeContext judgeContext, JudgeResult judgeResult) {
        // TODO: 2022/12/28
    }

    @Override
    public void silence(JudgeContext judgeContext, JudgeResult judgeResult) {
        // TODO: 2022/12/28
    }

    /**
     * 告警通知
     *
     * @param judgeContext
     * @param alerts
     */
    @Override
    public void notification(JudgeContext judgeContext, List<Alert> alerts) {
        JudgeRule judgeRule = judgeContext.getJudgeRule();
    }
}
