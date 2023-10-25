package endorphins.april.service.severity;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.google.common.collect.Maps;

/**
 * @author timothy
 * @DateTime: 2023/8/31 14:38
 **/
@Component
public class SeverityHelper {

    private static final Map<Integer, String> severityMap = Maps.newHashMap();

    public static final String UNKNOWN_SEVERITY = "unknown";

    @Value("${at.event.severity.definition:clear,warning,minor,major,critical}")
    public void setSeverityDefinition(List<String> severityList) {
        for (int i = 0; i < severityList.size(); i++) {
            severityMap.put(i, severityList.get(i));
        }
    }

    public static String getSeverityByCode(Integer code) {
        // TODO 在 alarm 展示时，使用映射
        return severityMap.getOrDefault(code, UNKNOWN_SEVERITY);
    }

}
