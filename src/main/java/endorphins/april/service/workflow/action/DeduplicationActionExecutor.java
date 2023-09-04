package endorphins.april.service.workflow.action;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;

import endorphins.april.entity.Alarm;
import endorphins.april.service.workflow.WorkflowEvent;
import endorphins.april.service.workflow.action.params.DeduplicationActionParams;
import endorphins.april.service.workflow.consumer.WorkflowExecutorContext;
import lombok.Data;
import lombok.NoArgsConstructor;

import com.google.common.collect.Lists;

/**
 * @author timothy
 * @DateTime: 2023/8/24 17:23
 **/
@Data
@NoArgsConstructor
public class DeduplicationActionExecutor implements ActionExecutor {

    private DeduplicationActionParams params;

    public DeduplicationActionExecutor(DeduplicationActionParams params) {
        this.params = params;
    }

    @Override
    public void execute(WorkflowExecutorContext context, WorkflowEvent workflowEvent) {
        // 根据去重字段查询历史 alarm
        String dedupeKey = workflowEvent.getInsideFieldsMap()
            .getOrDefault("deduplicationKey", "").toString();
        if (StringUtils.isBlank(dedupeKey)) {
            Set<String> dedupeFields = params.getDedupeFields();
            List<Object> dedupeValue = Lists.newArrayList();
            for (String dedupeField : dedupeFields) {
                dedupeValue.add(workflowEvent.getInsideFieldsMap().getOrDefault(dedupeField, ""));
            }
            dedupeKey = DeduplicationHelper.JOINER.join(dedupeValue);
        }

        Optional<Alarm> alarmOpt = context.getAlarmRepository().findByDedupeKey(dedupeKey);
        Alarm alarm = null;
        // 如果已经存在，则获取，否则创建
        if (alarmOpt.isPresent()) {
            alarm = alarmOpt.get();
            // 判断是否超时等操作
            // 根据字段聚合规则，设置聚合字段的值
            // 重新保存 alarm
            alarm.setEventCount(alarm.getEventCount() + 1);
            alarm.setLastEventTime(workflowEvent.getEventTime());
        } else {
            // 根据去重字段没有查到时，认为是新生成的 alarm
            alarm = Alarm.createByWorkflowEvent(workflowEvent);
            // 根据字段聚合规则，设置聚合字段的值
            context.getAlarmRepository().save(alarm);
        }
    }
}
