package endorphins.april.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import endorphins.april.entity.Workflow;

/**
 * @author timothy
 * @DateTime: 2023/8/31 15:31
 **/
public interface WorkflowRepository
    extends CrudRepository<Workflow, String>, PagingAndSortingRepository<Workflow, String>,
    CustomizedWorkflowRepository {

}
