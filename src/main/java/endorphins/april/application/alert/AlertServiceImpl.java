package endorphins.april.application.alert;

import endorphins.april.core.models.Alert;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <描述>
 *
 * @author timothy.yang cloudwise
 * @since 2023-02-18 22:09
 */
@Service
public class AlertServiceImpl implements AlertService {
    /**
     * 处理告警
     *
     * @param alerts
     * @return
     */
    @Override
    public boolean alertHandler(List<Alert> alerts) {
        // 1. 获取全局超时时间
        // 2. 遍历 alerts
        // 2.1 设置 updatedAt
        // 2.2 判断 startsAt 和 endsAt 时间
        // 2.3 判断是 firing 还是 resolved
        // 3. 移除无效的告警
        // 4. 将告警添加到处理列表中
        return false;
    }
}
