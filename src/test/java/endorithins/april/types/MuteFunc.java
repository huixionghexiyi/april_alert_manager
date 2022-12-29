package endorithins.april.types;

import java.util.Map;

/**
 * @author timothy
 * @data 2022年09月28日 11:31
 */
@FunctionalInterface
public interface MuteFunc {
    boolean invoke(Map<String, String> labels);
}
