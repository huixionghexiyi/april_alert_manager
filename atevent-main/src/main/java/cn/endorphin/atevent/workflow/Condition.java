package cn.endorphin.atevent.workflow;

import lombok.Data;

import java.util.List;

/**
 * @author timothy
 * @DateTime: 2023/8/24 16:40
 **/
@Data
public class Condition {

    private String key;

    private String op;

    private String value;

    private List<String> list;

    public static final String EQ = "=";
    public static final String NOT_EQ = "!=";
    public static final String IN = "in";
    public static final String NOT_IN = "not in";
    public static final String CONTAINS = "contains";
    public static final String NOT_CONTAINS = "not contains";
    public static final String BEGINS_WITH = "begins with";
    public static final String NOT_BEGIN_WITH = "not begin with";
    public static final String END_WITH = "end with";
    public static final String NOT_END_WITH = "not end with";
    public static final String NOT_EXIST = "not exist";
    public static final String EXIST = "exist";

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
            case CONTAINS:
                return value.toString().contains(this.value);
            case NOT_CONTAINS:
                return !value.toString().contains(this.value);
            case BEGINS_WITH:
                return value.toString().startsWith(this.value);
            case NOT_BEGIN_WITH:
                return !value.toString().startsWith(this.value);
            case END_WITH:
                return value.toString().endsWith(this.value);
            case NOT_END_WITH:
                return !value.toString().endsWith(this.value);
            case EXIST:
                return value != null;
            default:
                throw new UnsupportedOperationException("Not Support operator[" + op + "]");
        }
    }

}
