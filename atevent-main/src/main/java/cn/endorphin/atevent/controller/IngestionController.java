package cn.endorphin.atevent.controller;

import cn.endorphin.atevent.entity.IngestionInstance;
import cn.endorphin.atevent.infrastructure.web.ResponseResult;
import cn.endorphin.atevent.infrastructure.web.Result;
import cn.endorphin.atevent.model.Event;
import cn.endorphin.atevent.model.ingestion.*;
import cn.endorphin.atevent.service.Integration.IngestionService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * <描述>
 *
 * @author timothy.yang cloudwise
 * @since 2022-12-27 20:37
 */
@RestController
@RequestMapping("/api/v1/ingestion")
@Slf4j
@ResponseResult
public class IngestionController {

    @Autowired
    private IngestionService ingestionService;

    @GetMapping("/list")
    public Page<IngestionInstance> list(@RequestParam(required = false) String name,
                                        @RequestParam(required = false) String description,
                                        @RequestParam(required = false) IngestionSourceType sourceType,
                                        @RequestParam(required = false) IngestionInstanceStatus status,
                                        @RequestParam(required = false) String sortField,
                                        @RequestParam(required = false) Sort.Direction sortType,
                                        @RequestParam(required = false) Integer page,
                                        @RequestParam(required = false) Integer size) {
        IngestionQueryParam params = IngestionQueryParam
                .builder()
                .name(name)
                .description(description)
                .sourceType(sourceType)
                .status(status)
                .sortField(sortField)
                .sortDirection(sortType)
                .build();
        return ingestionService.list(params, page, size);
    }

    @PostMapping("/custom/{ingestionId}")
    public boolean custom(@RequestHeader String apiKey, @PathVariable String ingestionId, @RequestBody Map<String, Object> rawEvent) {
        return ingestionService.custom(apiKey, ingestionId, rawEvent);
    }

    @PostMapping({"/events/{apiKey}", "/events"})
    public boolean events(@RequestHeader(value = "apiKey", required = false) String headerApiKey,
                          @PathVariable(value = "apiKey", required = false) String pathApiKey, @RequestBody List<Event> events) {
        String apiKey;
        if (StringUtils.isNotBlank(headerApiKey)) {
            apiKey = headerApiKey;
        } else {
            apiKey = pathApiKey;
        }
        ingestionService.events(apiKey, events);
        return true;
    }

    @PostMapping("/status")
    public boolean status(@RequestBody PostStatus status) {
        return ingestionService.status(status);

    }

    @PostMapping
    public Result<String> create(@RequestBody IngestionInstanceVo ingestionInstanceVo) {
        return Result.success(ingestionService.create(ingestionInstanceVo));
    }

}
