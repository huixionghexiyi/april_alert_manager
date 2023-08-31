package endorphins.april.domain.workflow.action.context;

import org.apache.commons.lang3.NotImplementedException;

import endorphins.april.domain.workflow.action.ActionExecutor;
import endorphins.april.domain.workflow.action.ClassifyEventActionExecutor;
import endorphins.april.domain.workflow.action.DeduplicationEventExecutor;
import endorphins.april.infrastructure.json.JsonUtils;

/**
 * @author timothy
 * @DateTime: 2023/8/29 15:51
 **/
public interface ActionContext {

    String getName();

    /**
     * 根据名称获取执行器
     * @param name
     * @param context
     * @return
     */
    static ActionExecutor getExecutorByName(String name, String context) {
        if (name.equals(ClassifyEventActionActionContext.name)) {
            return new ClassifyEventActionExecutor(JsonUtils.parse(context, ClassifyEventActionActionContext.class));
        } else if (name.equals(DeduplicationEventActionActionContext.name)) {
            return new DeduplicationEventExecutor(
                JsonUtils.parse(context, DeduplicationEventActionActionContext.class));
        }
        throw new NotImplementedException();
    }
}
