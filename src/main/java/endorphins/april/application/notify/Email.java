package endorphins.april.application.notify;

import endorphins.april.application.alert.Alert;
import endorphins.april.core.notify.Notifier;
import endorphins.april.core.notify.NotifyContext;

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