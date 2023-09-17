package endorphins.april.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import endorphins.april.model.Event;
import endorphins.april.model.PostStatus;
import endorphins.april.service.Integration.IngestionService;
import lombok.extern.slf4j.Slf4j;

import com.google.common.collect.Lists;

/**
 * <描述>
 *
 * @author timothy.yang cloudwise
 * @since 2022-12-27 20:37
 */
@RestController("apiV1")
@RequestMapping("/api/v1/ingestion")
@Slf4j
public class IngestionController {

    @Autowired
    private IngestionService ingestionService;

    @PostMapping("/custom/{ingestionId}")
    public boolean custom(@RequestHeader String apiKey, @PathVariable String ingestionId, @RequestBody Event event) {
        return ingestionService.custom(apiKey, ingestionId, Lists.newArrayList(event));
    }

    @PostMapping("/events")
    public boolean events(@RequestHeader String apiKey, @RequestBody List<Event> events) {
        ingestionService.events(apiKey, events);
        return true;
    }

    @PostMapping("/instance/status")
    public boolean status(@RequestHeader String apiKey, @RequestBody PostStatus status) {
        ingestionService.status(apiKey, status);
        return true;
    }

}