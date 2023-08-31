package endorphins.april.service.valueconverter;

import org.springframework.data.elasticsearch.core.mapping.PropertyValueConverter;

import endorphins.april.service.severity.SeverityHelper;

/**
 * @author timothy
 * @DateTime: 2023/8/31 14:35
 **/
public class SeverityValueConverter implements PropertyValueConverter {
    @Override
    public Object write(Object value) {
        return value;
    }

    @Override
    public Object read(Object value) {
        Integer severityCode = (Integer) value;
        return SeverityHelper.getSeverityByCode(severityCode);
    }
}
