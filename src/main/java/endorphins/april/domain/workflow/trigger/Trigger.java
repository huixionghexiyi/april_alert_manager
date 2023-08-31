package endorphins.april.domain.workflow.trigger;

import java.util.List;

import endorphins.april.domain.workflow.Term;
import lombok.Builder;
import lombok.Data;

/**
 * @author timothy
 * @DateTime: 2023/8/24 16:32
 **/
@Data
@Builder
public class Trigger {
    private TriggerType type;
    private List<Term> terms;
}
