package endorphins.april.core.judge;

import endorphins.april.core.dataSource.DataSource;
import lombok.Builder;

import java.util.List;

/**
 * 生成数据的工厂
 *
 * @author timothy.yang cloudwise
 * @since 2023-01-09 10:14
 */
@Builder
public class JudgeDataListFactory {

    private final DataSource dataSource;

    private final long startTime;

    private final long endTime;

    private List<JudgeData> defaultJudgeDataList;

    public List<JudgeData> getJudgeDataList(JudgeRule judgeRule) {
        if (dataSource == null) {
            return defaultJudgeDataList;
        }
        return dataSource.getJudgeDataByJudgeRule(startTime, endTime, judgeRule);
    }
}
