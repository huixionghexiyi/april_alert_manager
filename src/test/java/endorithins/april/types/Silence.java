package endorithins.april.types;

import java.util.List;

/**
 * <描述>
 *
 * @author timothy.yang cloudwise
 * @since 2022-09-28 11:32
 */
public class Silence {
    private String id;
    private List<Matcher> matchers;

    private long startsAt;
    private long endsAt;
    private long updatedAt;
    private String createdBy;
    private String comment;
    private SilenceStatus status;

    private boolean expired() {
        return this.startsAt == this.endsAt;
    }

    private SilenceStatus calcSilenceState(long start, long end) {
        long curr = System.currentTimeMillis();
        if (curr < start) {
            return new SilenceStatus(SilenceStatus.silenceStatePending);
        }
        if (curr < end) {
            return new SilenceStatus(SilenceStatus.silenceStateActive);
        }
        return new SilenceStatus(SilenceStatus.silenceStateExpired);
    }
}
