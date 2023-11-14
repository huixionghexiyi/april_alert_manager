package cn.endorphin.atevent.model.ingestion;

import lombok.Data;

/**
 * @author timothy
 * @DateTime: 2023/9/5 17:54
 **/
@Data
public class PostStatus {

    private String ingestionInstanceId;

    private IngestionInstanceStatus status;

    private String apiKey;

}
