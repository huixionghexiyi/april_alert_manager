package endorphins.april.service.Integration;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import endorphins.april.model.Event;
import endorphins.april.model.ingestion.IngestionInstanceVo;
import endorphins.april.model.ingestion.PostStatus;

/**
 * @author timothy
 * @DateTime: 2023/8/22 16:56
 **/
public interface IngestionService {

    boolean events(String apiKey, List<Event> event);

    boolean status(PostStatus status);

    boolean custom(String apiKey, String ingestionId, Map<String, Object> rawEvent);

    /**
     * 创建 ingestion instance
     * @param ingestionInstanceVo
     * @return
     */
    boolean create(IngestionInstanceVo ingestionInstanceVo);
}
