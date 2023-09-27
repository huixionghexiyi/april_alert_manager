package endorphins.april.model.ingestion;

import endorphins.april.entity.IngestionSourceType;
import endorphins.april.model.mapping.IngestionConfig;
import lombok.Data;

/**
 * @author timothy
 * @DateTime: 2023/9/17 20:47
 **/
@Data
public class IngestionInstanceVo {
    private String name;

    private String description;

    /**
     * 摄取器id
     */
    private String sourceId;

    private IngestionSourceType sourceType;

    private IngestionConfig config;

    private String apiKey;

    private String createdBy;

    private String updatedBy;

    private long createTime;
}
