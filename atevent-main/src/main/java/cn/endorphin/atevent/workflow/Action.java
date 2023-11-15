package cn.endorphin.atevent.workflow;

import cn.endorphin.atevent.workflow.executor.ActionExecutor;
import org.springframework.data.annotation.Transient;

import lombok.Data;

/**
 * @author timothy
 * @DateTime: 2023/8/24 16:32
 **/
@Data
public class Action {

    private String name;

    private String params;

    @Transient
    private ActionExecutor executor;

    public Action(String name, String params) {
        this.name = name;
        this.params = params;
    }
}
