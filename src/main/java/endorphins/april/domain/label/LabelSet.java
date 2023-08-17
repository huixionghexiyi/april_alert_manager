package endorphins.april.domain.label;

import com.google.common.collect.Maps;
import endorphins.april.infrastructure.exception.ValidateException;
import lombok.Data;
import org.apache.commons.collections4.MapUtils;

import java.io.Serializable;
import java.util.Map;
import java.util.Set;

/**
 * <描述>
 *
 * @author timothy.yang cloudwise
 * @since 2022-12-16 11:31
 */
@Data
public class LabelSet implements Serializable, Cloneable {

    Map<String, String> entries;

    /**
     * 唯一确定一个序列
     */
    private int fingerprint;

    private static int emptyLabelSignature = 1;

    private LabelSet() {
        entries = Maps.newHashMap();
    }

    public static LabelSet create() {
        return new LabelSet();
    }

    public LabelSet put(String key, String value) {
        entries.put(key, value);
        return this;
    }

    public boolean addLabel(String key, String value) {
        entries.put(key, value);
        return true;
    }

    public void validate() throws ValidateException {
        // TODO: 2022/12/16
    }

    public boolean before(LabelSet target) {
        return false;
    }

    public LabelSet clone() throws CloneNotSupportedException {
        return (LabelSet) super.clone();
    }

    public LabelSet merge(LabelSet other) {
        // TODO: 2022/12/28
        return this;
    }

    public int fingerprint() {
        if (MapUtils.isEmpty(entries)) {
            this.fingerprint = emptyLabelSignature;
            return this.fingerprint;
        }

        Set<String> labelNames = entries.keySet();
        int fp = emptyLabelSignature;
        for (String labelName : labelNames) {
            fp ^= labelName.hashCode();
        }
        this.fingerprint = fp;
        return fingerprint;
    }

    public static LabelSet createByJson(String json) {
        return null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LabelSet labelSet = (LabelSet) o;
        return fingerprint() == labelSet.fingerprint();
    }

    @Override
    public int hashCode() {
        return fingerprint();
    }
}
