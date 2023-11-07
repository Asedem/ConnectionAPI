package de.asedem.rest;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.CompletableFuture;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Rest {

    private static final Logger logger = Logger.getLogger(Rest.class.getName());

    private Rest() {
    }

    /**
     * Send a GET request to an url async
     *
     * @param url         the url to send to
     * @param httpMethode the http method to use
     * @return a response from the GET request
     */
    public static CompletableFuture<JsonResponse> request(@NotNull final URL url, @NotNull final HttpMethode httpMethode) {

        return Rest.request(url, httpMethode, 10000, 10000);
    }

    /**
     * Send a GET request to an url async
     *
     * @param url               the url to send to
     * @param httpMethode       the http method to use
     * @param connectionTimeout the max connection time
     * @param readTimeout       the max read time
     * @return a response from the GET request
     */
    public static CompletableFuture<JsonResponse> request(@NotNull final URL url, @NotNull final HttpMethode httpMethode, final int connectionTimeout, final int readTimeout) {

        return Rest.request(url, httpMethode, null, readTimeout, connectionTimeout);
    }

    /**
     * Send a GET request to an url async
     *
     * @param url         the url to send to
     * @param httpMethode the http method to use
     * @param jsonObject  the json which should be sent
     * @return a response from the GET request
     */
    public static <T> CompletableFuture<JsonResponse> request(@NotNull final URL url, @NotNull final HttpMethode httpMethode, @Nullable final T jsonObject) {

        return Rest.request(url, httpMethode, jsonObject, 10000, 10000);
    }

    /**
     * Send a GET request to an url async
     *
     * @param url               the url to send to
     * @param httpMethode       the http method to use
     * @param jsonObject        the json which should be sent
     * @param connectionTimeout the max connection time
     * @param readTimeout       the max read time
     * @return a response from the GET request
     */
    public static <T> CompletableFuture<JsonResponse> request(@NotNull final URL url, @NotNull final HttpMethode httpMethode, @Nullable final T jsonObject, final int connectionTimeout, final int readTimeout) {

        final CompletableFuture<JsonResponse> completableFuture = new CompletableFuture<>();

        new Thread(() -> {

            try {
                completableFuture.complete(requestSync(url, httpMethode, jsonObject, connectionTimeout, readTimeout));
            } catch (IOException exception) {
                logger.log(Level.SEVERE, "Exception in request-Thread", exception);
            }
        });

        return completableFuture;
    }

    /**
     * Send a Rest request to an url sync
     *
     * @param url the url to send to
     * @param httpMethode the http method to use
     * @return a response from the GET request
     * @throws IOException if something went wrong
     */
    public static JsonResponse requestSync(@NotNull final URL url, @NotNull final HttpMethode httpMethode) throws IOException {

        return Rest.requestSync(url, httpMethode, 10000, 10000);
    }

    /**
     * Send a Rest request to an url sync
     *
     * @param url the url to send to
     * @param httpMethode the http method to use
     * @return a response from the GET request
     * @throws IOException if something went wrong
     */
    public static JsonResponse requestSync(@NotNull final URL url, @NotNull final HttpMethode httpMethode, final int connectionTimeout, final int readTimeout) throws IOException {

        return Rest.requestSync(url, httpMethode, null, connectionTimeout, readTimeout);
    }

    /**
     * Send a Rest request to an url sync
     *
     * @param url the url to send to
     * @param httpMethode the http method to use
     * @param jsonObject the json which should be sent
     * @return a response from the GET request
     * @throws IOException if something went wrong
     */
    public static <T> JsonResponse requestSync(@NotNull final URL url, @NotNull final HttpMethode httpMethode, @Nullable final T jsonObject) throws IOException {

        return Rest.requestSync(url, httpMethode, jsonObject, 10000, 10000);
    }

    /**
     * Send a Rest request to an url
     *
     * @param url  the url to send to
     * @param httpMethode the http method to use
     * @param jsonObject the json which should be sent
     * @param connectionTimeout the max connection time
     * @param readTimeout       the max read time
     * @return a response from the GET request
     * @throws IOException if something went wrong
     */
    public static <T> JsonResponse requestSync(@NotNull final URL url, @NotNull HttpMethode httpMethode, @Nullable final T jsonObject, final int connectionTimeout, final int readTimeout) throws IOException {

        final BufferedReader bufferedReader;
        final StringBuilder responseContent = new StringBuilder();

        String line;

        final HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        if (jsonObject != null) {
            connection.addRequestProperty("Content-Type", "application/json");
            connection.addRequestProperty("User-Agent", "request");
            connection.setDoOutput(true);
        }
        connection.setRequestMethod(httpMethode.get());
        connection.setConnectTimeout(connectionTimeout);
        connection.setReadTimeout(readTimeout);

        if (jsonObject != null) {
            final OutputStream stream = connection.getOutputStream();
            final String json = JsonResponse.mapper.writeValueAsString(jsonObject);
            stream.write(json.getBytes());
            stream.flush();
            stream.close();
        }

        final int responseCode = connection.getResponseCode();

        if (responseCode > 299) {
            bufferedReader = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
            while ((line = bufferedReader.readLine()) != null) responseContent.append(line);
            bufferedReader.close();
            return new JsonResponse(null);
        }

        bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        while ((line = bufferedReader.readLine()) != null) responseContent.append(line);
        connection.getInputStream().close();
        bufferedReader.close();

        connection.disconnect();

        return new JsonResponse(new JSONObject(responseContent.toString()));
    }
}
