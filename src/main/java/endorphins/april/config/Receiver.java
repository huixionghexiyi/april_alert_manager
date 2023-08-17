package endorphins.april.config;

import java.util.List;

/**
 * <描述>
 *
 * @author timothy.yang cloudwise
 * @since 2023-02-17 17:56
 */
public class Receiver {
    private String name;
    private List<DiscordConfig> discordConfigs;
    private List<EmailConfig> emailConfigs;
    private List<String> templates;
}
