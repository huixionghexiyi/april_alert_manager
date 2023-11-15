package cn.endorphin.atevent.workflow.executor;

import cn.endorphin.atevent.entity.Alarm;
import cn.endorphin.atevent.infrastructure.json.JsonUtils;
import cn.endorphin.atevent.workflow.WorkflowExecutorContext;
import cn.endorphin.atevent.utils.JoinerHelper;
import cn.endorphin.atevent.workflow.event.WorkflowEvent;
import com.google.common.collect.Lists;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * 去重 action 作为每个 workflow 的最后一个 step
 *
 * @author timothy
 * @DateTime: 2023/8/24 17:23
 **/
@Data
@NoArgsConstructor
@Component(DeduplicationActionExecutor.name)
public class DeduplicationActionExecutor implements ActionExecutor<WorkflowExecutorContext, WorkflowEvent> {

    public static final String name = "deduplication";

    @Override
    public void execute(Object paramsStr, WorkflowExecutorContext context, WorkflowEvent workflowEvent) {
        DeduplicationActionParams params = JsonUtils.parse((String)paramsStr, DeduplicationActionParams.class);
        // 根据去重字段查询历史 alarm
        String dedupeKey = workflowEvent.getDeduplicationKey();
        if (StringUtils.isBlank(dedupeKey)) {
            // 如果数据没有传 就使用 action 自身的 去重字段
            Set<String> dedupeFields = params.getDedupeFields();
            List<Object> dedupeValue = Lists.newArrayList();
            for (String dedupeField : dedupeFields) {
                Object dedupeFieldValue = workflowEvent.getByFieldName(dedupeField);
                if (ObjectUtils.isNotEmpty(dedupeFieldValue)) {
                    dedupeValue.add(dedupeFieldValue);
                }
            }
            dedupeKey = JoinerHelper.JOINER.join(dedupeValue);
        }

        Optional<Alarm> alarmOpt = context.getAlarmRepository().findByDedupeKey(dedupeKey);
        Alarm alarm = null;
        // 如果已经存在，则获取并修改，否则创建
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