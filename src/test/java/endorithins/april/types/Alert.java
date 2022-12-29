package endorithins.april.types;

import java.util.Map;

/**
 * <描述>
 *
 * @author timothy.yang cloudwise
 * @since 2022-09-27 20:30
 */
public class Alert {
    private Map<String, String> labels;
    private Map<String, String> annotations;
    // 时间范围，两端可选
    private long startAt;
    private long endAt;
    private String generatorURL;
    private long updatedAt;
    private boolean timeout;

    public static final String alertNameLabel = "alertname";
    public static final String exportedLabelPrefix = "exported_";
    public static final String metricNameLabel = "__name__";
    public static final String schemeLabel = "__scheme__";
    public static final String addressLabel = "__address__";
    public static final String metricsPathLabel = "__metrics_path__";
    public static final String scrapeIntervalLabel = "__scrape_interval__";
    public static final String scrapeTimeoutLabel = "__scrape_timeout__";
    public static final String reservedLabelPrefix = "__";
    public static final String metaLabelPrefix = "__meta_";
    public static final String alertResolved = "resolved";
    public static final String alertFiring = "firing";

    private String getName() {
        return labels.get(alertNameLabel);
    }

    private Alert merge(Alert alert) {
        return null;
    }

    private boolean resolved() {
        return this.resolvedAt(System.currentTimeMillis());
    }

    private boolean resolvedAt(long now) {
        return false;
    }

    private String status() {
        if (this.resolved()) {
            return alertResolved;
        }
        return alertFiring;
    }
}