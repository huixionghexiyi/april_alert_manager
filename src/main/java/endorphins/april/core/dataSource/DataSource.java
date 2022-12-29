package endorphins.april.core.dataSource;

import endorphins.april.core.judge.JudgeData;
import endorphins.april.core.judge.JudgeRule;

/**
 * <描述>
 *
 * @author timothy.yang cloudwise
 * @since 2022-12-24 22:46
 */
public interface DataSource {

    JudgeData getJudgeDataByJudgeRule(JudgeRule judgeRule);
}
