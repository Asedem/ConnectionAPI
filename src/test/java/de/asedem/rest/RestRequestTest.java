package de.asedem.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import de.asedem.rest.models.Results;
import de.asedem.rest.models.User;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URL;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class RestRequestTest {

    @Test
    void testGetRequestGetsFeedbackSync() throws IOException {

        JSONObject jsonObject = new RestRequest()
                .getSync(new URL("https://laby.net/api/search/names/Asedem"))
                .asRawValue();

        assertNotNull(jsonObject);
    }

    @Test
    void testGetRequestAsJavaObjectSync() throws IOException {

        Results results = new RestRequest()
                .getSync(new URL("https://laby.net/api/search/names/Asedem"))
                .asJavaObject(Results.class)
                .get();

        Results expected = new Results(Collections
                .singletonList(new User("Asedem", "121a9207-cd9a-4717-ba06-bb96667492f1")));

        assertEquals(expected, results);
    }

    @Test
    void testGetRequestAsStringSync() throws IOException {

        String string = new RestRequest()
                .getSync(new URL("https://laby.net/api/search/names/Asedem"))
                .asValueString();

        String expected = "{\"results\":[{\"user_name\":\"Asedem\",\"uuid\":\"121a9207-cd9a-4717-ba06-bb96667492f1\"}]}";

        assertEquals(expected, string);
    }

    @Test
    void testGetRequestGetsFeedbackAsync() throws IOException {

        new RestRequest()
                .get(new URL("https://laby.net/api/search/names/Asedem"))
                .whenComplete((restRequest, throwable) ->
                        assertNotNull(restRequest.asRawValue()));
    }

    @Test
    void testGetRequestAsJavaObjectAsync() throws IOException {

        Results expected = new Results(Collections
                .singletonList(new User("Asedem", "121a9207-cd9a-4717-ba06-bb96667492f1")));

        new RestRequest()
                .get(new URL("https://laby.net/api/search/names/Asedem"))
                .whenComplete((restRequest, throwable) -> {
                    try {
                        assertEquals(expected, restRequest.asJavaObject(Results.class).get());
                    } catch (JsonProcessingException exception) {
                        exception.printStackTrace();
                    }
                });
    }

    @Test
    void testGetRequestAsStringAsync() throws IOException {

        String expected = "{\"results\":[{\"user_name\":\"Asedem\",\"uuid\":\"121a9207-cd9a-4717-ba06-bb96667492f1\"}]}";

        new RestRequest()
                .get(new URL("https://laby.net/api/search/names/Asedem"))
                .whenComplete((restRequest, throwable) -> {
                    assertEquals(expected, restRequest.asValueString());
                });
    }
}
