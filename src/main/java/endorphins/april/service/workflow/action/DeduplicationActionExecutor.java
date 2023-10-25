package endorphins.april.service.workflow.action;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;

import endorphins.april.entity.Alarm;
import endorphins.april.service.workflow.WorkflowEvent;
import endorphins.april.service.workflow.action.params.DeduplicationActionParams;
import endorphins.april.service.workflow.consumer.WorkflowExecutorContext;
import lombok.Data;
import lombok.NoArgsConstructor;

import com.google.common.collect.Lists;

/**
 * 去重 action 作为每个 workflow 的最后一个 step
 *
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
        String dedupeKey = workflowEvent.getDeduplicationKey();
        if (StringUtils.isBlank(dedupeKey)) {
            // 如果数据没有传 就使用 action 自身的 去重字段
            Set<String> dedupeFields = params.getDedupeFields();
            List<Object> dedupeValue = Lists.newArrayList();
            for (String dedupeField : dedupeFields) {
                Object dedupeFieldValue = workflowEvent.getByFieldName(dedupeField);
                if (dedupeFieldValue instanceof Iterable) {
                    dedupeValue.add(DeduplicationHelper.LIST_JOINER.join((Iterable) dedupeFieldValue));
                } else if (ObjectUtils.isNotEmpty(dedupeFieldValue)) {
                    dedupeValue.add(dedupeFieldValue);
                }
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
            alarm.setLastEventTime(workflowEvent.getReceivedTime());
            // 追加一个 severity
            alarm.addSeverity(workflowEvent.getSeverity());
        } else {
            // 根据去重字段没有查到时，认为是新生成的 alarm
            alarm = Alarm.createByWorkflowEvent(workflowEvent, dedupeKey);
            // 根据字段聚合规则，设置聚合字段的值
        }
        context.getAlarmRepository().save(alarm);
    }
}