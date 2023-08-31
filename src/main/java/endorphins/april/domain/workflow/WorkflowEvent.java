package endorphins.april.domain.workflow;

import java.util.Map;

import lombok.Data;

/**
 * @author timothy
 * @DateTime: 2023/8/24 23:18
 **/
@Data
public class WorkflowEvent {

    /**
     * 内部定义的字段
     */
    private Map<String, Object> insideFieldsMap;

    private Map<String, Object> tags;

    public WorkflowEvent(Map<String, Object> insideFieldsMap, Map<String, Object> tags){
        this.insideFieldsMap = insideFieldsMap;
        this.tags = tags;
    }
}
