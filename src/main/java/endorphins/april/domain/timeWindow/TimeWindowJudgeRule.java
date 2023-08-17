package endorphins.april.domain.timeWindow;

import endorphins.april.domain.judge.JudgeRule;

/**
 * 时间窗口判定
 *
 * @author timothy.yang cloudwise
 * @since 2022-12-24 12:22
 */
public interface TimeWindowJudgeRule extends JudgeRule {

    /**
     * 是否需要检查 判定数据
     * @return
     */
    boolean needReviewJudgeData();
}
