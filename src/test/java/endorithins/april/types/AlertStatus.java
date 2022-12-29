package endorithins.april.types;

import lombok.Data;

/**
 * <描述>
 *
 * @author timothy.yang cloudwise
 * @since 2022-09-27 16:31
 */
@Data
public class AlertStatus {
    private String state;
    private String[] silencedBy;
    private String[] inhibitedBy;

    // 未执行
    public static final String UNPROCESSED = "unprocessed";
    // 有效
    public static final String ACTIVE = "active";
    // 被抑制
    public static final String SUPPRESSED = "suppressed";
}
