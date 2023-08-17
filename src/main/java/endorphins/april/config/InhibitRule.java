package endorphins.april.config;

import java.util.List;
import java.util.Map;

/**
 * <描述>
 *
 * @author timothy.yang cloudwise
 * @since 2023-02-17 17:56
 */
public class InhibitRule {
    private Map<String, String> sourceMatch;
    @Deprecated
    private Map<String, Regexp> matchRegexps;
    private List<Matcher> sourceMatchers;
    @Deprecated
    private Map<String, String> targetMatch;
    private List<String> equal;
}
