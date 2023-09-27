package endorphins.april.entity;

import java.util.Map;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import endorphins.april.model.ingestion.IngestionInstanceStatus;
import lombok.Data;

/**
 * @author timothy
 * @DateTime: 2023/9/4 19:58
 **/
@Data
@Document(indexName = "ingestion_instance")
public class IngestionInstance {
    @Id
    private String id;

    @Field(type = FieldType.Keyword)
    private String name;

    @Field(type = FieldType.Keyword)
    private IngestionInstanceStatus status;

    @Field(type = FieldType.Keyword)
    private String description;

    /**
     * ingestion source id
     */
    @Field(type = FieldType.Keyword)
    private String sourceId;

    /**
     * ingestion source typ 和 ingestion source id 一一对应
     */
    @Field(type = FieldType.Keyword)
    private IngestionSourceType sourceType;

    @Field(type = FieldType.Keyword)
    private String apiKey;

    @Field(type = FieldType.Keyword)
    private Map<String, String> mappings;

    @Field(type = FieldType.Long)
    private Long tenantId;

    @Field(type = FieldType.Long)
    private Long createUserId;

    @Field(type = FieldType.Long)
    private Long updateUserId;

    @Field(type = FieldType.Date)
    @CreatedDate
    private Long createTime;

    @Field(type = FieldType.Date)
    @LastModifiedDate
    private Long updateTime;
}
