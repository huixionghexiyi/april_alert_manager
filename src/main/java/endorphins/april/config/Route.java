package endorphins.april.config;

import javafx.util.Duration;

import java.util.List;

/**
 * <描述>
 *
 * @author timothy.yang cloudwise
 * @since 2023-02-17 17:55
 */
public class Route {
    private String receiver;
    private List<String> groupBy;
    private boolean groupByAll;
    private List<Matcher> matchers;
    private List<String> muteTimeIntervals;
    private List<String> activeTimeIntervals;
    private boolean continued;
    private List<Route> routes;
    private Duration groupWait;
    private Duration groupInterval;
    private Duration repeatInterval;
}
