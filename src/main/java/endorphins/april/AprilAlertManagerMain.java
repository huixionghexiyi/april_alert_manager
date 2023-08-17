package endorphins.april;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

/**
 * 告警管理
 *
 * @author timothy.yang cloudwise
 * @since 2022-12-26 22:10
 */
@SpringBootApplication
@EnableAspectJAutoProxy
public class AprilAlertManagerMain {
    public static void main(String[] args) {
        SpringApplication.run(AprilAlertManagerMain.class, args);
    }
}
