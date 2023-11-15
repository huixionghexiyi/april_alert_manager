package cn.endorphin.atevent.workflow.executor;

import cn.endorphin.atevent.workflow.executor.ActionExecutor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author timothy
 * @DateTime: 2023/11/15 11:39
 **/
@Component
public class ActionExecutorManager {

    @Autowired
    Map<String, ActionExecutor> actionExecutors;

    public ActionExecutor getByName(String name) {
        return actionExecutors.get(name);
    }
}
