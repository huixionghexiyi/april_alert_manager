package endorphins.april.application.synth;

import endorphins.april.core.judge.JudgeContext;
import endorphins.april.core.judge.JudgeData;
import endorphins.april.core.judge.JudgeResult;
import endorphins.april.core.judge.JudgeRule;
import lombok.Builder;

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
        JudgeData judgeData = judgeContext.getJudgeData();

        // 判定规则
        JudgeRule judgeRule = judgeContext.getJudgeRule();

        // 获取告警结果
        JudgeResult judgeResult = judgeContext.getJudgeWay().judge(judgeRule, judgeData);

        /**
         *  告警抑制
         *  由于其他更高优先级的告警触发，导致当前告警被抑制
         */
        judgeContext.getJudgeWay().inhibit(judgeContext, judgeResult);

        /**
         *  告警沉默
         *  由于某些条件，导致当前告警被禁用
         *  与告警抑制不同, 告警沉默是 通常是人为触发的
         *  例如：某些时间段，不需要触发该告警
         */
        judgeContext.getJudgeWay().silence(judgeContext, judgeResult);

        /**
         * 告警通知
         */
        judgeContext.getJudgeWay().notification(judgeContext, judgeResult);
    }
}
