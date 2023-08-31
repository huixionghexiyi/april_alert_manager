package endorphins.april.domain.Integration;

import java.util.List;

import endorphins.april.domain.event.model.Event;

/**
 * @author timothy
 * @DateTime: 2023/8/22 16:56
 **/
public interface IntegrationService {

    boolean events(List<Event> event);
}
