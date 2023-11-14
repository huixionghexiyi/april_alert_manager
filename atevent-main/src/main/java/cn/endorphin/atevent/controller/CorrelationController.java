package cn.endorphin.atevent.controller;

import cn.endorphin.atevent.entity.Correlation;
import cn.endorphin.atevent.infrastructure.web.ResponseResult;
import cn.endorphin.atevent.service.correlation.CorrelationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author timothy
 * @DateTime: 2023/11/14 11:26
 **/
@RestController
@RequestMapping("v1/correlation")
@ResponseResult
public class CorrelationController {

    @Autowired
    private CorrelationService correlationService;

    @PostMapping
    public String create(@RequestBody Correlation correlation) {
        return correlationService.create(correlation);
    }
}
