package endorithins.april.synth;

import endorphins.april.core.judge.JudgeData;
import endorphins.april.application.prometheus.PrometheusJudgeContext;
import endorphins.april.application.prometheus.PrometheusJudgeRule;
import endorphins.april.application.judge.timeWindow.TimeWindowJudgeWay;
import endorphins.april.application.synth.Synth;
import endorphins.april.core.label.LabelSet;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Test;

/**
 * <描述>
 *
 * @author timothy.yang cloudwise
 * @since 2022-12-26 22:39
 */
public class PrometheusTest {
    @Test
    public void prometheus_test() {
        long currTime = System.currentTimeMillis();
        JudgeData judgeData = JudgeData.builder()
                .labelSet(LabelSet.create()
                        .put("target_name", "target1")
                        .put("deviceId", "1"))
                .times(Lists.newArrayList(currTime / 1000 * 1000, currTime / 1000 * 1000 - 60000))
                .values(Lists.newArrayList(1d, 1d))
                .build();
        Synth synth = Synth.builder()
                .judgeContext(
                        PrometheusJudgeContext.builder()
                                .judgeRule(
                                        PrometheusJudgeRule.builder()
                                                .annotations(
                                                        LabelSet.create()
                                                                .put("description", "{{ value }}")
                                                )
                                                .expr("up == 0")
                                                .timeRange("5m")
                                                .labels(LabelSet.create().put("severity", "disaster"))
                                                .build()
                                )
                                .needSelectData(false)
                                .judgeData(judgeData)
                                .judgeWay(new TimeWindowJudgeWay())
                                .inhibits(null)
                                .silences(null)
                                .notifications(null)
                                .build())
                .build();

        /**
         * 组合判定
         */
        synth.synth();
    }
}
