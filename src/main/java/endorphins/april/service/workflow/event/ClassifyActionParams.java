package endorphins.april.service.workflow.event;

import java.util.List;

import endorphins.april.service.workflow.ActionParams;
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
