package endorphins.april.domain.dataSource;

import endorphins.april.domain.judge.JudgeRule;
import endorphins.april.domain.judge.JudgeData;

import java.util.List;

/**
 * <描述>
 *
 * @author timothy.yang cloudwise
 * @since 2022-12-24 22:46
 */
public interface DataSource {

    List<JudgeData> getJudgeDataByJudgeRule(final long startTime, final long endTime, final JudgeRule judgeRule);
}
