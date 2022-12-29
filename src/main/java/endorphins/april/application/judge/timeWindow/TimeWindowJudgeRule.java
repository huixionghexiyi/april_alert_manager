package endorphins.april.application.judge.timeWindow;

import endorphins.april.core.judge.JudgeRule;

/**
 * 时间窗口判定
 *
 * @author timothy.yang cloudwise
 * @since 2022-12-24 12:22
 */
public interface TimeWindowJudgeRule extends JudgeRule {

    /**
     * 预期有多少个值
     * 只有两个值:index=0 表示最少多少个;index=1 表示最多多少个
     * @return
     */
    long[] expectationValueSizeRange();
}
