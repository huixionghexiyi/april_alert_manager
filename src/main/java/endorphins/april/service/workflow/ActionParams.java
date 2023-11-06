package endorphins.april.service.workflow;

import org.apache.commons.lang3.NotImplementedException;

import endorphins.april.service.workflow.event.ClassifyActionExecutor;
import endorphins.april.service.workflow.event.DeduplicationActionExecutor;
import endorphins.april.infrastructure.json.JsonUtils;
import endorphins.april.service.workflow.event.ClassifyActionParams;
import endorphins.april.service.workflow.event.DeduplicationActionParams;
import endorphins.april.service.workflow.rawevent.RawEventMappingActionExecutor;
import endorphins.april.service.workflow.rawevent.RawEventMappingActionParams;

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
        } else if(name.equals(RawEventMappingActionParams.name)) {
            return new RawEventMappingActionExecutor(JsonUtils.parse(params, RawEventMappingActionParams.class));
        }
        throw new NotImplementedException();
    }
}
