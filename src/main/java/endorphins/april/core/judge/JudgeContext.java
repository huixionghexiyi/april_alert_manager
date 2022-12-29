package endorphins.april.core.judge;

import endorphins.april.core.dataSource.DataSource;
import endorphins.april.core.inhibit.Inhibit;
import endorphins.april.core.notify.Notification;
import endorphins.april.core.silence.Silence;

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

    DataSource getDataSource();

    boolean needSelectData();

    JudgeData getJudgeData();

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
