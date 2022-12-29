package endorphins.april.application.prometheus;

import endorphins.april.core.inhibit.Inhibit;
import endorphins.april.core.label.LabelSet;

import java.util.List;

/**
 * <描述>
 *
 * @author timothy.yang cloudwise
 * @since 2022-12-24 19:05
 */
public class PrometheusInhibit implements Inhibit {

    /**
     * 如果匹配了这些 labels 的 alert 被触发，则抑制 targetMatch 中的告警源, 同时，需要满足 equal 中的标签相同
     */
    private LabelSet sourceMatch;

    /**
     *
     */
    private LabelSet targetMatch;

    private List<String> equal;

}
