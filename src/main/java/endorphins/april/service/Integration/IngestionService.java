package endorphins.april.service.Integration;

import java.util.ArrayList;
import java.util.List;

import endorphins.april.model.Event;
import endorphins.april.model.PostStatus;

/**
 * @author timothy
 * @DateTime: 2023/8/22 16:56
 **/
public interface IngestionService {

    boolean events(String apiKey, List<Event> event);

    boolean status(String apiKey, PostStatus status);

    boolean custom(String apiKey, String ingestionId, ArrayList<Event> events);
}
