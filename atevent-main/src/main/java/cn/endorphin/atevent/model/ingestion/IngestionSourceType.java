package cn.endorphin.atevent.model.ingestion;

/**
 * 摄取器 类型
 * @author timothy
 * @DateTime: 2023/9/5 19:20
 **/
public enum IngestionSourceType {
    /**
     * zabbix
     */
    ZABBIX,
    /**
     * prometheus
     */
    PROMETHEUS,

    /**
     * Webhook
     */

    WEBHOOK

}
