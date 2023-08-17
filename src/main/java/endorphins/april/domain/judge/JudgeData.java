package endorphins.april.domain.judge;

import endorphins.april.domain.label.LabelSet;
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

    private Long realSize;

    private Long validDataSize;
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

    /**
     * 获取有效的数据的长度
     *
     * @return
     */
    public Long getValidDataSize() {
        if (validDataSize == null) {
            this.validDataSize = values.stream().filter(Objects::nonNull).count();
        }
        return validDataSize;
    }

    public long getRealDataSize() {
        if (realSize == null) {
            this.realSize = (long) values.size();
        }
        return realSize;
    }
}
