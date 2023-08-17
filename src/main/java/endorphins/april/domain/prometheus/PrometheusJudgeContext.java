package endorphins.april.domain.prometheus;

import endorphins.april.domain.judge.JudgeRule;
import endorphins.april.domain.judge.JudgeWay;
import endorphins.april.domain.timeWindow.TimeWindowJudgeWay;
import endorphins.april.domain.timeWindow.TimeWindowJudgeRule;
import endorphins.april.domain.judge.JudgeDataListFactory;
import endorphins.april.domain.judge.JudgeContext;
import endorphins.april.domain.judge.JudgeData;
import endorphins.april.domain.notify.Notification;
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
