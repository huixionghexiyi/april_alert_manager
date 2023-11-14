package cn.endorphin.atevent.service.valueconverter;

import org.springframework.data.elasticsearch.core.mapping.PropertyValueConverter;

/**
 * 用于处理告警级别的展示值的转换
 *
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
        return value;
    }
}
