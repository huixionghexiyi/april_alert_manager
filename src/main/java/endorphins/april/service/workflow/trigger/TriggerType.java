package endorphins.april.service.workflow.trigger;

/**
 * @author timothy
 * @DateTime: 2023/8/24 16:36
 **/
public enum TriggerType {
    /**
     * rawEvent 被收集时触发
     */
    RAW_EVENT_COLLECT,
    /**
     * event 被创建时触发
     */
    EVENT_CREATED,
}
