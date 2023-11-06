package de.asedem.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONObject;

public class RestResponse {

    private final JSONObject value;
    static final ObjectMapper mapper = new ObjectMapper();

    public RestResponse(JSONObject value) {
        this.value = value;
    }

    /**
     * Get the JSON value the is saved in the response
     *
     * @return the JSON value the is saved in the response
     */
    public JSONObject asRawValue() {
        return value;
    }

    /**
     * Get the JSON value the is saved in the response as a String
     *
     * @return the JSON value the is saved in the response as a String
     */
    public String asValueString() {
        return this.value.toString();
    }

    /**
     * Get the JSON value the is saved in the response as a java object
     *
     * @param targetClass The class of the java object
     * @param <T>         The class where the response should bhe mapped to
     * @return The java object if the response can be mapped
     * @throws JsonProcessingException if the response can't be mapped to the class
     */
    public <T> T asJavaObject(Class<T> targetClass) throws JsonProcessingException {

        return mapper.readValue(this.value.toString(), targetClass);
    }
}
