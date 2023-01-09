package endorphins.april.core.judge;

import endorphins.april.core.dataSource.DataSource;
import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * <描述>
 *
 * @author timothy.yang cloudwise
 * @since 2023-01-09 10:14
 */
@Builder
public class JudgeDataListFactory {

    private DataSource dataSource;

    private long startTime;

    private long endTime;

    private List<JudgeData> defaultJudgeDataList;

    public List<JudgeData> getJudgeDataList(JudgeRule judgeRule) {
        if (dataSource == null) {
            return defaultJudgeDataList;
        }
        return dataSource.getJudgeDataByJudgeRule(startTime, endTime, judgeRule);
    }
}
