package cn.endorphin.atevent.entity;

import java.io.Serializable;
import java.util.List;

import cn.endorphin.atevent.service.valueconverter.ActionPropertyValueConverter;
import cn.endorphin.atevent.workflow.Trigger;
import cn.endorphin.atevent.workflow.WorkflowStatus;
import cn.endorphin.atevent.workflow.WorkflowType;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Dynamic;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.data.elasticsearch.annotations.ValueConverter;
import org.springframework.data.elasticsearch.annotations.WriteTypeHint;

import cn.endorphin.atevent.workflow.Action;
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
@Document(indexName = "workflow", writeTypeHint = WriteTypeHint.FALSE, dynamic = Dynamic.STRICT)
public class Workflow implements Serializable {

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
     * 默认为 自定义 workflow
     */
    @Field(type = FieldType.Keyword, nullValue = "CUSTOM")
    private String tags;

    /**
     * 摄取器id
     */
    @Field(type = FieldType.Keyword)
    private String ingestionId;

    /**
     * 工作流类型
     */
    @Field(type = FieldType.Keyword)
    private WorkflowType type;

    @Field(type = FieldType.Keyword)
    private WorkflowStatus status;

    @Field(type = FieldType.Long)
    private Long tenantId;

    @Field(type = FieldType.Long)
    private Long createUserId;

    @Field(type = FieldType.Long)
    private Long updateUserId;

    @Field(type = FieldType.Date)
    private Long createTime;

    @Field(type = FieldType.Date)
    private Long updateTime;

    @Field(type = FieldType.Keyword)
    private String description;

    public static final String DEFAULT_TAG = "default";
    public static final String INGESTION_TAG = "ingestion";
}
