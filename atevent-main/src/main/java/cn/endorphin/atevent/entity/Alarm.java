package cn.endorphin.atevent.entity;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import cn.endorphin.atevent.model.alarm.AlarmStatus;
import cn.endorphin.atevent.service.valueconverter.SeverityValueConverter;
import cn.endorphin.atevent.service.workflow.event.WorkflowEvent;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.data.elasticsearch.annotations.ValueConverter;
import org.springframework.data.elasticsearch.annotations.WriteTypeHint;

import lombok.Builder;
import lombok.Data;

import com.google.common.collect.Lists;

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
    private List<Integer> severity;

    /**
     * 是否在维护
     */
    @Field(type = FieldType.Boolean)
    private Boolean inMaintenance;

    /**
     * 警报状态
     */
    @Field(type = FieldType.Keyword)
    private List<AlarmStatus> statuses;

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

    public List<Integer> addSeverity(Integer severity) {
        List<Integer> severityList = this.getSeverity();
        if(severityList == null) {
            severityList = Lists.newArrayList();
        }
        severityList.add(severity);
        setSeverity(severityList);
        return severityList;
    }

    public static Alarm createByWorkflowEvent(WorkflowEvent workflowEvent, String dedupeKey) {
        long now = System.currentTimeMillis();
        Alarm alarm = Alarm.builder()
            .eventCount(1)
            .check(workflowEvent.getCheck())
            .createTime(now)
            .createUserId(workflowEvent.getUserId())
            .source(workflowEvent.getSource())
            .service(workflowEvent.getService())
            .description(workflowEvent.getDescription())
            .kind(workflowEvent.getKind())
            // TODO alarm 的 namespace 根据计算得到
            .namespace("")
            .tags(workflowEvent.getTags())
            .firstEventTime(workflowEvent.getReceivedTime())
            .lastEventTime(workflowEvent.getReceivedTime())
            .type(workflowEvent.getType())
            .tenantId(workflowEvent.getTenantId())
            .inMaintenance(false)
            .statuses(Lists.newArrayList(AlarmStatus.Open))
            .dedupeKey(dedupeKey)
            .build();
        alarm.addSeverity(workflowEvent.getSeverity());
        return alarm;
    }
}
