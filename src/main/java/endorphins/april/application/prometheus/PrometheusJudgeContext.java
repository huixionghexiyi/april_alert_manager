package endorphins.april.application.prometheus;

import endorphins.april.application.judge.timeWindow.TimeWindowJudgeRule;
import endorphins.april.core.judge.JudgeDataListFactory;
import endorphins.april.core.judge.JudgeWay;
import endorphins.april.core.judge.JudgeContext;
import endorphins.april.core.judge.JudgeData;
import endorphins.april.core.judge.JudgeRule;
import endorphins.april.application.judge.timeWindow.TimeWindowJudgeWay;
import endorphins.april.core.notify.Notification;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.util.List;

/**
 * <描述>
 *
 * @author timothy.yang cloudwise
 * @since 2022-12-24 19:03
 */
@AllArgsConstructor
@Builder
public class PrometheusJudgeContext implements JudgeContext {

    private JudgeDataListFactory judgeDataListFactory;

    private PrometheusJudgeRule judgeRule;

    private List<PrometheusInhibit> inhibits;

    private List<PrometheusSilence> silences;

    private List<PrometheusNotification> notifications;

    private TimeWindowJudgeWay judgeWay;

    @Override
    public JudgeRule getJudgeRule() {
        return this.judgeRule;
    }

    @Override
    public List<JudgeData> getJudgeDataList() {
        return judgeDataListFactory.getJudgeDataList(judgeRule);
    }

    @Override
    public List<PrometheusInhibit> getInhibits() {
        return inhibits;
    }

    @Override
    public List<PrometheusSilence> getSilences() {
        return silences;
    }

    @Override
    public List<? extends Notification> getNotifications() {
        return notifications;
    }

    @Override
    public JudgeWay<TimeWindowJudgeRule> getJudgeWay() {
        return this.judgeWay;
    }


}
