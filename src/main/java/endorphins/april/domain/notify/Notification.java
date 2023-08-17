package endorphins.april.domain.notify;

import endorphins.april.domain.alert.Alert;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 通知信息
 *
 * @author timothy.yang cloudwise
 * @since 2022-12-13 22:56
 */
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Notification {

    /**
     * 通知人
     */
    private Notifier notifier;

    /**
     * 是否发送已解决
     */
    @Getter
    private boolean sendResolved;

    /**
     * 通知名
     */
    @Getter
    private String name;

    /**
     * 索引
     */
    @Getter
    private int idx;

    public boolean send(NotifyContext context, Alert... alerts) {
        return notifier.send(context, alerts);
    }
}
