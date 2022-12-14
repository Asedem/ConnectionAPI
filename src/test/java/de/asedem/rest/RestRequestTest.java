package de.asedem.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import de.asedem.rest.models.*;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URL;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class RestRequestTest {

    private static Result expectedResult;
    private static String expectedString;

    @BeforeAll
    static void setup() {

        Data expectedData = new Data(
                2,
                "janet.weaver@reqres.in",
                "Janet",
                "Weaver",
                "https://reqres.in/img/faces/2-image.jpg");

        Support expectedSupport = new Support(
                "https://reqres.in/#support-heading",
                "To keep ReqRes free, contributions towards server costs are appreciated!");

        expectedResult = new Result(
                expectedData,
                expectedSupport);

        expectedString = "{\"data\":{\"last_name\":\"Weaver\",\"id\":2,\"avatar\":\"https://reqres.in/img/faces/2-image.jpg\",\"first_name\":\"Janet\",\"email\":\"janet.weaver@reqres.in\"},\"support\":{\"text\":\"To keep ReqRes free, contributions towards server costs are appreciated!\",\"url\":\"https://reqres.in/#support-heading\"}}";
    }

    @Test
    void testGetRequestGetsFeedbackSync() throws IOException {

        JSONObject jsonObject = new RestRequest()
                .getSync(new URL("https://reqres.in/api/users/2"))
                .asRawValue();

        assertNotNull(jsonObject);
    }

    @Test
    void testGetRequestAsJavaObjectSync() throws IOException {

        Result result = new RestRequest()
                .getSync(new URL("https://reqres.in/api/users/2"))
                .asJavaObject(Result.class)
                .get();

        assertEquals(expectedResult, result);
    }

    @Test
    void testGetRequestAsStringSync() throws IOException {

        String string = new RestRequest()
                .getSync(new URL("https://reqres.in/api/users/2"))
                .asValueString();

        assertEquals(expectedString, string);
    }

    @Test
    void testGetRequestGetsFeedbackAsync() throws IOException {

        new RestRequest()
                .get(new URL("https://reqres.in/api/users/2"))
                .whenComplete((restRequest, throwable) ->
                        assertNotNull(restRequest.asRawValue()));
    }

    @Test
    void testGetRequestAsJavaObjectAsync() throws IOException {

        new RestRequest()
                .get(new URL("https://reqres.in/api/users/2"))
                .whenComplete((restRequest, throwable) -> {
                    try {
                        assertEquals(expectedResult, restRequest.asJavaObject(Result.class).get());
                    } catch (JsonProcessingException exception) {
                        exception.printStackTrace();
                    }
                });
    }

    @Test
    void testGetRequestAsStringAsync() throws IOException {

        new RestRequest()
                .get(new URL("https://reqres.in/api/users/2"))
                .whenComplete((restRequest, throwable) -> {
                    assertEquals(expectedString, restRequest.asValueString());
                });
    }
}
