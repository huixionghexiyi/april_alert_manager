package endorphins.april.service.workflow;

import java.util.List;

import org.apache.commons.lang3.NotImplementedException;

import lombok.Data;

/**
 * @author timothy
 * @DateTime: 2023/8/24 16:40
 **/
@Data
public class Term {

    private String key;

    private String op;

    private String value;

    private List<String> list;

    private static final String EQ = "=";
    private static final String NOT_EQ = "!=";
    private static final String IN = "in";
    private static final String NOT_IN = "not in";

    public boolean checkValue(Object value) {
        switch (op) {
            case EQ:
                return value.toString().equals(value);
            case NOT_EQ:
                return !value.toString().equals(value);
            case IN:
                return list.contains(value.toString());
            case NOT_IN:
                return !list.contains(value.toString());
            default:
                throw new NotImplementedException("暂不支持该操作符:" + op);
        }
    }

}
