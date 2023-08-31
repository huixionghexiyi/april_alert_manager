package test;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Scope;

/**
 * @author timothy
 * @Description TODO
 * @DateTime: 2023/4/19 20:41
 **/
@Scope
public class Test {
    private static String s = "ss";
    public static void main(String[] args) {
        String name = s;
        name = "hhh";
        System.out.println(name);
        System.out.println(s);
    }
}
