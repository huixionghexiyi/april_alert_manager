package endorphins.april.service.workflow.rawevent;

import java.util.Map;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import com.google.common.collect.Maps;

/**
 * @author timothy
 * @DateTime: 2023/8/24 23:18
 **/
@Getter
@Slf4j
public class WorkflowRawEvent {

    /**
     * 接收到的事件数据
     */
    Map<String, Object> sourceRawEvent;

    /**
     * 进行处理后的事件数据
     */
    Map<String, Object> targetRawEvent;

    public WorkflowRawEvent(Map<String, Object> sourceRawEvent) {
        this.sourceRawEvent = sourceRawEvent;
        this.targetRawEvent = Maps.newHashMap();
    }

}
