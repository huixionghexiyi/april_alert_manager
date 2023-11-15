package cn.endorphin.atevent.workflow.executor;

import cn.endorphin.atevent.infrastructure.json.JsonUtils;
import cn.endorphin.atevent.model.mapping.Conditional;
import cn.endorphin.atevent.model.mapping.IngestionConfig;
import cn.endorphin.atevent.model.mapping.MappingRule;
import cn.endorphin.atevent.workflow.Condition;
import cn.endorphin.atevent.workflow.WorkflowExecutorContext;
import cn.endorphin.atevent.workflow.rawevent.WorkflowRawEvent;
import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import lombok.Data;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * 去重 action 作为每个 workflow 的最后一个 step
 *
 * @author timothy
 * @DateTime: 2023/8/24 17:23
 **/
@Data
@Component(RawEventMappingActionExecutor.name)
public class RawEventMappingActionExecutor implements ActionExecutor<WorkflowExecutorContext, WorkflowRawEvent> {

    public static final String name = "rawEventMapping";

    @Override
    public void execute(Object paramsStr, WorkflowExecutorContext context, WorkflowRawEvent rawEvent) {
        RawEventMappingActionParams params;
        if (paramsStr instanceof RawEventMappingActionParams) {
            params = (RawEventMappingActionParams) paramsStr;
        } else {
            params = JsonUtils.parse(paramsStr.toString(), RawEventMappingActionParams.class);
        }
        IngestionConfig config = params.getIngestionConfig();
        for (MappingRule mappingRule : config.getMappings()) {
            switch (mappingRule.getType()) {
                case BASIC:
                    basicMapping(rawEvent, mappingRule);
                    break;
                case CONDITIONAL:
                    conditionalMapping(rawEvent, mappingRule);
                    break;
                case CONCATENATE:
                    concatenateMapping(rawEvent, mappingRule);
                    break;
                default:
                    throw new NullPointerException("not found the mapping type[" + mappingRule.getType() + "]");
            }
        }
    }

    /**
     * 基本映射逻辑
     *
     * @param rawEvent
     * @param mappingRule
     */
    private void basicMapping(WorkflowRawEvent rawEvent, MappingRule mappingRule) {
        String targetKey = mappingRule.getTargetKey();
        Object targetValue = null;
        Map<String, Object> sourceRawEvent = rawEvent.getSourceRawEvent();
        Map<String, Object> targetRawEvent = rawEvent.getTargetRawEvent();
        for (String mappingSourceKey : mappingRule.getSourceKeys()) {

            if (sourceRawEvent.containsKey(mappingSourceKey)) {
                targetValue = sourceRawEvent.get(mappingSourceKey);
            }
        }
        if (targetValue != null) {
            targetRawEvent.put(targetKey, targetValue);
        } else if (StringUtils.isNotEmpty(mappingRule.getDefaultValue())) {
            targetRawEvent.put(targetKey, mappingRule.getDefaultValue());
        }
    }

    /**
     * 条件映射逻辑 满足第一个条件后，就不再进行判断
     *
     * @param rawEvent
     * @param mappingRule
     */
    private void conditionalMapping(WorkflowRawEvent rawEvent, MappingRule mappingRule) {
        List<Conditional> conditionalList = mappingRule.getConditional();
        Map<String, Object> sourceRawEvent = rawEvent.getSourceRawEvent();
        Map<String, Object> targetRawEvent = rawEvent.getTargetRawEvent();
        for (Conditional conditional : conditionalList) {
            // 第一个 conditional 满足，则break
            List<Condition> conditions = conditional.getConditions();
            boolean checkFinalResult = true;
            // 如果有条件，则进行条件判断
            if (CollectionUtils.isNotEmpty(conditions)) {
                List<Boolean> conditionResult = Lists.newArrayListWithCapacity(conditions.size());
                for (Condition condition : conditions) {
                    if (sourceRawEvent.containsKey(condition.getKey())) {
                        // TODO tags 中的参数
                        Object value = sourceRawEvent.get(condition.getKey());
                        conditionResult.add(condition.checkValue(value));
                    } else if (Condition.NOT_EXIST.equals(condition.getOp())) {
                        conditionResult.add(true);
                    } else {
                        conditionResult.add(false);
                    }
                }
                checkFinalResult = conditional.checkValue(conditionResult);
            }
            // 如果满足当前条件，则进行 mapping，并且退出当前 conditional
            if (checkFinalResult) {
                MappingRule mapping = conditional.getMapping();
                Object sourceValue = null;
                // TODO 这是一段公共逻辑，可以提取出来
                List<String> sourceKeys = mapping.getSourceKeys();
                if (CollectionUtils.isNotEmpty(sourceKeys)) {
                    for (String sourceKey : sourceKeys) {
                        sourceValue = sourceRawEvent.get(sourceKey);
                        if (sourceValue != null) {
                            break;
                        }
                    }
                }
                if (sourceValue == null) {
                    sourceValue = mapping.getDefaultValue();
                }
                if (MapUtils.isNotEmpty(mappingRule.getConverter())) {
                    sourceValue = mappingRule.convertValue(sourceValue);
                }
                targetRawEvent.put(mapping.getTargetKey(), sourceValue);
                break;
            }
        }
    }

    /**
     * 把多个字段链接起来
     *
     * @param rawEvent
     * @param mappingRule
     */
    private void concatenateMapping(WorkflowRawEvent rawEvent, MappingRule mappingRule) {
        String targetKey = mappingRule.getTargetKey();
        List<String> sourceKeys = mappingRule.getSourceKeys();
        List<String> sourceValues = Lists.newArrayListWithCapacity(sourceKeys.size());
        Map<String, Object> sourceRawEvent = rawEvent.getSourceRawEvent();
        Map<String, Object> targetRawEvent = rawEvent.getTargetRawEvent();
        for (String sourceKey : sourceKeys) {
            Object o = sourceRawEvent.get(sourceKey);
            if (o != null) {
                sourceValues.add(o.toString());
            }
        }
        String targetValue = Joiner.on(mappingRule.getDelimiter()).join(sourceValues);
        // TODO 这里是否也需要 converter
        targetRawEvent.put(targetKey, targetValue);
    }
}