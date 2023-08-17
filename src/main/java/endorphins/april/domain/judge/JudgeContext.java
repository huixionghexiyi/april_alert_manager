package endorphins.april.domain.judge;

import endorphins.april.domain.inhibit.Inhibit;
import endorphins.april.domain.silence.Silence;
import endorphins.april.domain.notify.Notification;

import java.util.List;

/**
 * <描述>
 *
 * @author timothy.yang cloudwise
 * @since 2022-12-24 11:11
 */
public interface JudgeContext {

    /**
     * 判定规则
     */
    JudgeRule getJudgeRule();

    List<JudgeData> getJudgeDataList();

    /**
     * 告警抑制列表
     */
    List<? extends Inhibit> getInhibits();

    /**
     * 获取告警沉默列表
     */
    List<? extends Silence> getSilences();

    List<? extends Notification> getNotifications();

    JudgeWay getJudgeWay();
}
