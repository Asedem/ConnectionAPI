package de.asedem.rest;

import de.asedem.rest.models.Results;
import de.asedem.rest.models.User;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URL;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

class RestRequestTest {

    @Test
    void testGetRequestGetsFeedback() throws IOException {

        JSONObject jsonObject = new RestRequest()
                .get(new URL("https://laby.net/api/search/names/Asedem"))
                .asRawValue();

        assertNotNull(jsonObject);
    }

    @Test
    void testGetRequestAsJavaObject() throws IOException {

        Results results = new RestRequest()
                .get(new URL("https://laby.net/api/search/names/Asedem"))
                .asJavaObject(Results.class)
                .get();

        Results expected = new Results(Collections
                .singletonList(new User("Asedem", "121a9207-cd9a-4717-ba06-bb96667492f1")));

        assertEquals(expected, results);
    }

    @Test
    void testGetRequestAsString() throws IOException {

        String string = new RestRequest()
                .get(new URL("https://laby.net/api/search/names/Asedem"))
                .asValueString();

        String expected = "{\"results\":[{\"user_name\":\"Asedem\",\"uuid\":\"121a9207-cd9a-4717-ba06-bb96667492f1\"}]}";

        assertEquals(expected, string);
    }
}
