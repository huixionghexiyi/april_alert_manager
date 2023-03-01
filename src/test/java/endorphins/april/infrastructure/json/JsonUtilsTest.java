package endorphins.april.infrastructure.json;

import com.google.common.collect.Maps;
import org.assertj.core.util.Sets;
import org.junit.jupiter.api.Test;

import java.util.Date;

class JsonUtilsTest {

    @Test
    public void test() {
        System.out.println(JsonUtils.toJSONString(Maps.asMap(Sets.set("nice"), (k) ->
                new Date()
        )));
        System.out.println(new Date());
    }

}