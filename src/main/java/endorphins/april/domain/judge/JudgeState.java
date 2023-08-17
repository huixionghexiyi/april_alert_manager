package endorphins.april.domain.judge;

/**
 * 一次告警判定的状态
 */
public enum JudgeState {
    abnormal, // 异常
    normal, // 正常
    data_lack // 数据不足
}
