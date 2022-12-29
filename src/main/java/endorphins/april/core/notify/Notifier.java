package endorphins.april.core.notify;

import endorphins.april.application.alert.Alert;

/**
 * 通知者
 *
 * @author timothy
 * @data 2022年12月18日 22:09
 */
public interface Notifier {

    boolean send(NotifyContext context, Alert... alerts);
}
