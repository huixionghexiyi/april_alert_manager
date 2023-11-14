package cn.endorphin.atevent.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

/**
 * @author timothy
 * @DateTime: 2023/11/14 11:32
 **/
@Data
@Document(indexName = "correlation")
public class Correlation {
    @Id
    private String id;

    @Field(type = FieldType.Keyword)
    private String name;

    @Field(type = FieldType.Keyword)
    private String description;

    @Field(type = FieldType.Keyword)
    private Integer tenantId;

    @Field(type = FieldType.Keyword)
    private Integer userId;

    @Field(type = FieldType.Keyword)
    private String source;
}
