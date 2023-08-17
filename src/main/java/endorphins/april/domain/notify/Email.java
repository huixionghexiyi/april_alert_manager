package endorphins.april.domain.notify;

import endorphins.april.domain.alert.Alert;

/**
 * email 通知
 *
 * @author timothy.yang cloudwise
 * @since 2022-12-18 23:00
 */
public class Email implements Notifier {

    @Override
    public boolean send(NotifyContext context, Alert... alerts) {
        return false;
    }
}