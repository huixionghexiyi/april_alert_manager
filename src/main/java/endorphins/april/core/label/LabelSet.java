package endorphins.april.core.label;

import com.google.common.collect.Maps;
import endorphins.april.infrastructure.exception.ValidateException;

import java.util.Map;

/**
 * <描述>
 *
 * @author timothy.yang cloudwise
 * @since 2022-12-16 11:31
 */
public class LabelSet implements Cloneable {

    Map<String, String> entries;

    private int fingerprint;

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
        if (fingerprint == 0) {
            // TODO: 2022/12/24 构建 entries 的 hashCode, 并且复制给 fingerprint
        }
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
