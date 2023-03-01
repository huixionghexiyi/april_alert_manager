package endorphins.april.application.alert;

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
    boolean alertHandler(List<endorphins.april.core.models.Alert> alerts);
}
