package endorphins.april.domain.workflow.action.context;

import java.util.List;

import lombok.Data;

/**
 * @author timothy
 * @DateTime: 2023/8/29 15:51
 **/
@Data
public class ClassifyEventActionActionContext implements ActionContext {

    public static final String name = "classify";

    private List<String> classifyFields;

    @Override
    public String getName() {
        return name;
    }
}
