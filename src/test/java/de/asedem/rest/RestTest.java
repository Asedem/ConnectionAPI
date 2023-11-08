package de.asedem.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import de.asedem.rest.models.Data;
import de.asedem.rest.models.Result;
import de.asedem.rest.models.Support;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URI;
import java.util.logging.Level;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class RestTest {

    private static Result expectedResult;
    private static String expectedString;
    private static final Logger logger = Logger.getLogger(RestTest.class.getName());

    @BeforeAll
    static void setup() {

        final Data expectedData = new Data(
                2,
                "janet.weaver@reqres.in",
                "Janet",
                "Weaver",
                "https://reqres.in/img/faces/2-image.jpg");

        final Support expectedSupport = new Support(
                "https://reqres.in/#support-heading",
                "To keep ReqRes free, contributions towards server costs are appreciated!");

        expectedResult = new Result(
                expectedData,
                expectedSupport);

        expectedString = "{\"data\":{\"id\":2,\"email\":\"janet.weaver@reqres.in\",\"first_name\":\"Janet\",\"last_name\":\"Weaver\",\"avatar\":\"https://reqres.in/img/faces/2-image.jpg\"},\"support\":{\"url\":\"https://reqres.in/#support-heading\",\"text\":\"To keep ReqRes free, contributions towards server costs are appreciated!\"}}";
    }

    @Test
    void testGetRequestGetsFeedbackSync() throws IOException {

        final JSONObject jsonObject = Rest.requestSync(URI.create("https://reqres.in/api/users/2").toURL(), HttpMethode.GET)
                .asJSONObject();

        assertNotNull(jsonObject);
    }

    @Test
    void testGetRequestAsJavaObjectSync() throws IOException {

        final Result result = Rest.requestSync(URI.create("https://reqres.in/api/users/2").toURL(), HttpMethode.GET)
                .asJavaObject(Result.class);

        assertEquals(expectedResult, result);
    }

    @Test
    void testGetRequestAsStringSync() throws IOException {

        final String string = Rest.requestSync(URI.create("https://reqres.in/api/users/2").toURL(), HttpMethode.GET)
                .asValueString();

        assertEquals(expectedString, string);
    }

    @Test
    void testGetRequestGetsFeedbackAsync() throws IOException {

        Rest.request(URI.create("https://reqres.in/api/users/2").toURL(), HttpMethode.GET)
                .whenComplete((restRequest, throwable) ->
                        assertNotNull(restRequest.asJSONObject()));
    }

    @Test
    void testGetRequestAsJavaObjectAsync() throws IOException {

        Rest.request(URI.create("https://reqres.in/api/users/2").toURL(), HttpMethode.GET)
                .whenComplete((restRequest, throwable) -> {
                    try {
                        assertEquals(expectedResult, restRequest.asJavaObject(Result.class));
                    } catch (JsonProcessingException exception) {
                        logger.log(Level.SEVERE, "There was an Exception while getting user Data", exception);
                    }
                });
    }

    @Test
    void testGetRequestAsStringAsync() throws IOException {

        Rest.request(URI.create("https://reqres.in/api/users/2").toURL(), HttpMethode.GET)
                .whenComplete((restRequest, throwable) -> {
                    assertEquals(expectedString, restRequest.asValueString());
                });
    }
}
