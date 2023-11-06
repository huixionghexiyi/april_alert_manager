package endorphins.april.model.alarm;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import lombok.Data;

/**
 * @author timothy
 * @DateTime: 2023/8/31 11:34
 **/
@Data
public class AlarmVo implements Serializable {

    private Long id;
    private int eventCount;
    private String kind;
    private String type;
    private String check;
    private String source;
    private String description;
    private String service;
    private String dedupeKey;
    private long firstEventTime;
    private long lastEventTime;
    private long lastStatusChangeTime;
    private String severity;
    private boolean inMaintenance;
    private String status;
    private Long createUserId;
    private Long updateUserId;
    private Map<String, String> tags;
    private List<Long> incidents;
    private long createTime;
    private long updateTime;
}
