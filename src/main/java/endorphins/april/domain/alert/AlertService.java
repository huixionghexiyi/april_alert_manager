package endorphins.april.domain.alert;

import endorphins.april.api.v2.Alert;

import java.util.List;

/**
 * @author timothy
 * @data 2023年02月18日 21:56
 */
public interface AlertService {
    /**
     * 处理告警
     * @param alerts
     * @return
     */
    boolean alertHandler(List<Alert> alerts);
}
