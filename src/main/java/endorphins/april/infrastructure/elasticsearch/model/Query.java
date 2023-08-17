package endorphins.april.infrastructure.elasticsearch.model;

import lombok.Data;

/**
 * @author timothy
 * @DateTime: 2023/8/4 15:42
 **/
@Data
public class Query {
    private Integer currPage;
    private Integer pageSize;
    private String name;
}
