package endorphins.april.domain.judge;

import endorphins.april.domain.label.LabelSet;
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

    private JudgeState state;

    private List<Double> values;

    private List<Long> times;

    public boolean isMute = false;

}
