package endorphins.april.domain.model;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Dynamic;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.data.elasticsearch.annotations.ValueConverter;
import org.springframework.data.elasticsearch.annotations.WriteTypeHint;

import endorphins.april.domain.workflow.WorkflowStatus;
import endorphins.april.domain.workflow.WorkflowType;
import endorphins.april.domain.workflow.action.Action;
import endorphins.april.domain.workflow.converter.ActionPropertyValueConverter;
import endorphins.april.domain.workflow.trigger.Trigger;
import lombok.Builder;
import lombok.Data;

/**
 * 工作流
 *
 * @author timothy
 * @DateTime: 2023/8/24 16:31
 **/
@Data
@Builder
@Document(indexName = "workflow", writeTypeHint = WriteTypeHint.FALSE)
public class Workflow {

    @Id
    private String id;

    /**
     * 满足 trigger 条件才会执行该工作流
     */
    @Field(type = FieldType.Flattened)
    private Trigger trigger;

    /**
     * 优先级，1最高，依次降低
     */
    @Field(type = FieldType.Integer)
    private int priority;

    /**
     * 当前工作流执行的步骤
     */
    @Field(type = FieldType.Flattened)
    @ValueConverter(ActionPropertyValueConverter.class)
    private List<Action> steps;

    /**
     * 工作流类型
     */
    @Field(type = FieldType.Keyword)
    private WorkflowType type;

    @Field(type = FieldType.Keyword)
    private WorkflowStatus status;

    @Field(type = FieldType.Date)
    private long createTime;

    @Field(type = FieldType.Date)
    private long updateTime;

    @Field(type = FieldType.Keyword)
    private String description;
}
