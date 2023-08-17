package endorphins.april.api.v2;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.client.elc.NativeQueryBuilder;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.query.Criteria;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import endorphins.april.infrastructure.elasticsearch.entity.IncidentView;
import endorphins.april.infrastructure.elasticsearch.entity.IncidentView.ValidPage;
import endorphins.april.infrastructure.elasticsearch.model.Query;
import endorphins.april.infrastructure.json.JsonUtils;
import endorphins.april.domain.alert.AlertService;
import lombok.extern.slf4j.Slf4j;

/**
 * <描述>
 *
 * @author timothy.yang cloudwise
 * @since 2022-12-27 20:37
 */
@RestController("apiV2")
@RequestMapping("/api/v2")
@Slf4j
public class Api {
    @Autowired
    private AlertService alertService;

    /**
     * postAlertsHandler
     *
     * @param alerts
     * @return
     */
    @PostMapping("/alerts")
    public boolean everyThingPost(@RequestBody List<Alert> alerts) {
        log.info(JsonUtils.toJSONString(alerts));
        return alertService.alertHandler(alerts);
    }

    @GetMapping("/{path}")
    public Object everyThingGet(@PathVariable("path") String path, @RequestBody Object obj) {
        log.info(JsonUtils.toJSONString(obj));
        log.info("get path:{}", path);
        return obj;
    }

    @Autowired
    private ElasticsearchOperations operations;

    @Autowired
    private ElasticsearchRestTemplate restTemplate;

    @GetMapping("/es/{ops}")
    public IncidentView es(@PathVariable("ops") String path) throws IOException {
        IncidentView incidentView = new IncidentView();
        incidentView.setName("timothy");
        Query query = new Query();
        incidentView.setQuery(query);
        long now = System.currentTimeMillis();
        incidentView.setCreateTime(now);
        incidentView.setUpdateTime(now);
        ValidPage validPage = new ValidPage();
        validPage.setFrom(1);
        validPage.setTo(2);
        incidentView.setValidPage(validPage);
        IncidentView save = operations.save(incidentView);
        NativeQueryBuilder builder = new NativeQueryBuilder();
        new Criteria("").is("");
        SearchHit searchHit = restTemplate.get(path, SearchHit.class);
        return operations.get(save.getId(), IncidentView.class);
    }
}
