package endorphins.april.service.Integration;

import java.util.List;

import endorphins.april.entity.Event;

/**
 * @author timothy
 * @DateTime: 2023/8/22 16:56
 **/
public interface IntegrationService {

    boolean events(String apiKey, List<Event> event);
}
