package cn.endorphin.atevent.model.ingestion;

import lombok.Builder;
import lombok.Data;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.annotations.Setting;

import java.io.Serializable;

/**
 * @author timothy
 * @DateTime: 2023/11/16 16:05
 **/
@Builder
@Data
public class IngestionQueryParam implements Serializable {

    private String name;

    private IngestionInstanceStatus status;

    private IngestionSourceType sourceType;

    private String description;

    private String sortField;

    private Sort.Direction sortDirection;

}
