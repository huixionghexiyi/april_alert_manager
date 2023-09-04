package endorphins.april.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import endorphins.april.entity.Event;
import endorphins.april.service.Integration.IntegrationService;
import lombok.extern.slf4j.Slf4j;

/**
 * <描述>
 *
 * @author timothy.yang cloudwise
 * @since 2022-12-27 20:37
 */
@RestController("apiV1")
@RequestMapping("/api/v1/integration")
@Slf4j
public class IntegrationApi {

    @Autowired
    private IntegrationService integrationService;

    @PostMapping("/events")
    public boolean events(@RequestHeader String apiKey, @RequestBody List<Event> events) {
        integrationService.events(apiKey, events);
        return true;
    }

}
