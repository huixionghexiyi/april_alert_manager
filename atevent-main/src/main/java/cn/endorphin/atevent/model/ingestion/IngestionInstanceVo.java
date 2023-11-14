package cn.endorphin.atevent.model.ingestion;

import cn.endorphin.atevent.model.mapping.IngestionConfig;
import lombok.Data;

/**
 * 摄取器instance vo
 * @author timothy
 * @DateTime: 2023/9/17 20:47
 **/
@Data
public class IngestionInstanceVo {

    private String name;

    private String description;

    private IngestionInstanceStatus status;

    /**
     * 摄取器id
     */
    private String sourceId;

    private IngestionSourceType sourceType;

    private IngestionDataScaleType scaleType;

    private IngestionConfig config;

    private String createdBy;

    private String updatedBy;

    private Long createTime;

    private Long updateTime;
}
