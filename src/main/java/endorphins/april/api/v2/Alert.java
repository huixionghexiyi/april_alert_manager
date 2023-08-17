package endorphins.april.api.v2;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;

/**
 * @author timothy.yang cloudwise
 * @since 2023-02-17 09:28
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Alert implements Serializable {

    private String generatorURL;
    private Map<String, Object> labels;

    private Map<String, String> annotations;
    private Date startsAt;
    private Date endsAt;
}
