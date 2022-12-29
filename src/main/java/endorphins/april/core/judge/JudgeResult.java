package endorphins.april.core.judge;

import endorphins.april.core.label.LabelSet;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 判定结果
 *
 * @author timothy.yang cloudwise
 * @since 2022-12-19 23:07
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class JudgeResult {

    private LabelSet labelSets;

    /**
     * 附加的标签
     */
    private LabelSet annotations;

    private Long startTime;

    private Long endTime;

    private JudgeStatus status;

    private List<Double> values;

    private List<Long> times;

    public boolean isMute = false;

    public enum JudgeStatus {
        abnormal,
        normal,
        data_invalid, // 数据无效
        data_lack // 数据不足
    }
}
