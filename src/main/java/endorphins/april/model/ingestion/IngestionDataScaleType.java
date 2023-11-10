package endorphins.april.model.ingestion;

/**
 * 摄取器 数据规模
 *
 * @author timothy
 * @DateTime: 2023/11/10 09:50
 **/
public enum IngestionDataScaleType {
    /**
     * 基础，公用一个 queue 和一个 consumer
     */
    BASIC,
    /**
     * 使用独立的queue 和 独立的consumer
     */
    SINGLE_CONSUMER,

    /**
     * 使用独立的queue 和多个consumer
     */
    MULTI_CONSUMER;
}