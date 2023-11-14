package cn.endorphin.atevent.model.mapping;

/**
 * @author timothy
 * @DateTime: 2023/9/27 19:33
 **/
public enum OperatorType {
    EQUALS("equals"),
    CONTAINS("contains"),
    BEGINS_WITH("begins with"),
    ENDS_WITH("ends with"),
    NOT_EQUALS("not equals"),
    NOT_CONTAINS("not Contains"),
    NOT_BEGIN_WITH("not begin with"),
    NOT_END_WITH("not end with"),
    EXIST("exist"),
    NOT_EXIST("not exist"),
    AND("and"),
    OR("or");

    private String desc;

    public String getDesc() {
        return this.desc;
    }

    OperatorType(String desc) {
        this.desc = desc;
    }
}
