package cn.endorphin.atevent.service.Integration;

import java.util.List;
import java.util.Map;

import cn.endorphin.atevent.entity.IngestionInstance;
import cn.endorphin.atevent.model.Event;
import cn.endorphin.atevent.model.ingestion.IngestionInstanceStatus;
import cn.endorphin.atevent.model.ingestion.IngestionInstanceVo;
import cn.endorphin.atevent.model.ingestion.IngestionQueryParam;
import cn.endorphin.atevent.model.ingestion.PostStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchPage;

/**
 * @author timothy
 * @DateTime: 2023/8/22 16:56
 **/
public interface IngestionService {

    /**
     * 批量接收事件
     * @param apiKey
     * @param event
     * @return
     */
    boolean events(String apiKey, List<Event> event);

    /**
     * 修改 ingestion 的状态
     * @param status
     * @return
     */
    boolean status(PostStatus status);

    /**
     * 接收自定义的事件
     * @param apiKey
     * @param ingestionId
     * @param rawEvent
     * @return
     */
    boolean custom(String apiKey, String ingestionId, Map<String, Object> rawEvent);

    /**
     * 创建 ingestion instance
     * @param ingestionInstanceVo
     * @return
     */
    String create(IngestionInstanceVo ingestionInstanceVo);

    Page<IngestionInstance> list(IngestionQueryParam param, Integer page, Integer size);
}
