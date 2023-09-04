package endorphins.april.entity;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.data.elasticsearch.annotations.ValueConverter;
import org.springframework.data.elasticsearch.annotations.WriteTypeHint;

import endorphins.april.service.valueconverter.SeverityValueConverter;
import endorphins.april.service.workflow.WorkflowEvent;
import lombok.Builder;
import lombok.Data;

/**
 * 严格模式下 需要将 writeTypeHint 设置为false
 *
 * @author timothy
 * @DateTime: 2023/8/17 17:43
 **/
@Data
@Builder
@Document(indexName = "alarm", writeTypeHint = WriteTypeHint.FALSE)
public class Alarm implements Serializable {
    @Id
    private String id;

    @Field(type = FieldType.Integer)
    private int eventCount;

    @Field(type = FieldType.Keyword)
    private String kind;

    @Field(type = FieldType.Keyword)
    private String type;

    @Field(type = FieldType.Keyword)
    private String check;

    @Field(type = FieldType.Keyword)
    private String source;

    @Field(type = FieldType.Keyword)
    private String description;

    @Field(type = FieldType.Keyword)
    private String service;

    @Field(type = FieldType.Keyword)

    private String dedupeKey;
    @Field(type = FieldType.Keyword)

    private Long firstEventTime;
    @Field(type = FieldType.Date)

    private Long lastEventTime;
    @Field(type = FieldType.Date)
    private Long lastStatusChangeTime;

    /**
     * 存储的值为 0 1 2 3 4 ...
     */
    @Field(type = FieldType.Integer)
    @ValueConverter(SeverityValueConverter.class)
    private String severity;

    /**
     * 是否在维护
     */
    @Field(type = FieldType.Boolean)
    private Boolean inMaintenance;

    /**
     * 警报状态
     */
    @Field(type = FieldType.Keyword)
    private AlarmStatus status;

    /**
     * 命名空间
     */
    @Field(type = FieldType.Keyword)
    private String namespace;

    @Field(type = FieldType.Long)
    private Long createUserId;

    @Field(type = FieldType.Long)
    private Long updateUserId;

    @Field(type = FieldType.Long)
    private Long tenantId;

    @Field(type = FieldType.Object)
    private Map<String, Object> tags;

    @Field(type = FieldType.Keyword)
    private List<Long> incidents;

    @Field(type = FieldType.Date)
    @CreatedDate
    private Long createTime;

    @Field(type = FieldType.Date)
    @LastModifiedDate
    private Long updateTime;

    public static Alarm createByWorkflowEvent(WorkflowEvent workflowEvent) {
        long now = System.currentTimeMillis();

        Map<String, Object> insideFieldsMap = workflowEvent.getInsideFieldsMap();
        return Alarm.builder()
            .eventCount(1)
            .check(insideFieldsMap.getOrDefault("check", "").toString())
            .createTime(now)
            .createUserId(workflowEvent.getUserId())
            .kind(insideFieldsMap.get("kind").toString())
            .source(insideFieldsMap.get("source").toString())
            .service(insideFieldsMap.get("service").toString())
            .description(insideFieldsMap.get("description").toString())
            .namespace(insideFieldsMap.get("namespace").toString())
            .tags(workflowEvent.getTags())
            .firstEventTime(now)
            .lastEventTime(now)
            .type(insideFieldsMap.get("type").toString())
            .tenantId(workflowEvent.getTenantId())
            .inMaintenance(false)
            .status(AlarmStatus.Open)
            .severity(insideFieldsMap.get("severity").toString())
            .dedupeKey(insideFieldsMap.get("dedupKey").toString())
            .build();
    }
}
