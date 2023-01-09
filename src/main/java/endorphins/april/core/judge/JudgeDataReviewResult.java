package endorphins.april.core.judge;

import lombok.Builder;

/**
 * 判定数据 检查的结果
 *
 * @author timothy.yang cloudwise
 * @since 2023-01-06 11:39
 */
@Builder
public class JudgeDataReviewResult {

    public final Result result;

    public final Long validDataSize;

    public final Long estimateDataSize;

    public boolean isDataLack() {
        return result == Result.data_lack;
    }

    public enum Result {
        data_lack, // 数据不足
        data_enough, // 数组充足
        data_not_review // 数据未检查
    }
}
