package org.activiti.cloud.services.query.qraphql.ws.util;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import org.junit.Test;


public class JsonConverterTest {

    @Test
    public void testToMapEmptyString() throws JsonParseException, JsonMappingException, IOException {
        // given
        String jsonStr = "";

        // when
        Map<String, Object> result = JsonConverter.toMap(jsonStr);

        // then
        assertThat(result).isEqualTo(Collections.emptyMap());

    }

    @Test
    public void testToMapNullValue() throws JsonParseException, JsonMappingException, IOException {
        // given
        String jsonStr = "{\"key\":null}";

        // when
        Map<String, Object> result = JsonConverter.toMap(jsonStr);

        // then
        assertThat(result).containsKey("key");
        assertThat(result).containsValue(null);
    }

    @Test
    public void testToJsonStringNullValues() throws JsonProcessingException {
        // given
        Map<String, Object> obj = new HashMap<String, Object>();

        obj.put("key", null);

        // when
        String result = JsonConverter.toJsonString(obj);

        // then
        assertThat(result).isEqualTo("{\"key\":null}");
    }

}
