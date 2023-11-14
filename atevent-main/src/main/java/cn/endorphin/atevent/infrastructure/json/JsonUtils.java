package cn.endorphin.atevent.infrastructure.json;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

/**
 * <描述>
 *
 * @author timothy.yang cloudwise
 * @since 2022-12-16 12:01
 */
@Slf4j
public class JsonUtils {

    private static final ObjectMapper objectMapper = new ObjectMapper()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
            .configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false)
            .setDateFormat(new SimpleDateFormat("yyyy-dd-MM HH:mm:ss"))
            .findAndRegisterModules()
            .setSerializationInclusion(JsonInclude.Include.NON_NULL);

    public static String emptyJson = "{}";

    public static ObjectMapper getMapper() {
        return objectMapper;
    }

    public static String toJSONString(Object o) {
        String result = null;
        try {
            result = objectMapper.writeValueAsString(o);
        } catch (Exception e) {
            log.error("JsonUtils convert object {} to String error {}", o, e.getMessage());
        }
        return result;
    }

    public static <T> T parseByJavaType(String jsonString, Class<T> clazz, Class<?>... elementClasses) {
        T t = null;
        if (StringUtils.isNotBlank(jsonString)) {
            try {
                byte[] bytes = jsonString.getBytes(StandardCharsets.UTF_8);
                JavaType javaType = objectMapper.getTypeFactory().constructParametricType(clazz, elementClasses);
                t = objectMapper.readValue(bytes, javaType);
            } catch (IOException e) {
                log.error("JsonUtils parse string {} by javatype  to object error {}", jsonString, e.getMessage());
            }
        }
        return t;
    }

    public static <T> T parse(String jsonString, Class<T> clazz) {
        if (null == jsonString) {
            return null;
        }
        T t = null;
        try {
            t = objectMapper.readValue(jsonString, clazz);
        } catch (Exception e) {
            log.error("JsonUtils parse string {} to object error {}", jsonString, e.getMessage());
        }
        return t;
    }

    public static <T> T parse(String jsonString, TypeReference<T> typeReference) {
        if (null == jsonString) {
            return null;
        }
        T t = null;
        try {
            t = objectMapper.readValue(jsonString, typeReference);
        } catch (Exception e) {
            log.error("JsonUtils parse string {} to typeReference error {}", jsonString, e.getMessage());
        }
        return t;
    }

    public static Map convertValueToMap(JsonNode jsonNode) {
        return objectMapper.convertValue(jsonNode, Map.class);
    }

    public static Map convertValueToMap(Object obj) {
        return objectMapper.convertValue(obj, Map.class);
    }

    public static JsonNode getJsonNodeFromJson(String json) {
        JsonNode jsonNode = null;
        try {
            jsonNode = objectMapper.readTree(json);
        } catch (IOException e) {
            log.error("JsonUtils parse json {} to JsonNode error {}", json, e);
        }
        return jsonNode;
    }

    public static <T> List<T> parseList(String jsonString, Class<T> clazz) {
        if (jsonString == null) {
            return null;
        }
        List<T> t = null;
        try {
            t = objectMapper.readerForListOf(clazz).readValue(jsonString);
        } catch (IOException e) {
            log.error("JsonUtils parse string {} to object list error {}", jsonString, e.getMessage());
        }
        return t;
    }


    public static <T> List<T> parseList(String jsonString, TypeReference<List<T>> valueTypeRef) {
        List<T> t = null;
        try {
            t = objectMapper.readValue(jsonString, valueTypeRef);
        } catch (IOException e) {
            log.error("JsonUtils parse string {} to object list error {}", jsonString, e.getMessage());
        }
        return t;
    }

    public static <T> T fromJsonMap(Class<T> type, Map json) {
        T t = null;
        try {
            t = objectMapper.convertValue(json, type);
        } catch (Exception e) {
            log.error("JsonUtils parse Map {} to Class<T> type error {}", json, e.getMessage());
        }
        return t;
    }

    public static List convertValueToList(Object obj) {
        return objectMapper.convertValue(obj, List.class);
    }


    /**
     * 根据keys集合,提取json中的一部分组成新的json
     *
     * @param jsonStr
     * @param keys
     * @return
     */
    public static String extractPartFromJsonByKeys(String jsonStr, List<String> keys) {
        Map map = parse(jsonStr, Map.class);
        Map<String, Object> resultMap = Maps.newHashMap();
        if (CollectionUtils.isEmpty(keys)) {
            return emptyJson;
        }
        for (String key : keys) {
            if (map != null && map.get(key) != null) {
                resultMap.put(key, map.get(key));
            }
        }
        return JsonUtils.toJSONString(resultMap);
    }

    /**
     * 校验json是否为正确的json结构，如果入参是个null话，我们也认为是个正常的
     *
     * @param text 输入的文本
     * @return 是否为合法的json，true 为合法，false为不合法
     */
    public static boolean isValidJson(String text) {
        boolean isValid = true;
        if (StringUtils.isNotEmpty(text)) {
            try {
                objectMapper.readTree(text);
            } catch (Exception e) {
                log.error("json valid error,text=【{}】,error={}", text, e.getMessage());
                isValid = false;
            }
        }
        return isValid;
    }

    public static <T> T parseByTypeReference(String jsonString, TypeReference<T> valueTypeRef) {
        T t = null;
        if (StringUtils.isNotBlank(jsonString)) {
            try {
                byte[] bytes = jsonString.getBytes(StandardCharsets.UTF_8);
                t = objectMapper.readValue(bytes, valueTypeRef);
            } catch (IOException e) {
                log.error("JsonUtils parse string {} by typeReference to object error {}", jsonString, e.getMessage());
            }
        }
        return t;
    }
}
