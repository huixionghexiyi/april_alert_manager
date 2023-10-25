package endorphins.april.entity;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.data.elasticsearch.annotations.ValueConverter;

import endorphins.april.model.ingestion.IngestionInstanceStatus;
import endorphins.april.model.ingestion.IngestionInstanceVo;
import endorphins.april.model.ingestion.IngestionSourceType;
import endorphins.april.model.mapping.IngestionConfig;
import endorphins.april.service.valueconverter.IngestionConfigValueConverter;
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

    @Field(type = FieldType.Flattened)
    @ValueConverter(IngestionConfigValueConverter.class)
    private IngestionConfig config;

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

    /**
     * 根据 vo 构建 instance
     * @param ingestionInstanceVo
     * @return
     */
    public static IngestionInstance createByVo(IngestionInstanceVo ingestionInstanceVo) {
        IngestionInstance instance = new IngestionInstance();
        instance.setName(ingestionInstanceVo.getName());
        instance.setDescription(ingestionInstanceVo.getDescription());
        instance.setSourceId(ingestionInstanceVo.getSourceId());
        instance.setSourceType(ingestionInstanceVo.getSourceType());
        instance.setTenantId(1L);
        instance.setConfig(ingestionInstanceVo.getConfig());

        Long createTime = ingestionInstanceVo.getCreateTime();
        if(createTime == null) {
            createTime = System.currentTimeMillis();
        }
        instance.setCreateTime(createTime);

        Long updateTime = ingestionInstanceVo.getUpdateTime();
        if (updateTime == null) {
            updateTime = System.currentTimeMillis();
        }
        instance.setUpdateTime(updateTime);
        instance.setStatus(ingestionInstanceVo.getStatus());
        return instance;
    }
}
