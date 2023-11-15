package cn.endorphin.atevent.utils;

import com.google.common.base.Joiner;

/**
 * @author timothy
 * @DateTime: 2023/9/1 23:53
 **/
public class JoinerHelper {
    public static final Joiner JOINER = com.google.common.base.Joiner.on("::");
    public static final Joiner LIST_JOINER = com.google.common.base.Joiner.on(",");
    public static final Joiner CONN_JOINER = com.google.common.base.Joiner.on("-");

}
