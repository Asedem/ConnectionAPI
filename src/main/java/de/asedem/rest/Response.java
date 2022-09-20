package de.asedem.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONObject;

public class Response {

    private final JSONObject value;

    public Response(JSONObject value) {
        this.value = value;
    }

    public JSONObject asRawValue() {
        return value;
    }

    public String asValueString() {
        return this.value.toString();
    }

    public <T> JavaObjectHolder<T> asJavaObject(Class<T> targetClass) throws JsonProcessingException {

        return new JavaObjectHolder<>(new ObjectMapper().readValue(this.value.toString(), targetClass));
    }

    public record JavaObjectHolder<T>(T get) {
    }
}
