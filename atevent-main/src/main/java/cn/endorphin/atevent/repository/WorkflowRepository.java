package cn.endorphin.atevent.repository;

import java.util.Optional;

import cn.endorphin.atevent.entity.Workflow;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * @author timothy
 * @DateTime: 2023/8/31 15:31
 **/
public interface WorkflowRepository
    extends CrudRepository<Workflow, String>, PagingAndSortingRepository<Workflow, String>,
    CustomizedWorkflowRepository {

    Optional<Workflow> findByTags(String tags);
}
