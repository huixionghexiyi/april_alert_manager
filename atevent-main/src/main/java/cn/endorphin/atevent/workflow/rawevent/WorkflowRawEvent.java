package cn.endorphin.atevent.workflow.rawevent;

import cn.endorphin.atevent.workflow.executor.WorkflowData;
import com.google.common.collect.Maps;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

/**
 * @author timothy
 * @DateTime: 2023/8/24 23:18
 **/
@Getter
@Slf4j
public class WorkflowRawEvent implements WorkflowData {

    /**
     * 接收到的事件数据
     */
    private Map<String, Object> sourceRawEvent;

    /**
     * 进行处理后的事件数据
     */
    private Map<String, Object> targetRawEvent;

    private String ingestionId;

    public WorkflowRawEvent(String ingestionId, Map<String, Object> sourceRawEvent) {
        this.ingestionId = ingestionId;
        this.sourceRawEvent = sourceRawEvent;
        this.targetRawEvent = Maps.newHashMap();
    }

}
