package endorphins.april.application.prometheus;

import endorphins.april.application.judge.timeWindow.TimeWindowJudgeRule;
import endorphins.april.core.dataSource.DataSource;
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

    /**
     * 默认不需要查询数据, 则需要用户自己传递查询到到数据
     */
    private boolean needSelectData = false;

    private JudgeData judgeData;

    private PrometheusJudgeRule judgeRule;

    private List<PrometheusInhibit> inhibits;

    private List<PrometheusSilence> silences;

    private List<PrometheusNotification> notifications;

    private DataSource dataSource;

    private TimeWindowJudgeWay judgeWay;

    @Override
    public JudgeRule getJudgeRule() {
        return this.judgeRule;
    }

    @Override
    public DataSource getDataSource() {
        return this.dataSource;
    }

    @Override
    public boolean needSelectData() {
        return this.needSelectData;
    }

    @Override
    public JudgeData getJudgeData() {
        if (needSelectData()) {
            this.judgeData = this.getDataSource().getJudgeDataByJudgeRule(this.judgeRule);
        }
        return this.judgeData;
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
