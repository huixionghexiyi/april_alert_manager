package cn.endorphin.atevent.entity;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.data.elasticsearch.annotations.WriteTypeHint;

import lombok.Data;

/**
 * @author timothy
 * @DateTime: 2023/8/31 19:31
 **/
@Data
@Document(indexName = "api_key", writeTypeHint = WriteTypeHint.FALSE)
public class ApiKey {
    @Id
    private String id;

    @Field(type = FieldType.Keyword)
    private String name;

    @Field(type = FieldType.Long)
    private Long tenantId;

    @Field(type = FieldType.Long)
    private Long createUserId;

    @Field(type = FieldType.Keyword)
    private String description;

    @Field(type = FieldType.Date)
    @CreatedDate
    private Long createTime;
}
