package endorphins.april.service.workflow.rawevent;

import endorphins.april.model.mapping.IngestionConfig;
import endorphins.april.service.workflow.ActionParams;
import lombok.Getter;

/**
 * @author timothy
 * @DateTime: 2023/8/29 15:56
 **/
@Getter
public class RawEventMappingActionParams implements ActionParams {

    public static final String name = "ingestionMapping";

    IngestionConfig ingestionConfig;

    public RawEventMappingActionParams(IngestionConfig ingestionConfig) {
        this.ingestionConfig = ingestionConfig;
    }
    @Override
    public String getName() {
        return name;
    }
}
