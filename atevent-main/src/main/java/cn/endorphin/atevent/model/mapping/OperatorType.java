package cn.endorphin.atevent.model.mapping;

/**
 * @author timothy
 * @DateTime: 2023/9/27 19:33
 **/
public enum OperatorType {
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
