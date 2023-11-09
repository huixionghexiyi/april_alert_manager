package endorphins.april.service.workflow.rawevent;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import endorphins.april.model.mapping.Conditional;
import endorphins.april.model.mapping.Conditional.Condition;
import endorphins.april.model.mapping.IngestionConfig;
import endorphins.april.model.mapping.MappingRule;
import endorphins.april.model.mapping.OperatorType;
import endorphins.april.service.workflow.ActionExecutor;
import endorphins.april.service.workflow.WorkflowExecutorContext;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;

import java.util.List;
import java.util.Map;

/**
 * 去重 action 作为每个 workflow 的最后一个 step
 *
 * @author timothy
 * @DateTime: 2023/8/24 17:23
 **/
@Data
@NoArgsConstructor
public class RawEventMappingActionExecutor implements ActionExecutor {

    private RawEventMappingActionParams params;

    public RawEventMappingActionExecutor(RawEventMappingActionParams params) {
        this.params = params;
    }

    @Override
    public void execute(WorkflowExecutorContext context, WorkflowRawEvent rawEvent) {
        IngestionConfig config = params.getConfig();
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
        } else {
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
            List<Boolean> conditionResult = Lists.newArrayListWithCapacity(conditional.getConditions().size());
            for (Condition condition : conditional.getConditions()) {
                if (sourceRawEvent.containsKey(condition.getSourceKey())) {
                    // TODO tags 中的参数
                    Object value = sourceRawEvent.get(condition.getSourceKey());
                    conditionResult.add(condition.checkValue(value));
                } else if (OperatorType.NOT_EXIST == condition.getOperator()) {
                    conditionResult.add(true);
                } else {
                    conditionResult.add(false);
                }
            }
            // 如果满足当前条件，则进行 mapping，并且退出当前 conditional
            if (conditional.checkValue(conditionResult)) {
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
                if (MapUtils.isNotEmpty(conditional.getConverter())) {
                    sourceValue = conditional.convertValue(sourceValue);
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