package rewardCalculationTest.common;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;


public class JsonUtils {

    private static final ObjectMapper mapper = new ObjectMapper();

    public static Object json(String json) {
        try {
            return mapper.readValue(json, Object.class);
        } catch (Exception e) {
            throw new RuntimeException("Ошибка при парсинге JSON: " + json, e);
        }
    }
}
