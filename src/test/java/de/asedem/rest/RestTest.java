package de.asedem.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import de.asedem.rest.models.Data;
import de.asedem.rest.models.Result;
import de.asedem.rest.models.Support;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URL;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class RestTest {

    private static Result expectedResult;
    private static String expectedString;

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

        expectedString = "{\"data\":{\"last_name\":\"Weaver\",\"id\":2,\"avatar\":\"https://reqres.in/img/faces/2-image.jpg\",\"first_name\":\"Janet\",\"email\":\"janet.weaver@reqres.in\"},\"support\":{\"text\":\"To keep ReqRes free, contributions towards server costs are appreciated!\",\"url\":\"https://reqres.in/#support-heading\"}}";
    }

    @Test
    void testGetRequestGetsFeedbackSync() throws IOException {

        final JSONObject jsonObject = Rest.requestSync(new URL("https://reqres.in/api/users/2"), HttpMethode.GET)
                .asRawValue();

        assertNotNull(jsonObject);
    }

    @Test
    void testGetRequestAsJavaObjectSync() throws IOException {

        final Result result = Rest.requestSync(new URL("https://reqres.in/api/users/2"), HttpMethode.GET)
                .asJavaObject(Result.class);

        assertEquals(expectedResult, result);
    }

    @Test
    void testGetRequestAsStringSync() throws IOException {

        final String string = Rest.requestSync(new URL("https://reqres.in/api/users/2"), HttpMethode.GET)
                .asValueString();

        assertEquals(expectedString, string);
    }

    @Test
    void testGetRequestGetsFeedbackAsync() throws IOException {

        Rest.request(new URL("https://reqres.in/api/users/2"), HttpMethode.GET)
                .whenComplete((restRequest, throwable) ->
                        assertNotNull(restRequest.asRawValue()));
    }

    @Test
    void testGetRequestAsJavaObjectAsync() throws IOException {

        Rest.request(new URL("https://reqres.in/api/users/2"), HttpMethode.GET)
                .whenComplete((restRequest, throwable) -> {
                    try {
                        assertEquals(expectedResult, restRequest.asJavaObject(Result.class));
                    } catch (JsonProcessingException exception) {
                        exception.printStackTrace();
                    }
                });
    }

    @Test
    void testGetRequestAsStringAsync() throws IOException {

        Rest.request(new URL("https://reqres.in/api/users/2"), HttpMethode.GET)
                .whenComplete((restRequest, throwable) -> {
                    assertEquals(expectedString, restRequest.asValueString());
                });
    }
}
