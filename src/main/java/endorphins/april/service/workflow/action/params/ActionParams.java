package endorphins.april.service.workflow.action.params;

import org.apache.commons.lang3.NotImplementedException;

import endorphins.april.service.workflow.action.ActionExecutor;
import endorphins.april.service.workflow.action.ClassifyActionExecutor;
import endorphins.april.service.workflow.action.DeduplicationActionExecutor;
import endorphins.april.infrastructure.json.JsonUtils;

/**
 * @author timothy
 * @DateTime: 2023/8/29 15:51
 **/
public interface ActionParams {

    String getName();

    /**
     * 根据名称获取执行器
     * @param name
     * @param params
     * @return
     */
    static ActionExecutor getExecutorByName(String name, String params) {
        if (name.equals(ClassifyActionParams.name)) {
            return new ClassifyActionExecutor(JsonUtils.parse(params, ClassifyActionParams.class));
        } else if (name.equals(DeduplicationActionParams.name)) {
            return new DeduplicationActionExecutor(
                JsonUtils.parse(params, DeduplicationActionParams.class));
        }
        throw new NotImplementedException();
    }
}
