package endorithins.april.types;

import endorithins.april.model.FingerPrint;

import java.util.Map;

/**
 * <描述>
 *
 * @author timothy.yang cloudwise
 * @since 2022-09-27 20:01
 */
public class MemMaker implements Marker {

    private Map<FingerPrint, AlertStatus> m;

    @Override
    public void setActiveOrSilenced(FingerPrint alert, int version, String[] activeSilenceIDs, String[] pendingSilenceIDs) {

    }

    @Override
    public void setInhibited(FingerPrint alert, String... alertIDs) {

    }

    @Override
    public int count(String... alertState) {
        return 0;
    }

    @Override
    public AlertStatus status(FingerPrint alert) {
        return null;
    }

    @Override
    public void delete(FingerPrint alert) {

    }

    @Override
    public boolean unprocessed(FingerPrint alert) {
        return false;
    }

    @Override
    public boolean active(FingerPrint alert) {
        return false;
    }

    @Override
    public SilencedResult silenced(FingerPrint alert) {
        return null;
    }
}
