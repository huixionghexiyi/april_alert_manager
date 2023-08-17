package endorphins.april.domain.silence;

import endorphins.april.domain.label.LabelSet;

/**
 * @author timothy
 * @data 2022年12月18日 17:40
 */
public interface Silencer {
    boolean mutes(LabelSet labelSet);
}
