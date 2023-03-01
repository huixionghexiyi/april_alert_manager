package endorphins.april.application.synth;

import com.google.common.collect.Lists;
import endorphins.april.application.alert.Alert;
import endorphins.april.core.judge.JudgeContext;
import endorphins.april.core.judge.JudgeData;
import endorphins.april.core.judge.JudgeResult;
import endorphins.april.core.judge.JudgeRule;
import endorphins.april.core.judge.JudgeWay;
import lombok.Builder;

import java.util.List;

/**
 * 将各个功能组合起来
 *
 * @author timothy.yang cloudwise
 * @since 2022-12-24 18:36
 */
@Builder
public class Synth {


    private JudgeContext judgeContext;

    public void synth() {

        // 判定数据
        List<JudgeData> judgeDataList = judgeContext.getJudgeDataList();

        // 判定规则
        JudgeRule judgeRule = judgeContext.getJudgeRule();

        // 判定方式
        JudgeWay judgeWay = judgeContext.getJudgeWay();

        for (JudgeData judgeData : judgeDataList) {
            // 获取告警结果
            JudgeResult judgeResult = judgeWay.judge(judgeRule, judgeData);

            /**
             *  告警抑制
             *  由于其他更高优先级的告警触发，导致当前告警被抑制
             */
            judgeWay.inhibit(judgeContext, judgeResult);

            /**
             *  告警沉默
             *  由于某些条件，导致当前告警被禁用
             *  与告警抑制不同, 告警沉默是 通常是人为触发的
             *  例如：某些时间段，不需要触发该告警
             */
            judgeWay.silence(judgeContext, judgeResult);


            // TODO: 2022/12/31 构建告警信息
            List<Alert> alerts = Lists.newArrayList();
            /**
             * 告警通知
             */
            judgeWay.notification(judgeContext, alerts);
        }

    }
}
