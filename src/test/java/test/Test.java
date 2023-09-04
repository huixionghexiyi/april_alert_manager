package test;

import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.springframework.context.annotation.Scope;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

/**
 * @author timothy
 * @Description TODO
 * @DateTime: 2023/4/19 20:41
 **/
@Scope
public class Test {
    public static void main(String[] args) {
        List<String> randomStr = Lists.newArrayList();
        for (int i = 0; i < 10000; i++) {
            randomStr.add(UUID.randomUUID().toString());
        }
        Set<String> s1 = Sets.newHashSet();
        for (int i = 0; i < randomStr.size(); i++) {
            s1.add(randomStr.get(i));
        }
        Set<String> s2 = Sets.newHashSet();
        for (int i = randomStr.size() - 1; i >= 0; i--) {
            s2.add(randomStr.get(i));
        }

        List<String> r1 = Lists.newArrayList();
        for (String s : s1) {
            r1.add(s);
        }
        List<String> r2 = Lists.newArrayList();
        for (String s : s2) {
            r2.add(s);
        }

        for (int i = 0; i < r1.size(); i++) {
            if (r1.get(i).equals(r2.get(i))) {
                continue;
            } else {
                System.out.println("not equals: "+ i + ":" + r1.get(i) + " " + r2.get(i));
            }
        }
    }
}
