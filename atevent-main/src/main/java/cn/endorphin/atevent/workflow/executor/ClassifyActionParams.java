package cn.endorphin.atevent.workflow.executor;

import lombok.Data;

import java.util.List;

/**
 * @author timothy
 * @DateTime: 2023/8/29 15:51
 **/
@Data
public class ClassifyActionParams implements ActionParams {

    private List<String> classifyFields;
}
