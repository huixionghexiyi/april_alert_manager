package cn.endorphin.atevent.service.workflow.event;

import cn.endorphin.atevent.infrastructure.thread.ThreadPoolManager;
import cn.endorphin.atevent.repository.AlarmRepository;
import cn.endorphin.atevent.repository.WorkflowRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * @author timothy
 * @DateTime: 2023/8/31 19:50
 **/
@Component
@AllArgsConstructor
@Slf4j
@Order(1)
public class EventConsumerManager implements ApplicationRunner {
    private EventQueueManager eventQueueManager;

    private ThreadPoolManager threadPoolManager;

    private WorkflowRepository workflowRepository;

    private AlarmRepository alarmRepository;

    @Override
    public void run(ApplicationArguments args) throws Exception {


    }
}
