package endorphins.april.core.judge;

import endorphins.april.core.label.LabelSet;

/**
 * @author timothy
 * @data 2022年12月24日 12:21
 */
public interface JudgeRule {

    /**
     * 获取 附加信息
     * @return
     */
    LabelSet getAnnotations();

    /**
     * 获取 告警标签
     * @return
     */
    LabelSet getLabels();

    /**
     * 获取判定数据的状态
     * @return
     */
    JudgeDataReviewResult reviewJudgeData(JudgeData judgeData);

    /**
     * 数据无效时，是否发送通知
     * @return
     */
    boolean dataLackNotify();

    /**
     * 判断某个值 是否异常
     * @return
     */
    JudgeResult isAbnormal(JudgeData judgeData);

    /**
     * 是否在告警结果中追加判定时的数据
     * @return
     */
    boolean appendJudgeData();
}
