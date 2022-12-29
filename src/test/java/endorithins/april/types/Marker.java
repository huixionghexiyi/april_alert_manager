package endorithins.april.types;

import endorithins.april.model.FingerPrint;

/**
 * @author timothy
 * @data 2022年09月27日 16:38
 */
public interface Marker {
    void setActiveOrSilenced(FingerPrint alert, int version, String[] activeSilenceIDs, String[] pendingSilenceIDs);

    void setInhibited(FingerPrint alert, String... alertIDs);

    int count(String... alertState);

    AlertStatus status(FingerPrint alert);

    void delete(FingerPrint alert);

    boolean unprocessed(FingerPrint alert);

    boolean active(FingerPrint alert);

    SilencedResult silenced(FingerPrint alert);
}
