package endorphins.april.config;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 读取配置文件
 *
 * @author timothy.yang cloudwise
 * @since 2023-02-17 17:51
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Config {
    private Global globalConfig;
    private Route route;
    private List<InhibitRule> InhibitRules;
    private List<Receiver> receivers;
    private List<String> templates;
    @Deprecated
    private List<MuteTimeInterval> muteTimeIntervals;
    private List<TimeInterval> timeIntervals;
    private String original;
}
