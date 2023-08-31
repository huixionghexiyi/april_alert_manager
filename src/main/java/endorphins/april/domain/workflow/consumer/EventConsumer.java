package endorphins.april.domain.workflow.consumer;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.stereotype.Component;

import endorphins.april.domain.event.model.Event;
import endorphins.april.domain.model.Workflow;
import endorphins.april.domain.workflow.WorkflowContext;
import endorphins.april.domain.workflow.WorkflowEvent;
import endorphins.april.domain.workflow.WorkflowExecutor;
import endorphins.april.domain.workflow.queue.EventQueueManager;
import endorphins.april.infrastructure.thread.ThreadPoolManager;
import lombok.extern.slf4j.Slf4j;

import com.google.common.collect.Lists;

/**
 * @author timothy
 * @DateTime: 2023/8/24 17:32
 **/
@Component
@Slf4j
public class EventConsumer implements ApplicationRunner {

    @Autowired
    private EventQueueManager eventQueueManager;

    @Autowired
    private ThreadPoolManager threadPoolManager;

    @Autowired
    private ElasticsearchOperations elasticsearchOperations;

    /**
     * 工作流
     */
    List<Workflow> workflowList = Lists.newArrayList();

    @Override
    public void run(ApplicationArguments args) {
        SearchHits<Workflow> workflowSearchHits = elasticsearchOperations.search(Query.findAll(), Workflow.class);
        if (workflowSearchHits.hasSearchHits()) {
            workflowSearchHits.getSearchHits().forEach(
                workflow -> {
                    Workflow currWorkflow = workflow.getContent();
                    workflowList.add(currWorkflow);
                }
            );
        }

        threadPoolManager.getEventConsumerThreadPool()
            .execute(
                () -> {
                    while (true) {
                        try {
                            Event event = eventQueueManager.take();
                            Map<String, Object> insideFieldsMap = event.insideFieldsMap();
                            Map<String, Object> tags = event.getTags();
                            WorkflowEvent workflowEvent = new WorkflowEvent(insideFieldsMap, tags);
                            threadPoolManager.getRawEventThreadPool().execute(
                                new WorkflowExecutor(workflowEvent, workflowList, new WorkflowContext())
                            );
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                        }
                    }
                }
            );
    }
}
