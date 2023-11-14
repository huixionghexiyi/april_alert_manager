package cn.endorphin.atevent.service.workflow;

import cn.endorphin.atevent.infrastructure.json.JsonUtils;
import cn.endorphin.atevent.model.mapping.IngestionConfig;
import cn.endorphin.atevent.service.workflow.event.ClassifyActionExecutor;
import cn.endorphin.atevent.service.workflow.event.ClassifyActionParams;
import cn.endorphin.atevent.service.workflow.rawevent.RawEventMappingActionParams;
import org.apache.commons.lang3.NotImplementedException;

import cn.endorphin.atevent.service.workflow.event.DeduplicationActionExecutor;
import cn.endorphin.atevent.service.workflow.event.DeduplicationActionParams;
import cn.endorphin.atevent.service.workflow.rawevent.RawEventMappingActionExecutor;

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
            IngestionConfig ingestionConfig = JsonUtils.parse(params, IngestionConfig.class);
            return new RawEventMappingActionExecutor(ingestionConfig);
        }
        throw new NotImplementedException();
    }
}
