package endorphins.april.core.judge;

import endorphins.april.application.alert.Alert;

import java.util.List;

/**
 * 判定
 *
 * @author timothy.yang cloudwise
 * @since 2022-09-27 15:57
 */
public interface JudgeWay<T extends JudgeRule> {

    /**
     * 告警判定
     *
     * @param judgeRule
     * @param judgeData
     * @return
     */
    JudgeResult judge(T judgeRule, JudgeData judgeData);

    /**
     * 告警通知
     */
    void notification(JudgeContext judgeContext, List<Alert> alerts);

    /**
     * 告警抑制
     */
    void inhibit(JudgeContext judgeContext, JudgeResult judgeResult);

    /**
     * 告警静音
     */
    void silence(JudgeContext judgeContext, JudgeResult judgeResult);
}
