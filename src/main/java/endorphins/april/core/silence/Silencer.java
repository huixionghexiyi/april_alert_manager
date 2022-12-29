package endorphins.april.core.silence;

import endorphins.april.core.label.LabelSet;

/**
 * @author timothy
 * @data 2022年12月18日 17:40
 */
public interface Silencer {
    boolean mutes(LabelSet labelSet);
}
