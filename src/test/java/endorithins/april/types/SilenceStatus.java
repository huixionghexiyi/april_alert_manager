package endorithins.april.types;

import lombok.Data;

/**
 * <描述>
 *
 * @author timothy.yang cloudwise
 * @since 2022-09-27 20:28
 */
@Data
public class SilenceStatus {
    public static String silenceStateExpired = "expired";
    public static String silenceStateActive = "active";
    public static String silenceStatePending = "pending";
    private String state;

    public SilenceStatus(String state) {
        this.state = state;
    }
}
