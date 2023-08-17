package endorphins.april.infrastructure.elasticsearch.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.data.elasticsearch.annotations.WriteTypeHint;

import endorphins.april.infrastructure.elasticsearch.model.Query;
import lombok.Data;

/**
 * @author timothy
 * @DateTime: 2023/8/4 15:26
 **/
@Document(indexName = "test_timothy", writeTypeHint = WriteTypeHint.FALSE)
@Data
public class IncidentView {
    @Id
    private String id;

    private String name;

    private Query query;

    @Field(type = FieldType.Integer_Range)
    private ValidPage validPage;

    private long createTime;

    private long updateTime;

    @Data
    public static class ValidPage {
        @Field(name = "gte")
        private Integer from;

        @Field(name = "lte")
        private Integer to;
    }
}