package endorphins.april.service.workflow.action.params;

import java.util.List;

import lombok.Data;

/**
 * @author timothy
 * @DateTime: 2023/8/29 15:51
 **/
@Data
public class ClassifyActionParams implements ActionParams {

    public static final String name = "classify";

    private List<String> classifyFields;

    @Override
    public String getName() {
        return name;
    }
}
