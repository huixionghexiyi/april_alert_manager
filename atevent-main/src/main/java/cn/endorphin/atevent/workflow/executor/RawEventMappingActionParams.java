package cn.endorphin.atevent.workflow.executor;

import cn.endorphin.atevent.model.mapping.IngestionConfig;
import lombok.Getter;

/**
 * @author timothy
 * @DateTime: 2023/8/29 15:56
 **/
@Getter
public class RawEventMappingActionParams implements ActionParams {
    IngestionConfig ingestionConfig;
}
