package endorphins.april.core.judge;

import endorphins.april.core.label.LabelSet;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Objects;

/**
 * 判定数据
 *
 * @author timothy.yang cloudwise
 * @since 2022-12-24 11:09
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class JudgeData {

    private SortWay sortWay = SortWay.asc;

    private Long size;
    /**
     * 当前要判定的维度
     */
    private LabelSet labelSet;

    /**
     * 当前要判定的值
     */
    private List<Double> values;

    /**
     * 当前要判定的时间
     */
    private List<Long> times;

    public Long getSize() {
        if (size == null) {
            this.size = values.stream().filter(Objects::nonNull).count();
        }
        return size;
    }
}
