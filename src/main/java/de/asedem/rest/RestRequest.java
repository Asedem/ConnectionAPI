package de.asedem.rest;

import org.jetbrains.annotations.NotNull;
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

public class RestRequest {

    private static final Logger logger = Logger.getLogger(RestRequest.class.getName());

    private RestRequest() {
    }

    /**
     * Send a POST request to an url
     *
     * @param url  the url to send to
     * @param jsonObject the json which should be sent
     * @return a response from the GET request
     * @throws IOException if something went wrong
     */
    public static <T> RestResponse postSync(@NotNull final URL url, @NotNull final T jsonObject) throws IOException {

        return RestRequest.postSync(url, jsonObject, 10000, 10000);
    }

    /**
     * Send a POST request to an url
     *
     * @param url  the url to send to
     * @param jsonObject the json which should be sent
     * @param connectionTimeout the max connection time
     * @param readTimeout       the max read time
     * @return a response from the GET request
     * @throws IOException if something went wrong
     */
    public static <T> RestResponse postSync(@NotNull final URL url, @NotNull final T jsonObject, final int connectionTimeout, final int readTimeout) throws IOException {

        final BufferedReader bufferedReader;
        final StringBuilder responseContent = new StringBuilder();

        String line;

        final HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.addRequestProperty("Content-Type", "application/json");
        connection.addRequestProperty("User-Agent", "request");
        connection.setDoOutput(true);
        connection.setRequestMethod(RestRequestMethode.POST.getMethode());
        connection.setConnectTimeout(connectionTimeout);
        connection.setReadTimeout(readTimeout);

        final OutputStream stream = connection.getOutputStream();
        final String json = RestResponse.mapper.writeValueAsString(jsonObject);
        stream.write(json.getBytes());
        stream.flush();
        stream.close();

        final int responseCode = connection.getResponseCode();

        if (responseCode > 299) {
            bufferedReader = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
            while ((line = bufferedReader.readLine()) != null) responseContent.append(line);
            bufferedReader.close();
            return new RestResponse(null);
        }

        bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        while ((line = bufferedReader.readLine()) != null) responseContent.append(line);
        connection.getInputStream().close();
        bufferedReader.close();

        connection.disconnect();

        return new RestResponse(new JSONObject(responseContent.toString()));
    }

    /**
     * Send a GET request to an url async
     *
     * @param url the url to send to
     * @return a response from the GET request
     */
    public static CompletableFuture<RestResponse> get(@NotNull final URL url) {

        return RestRequest.get(url, 10000, 10000);
    }

    /**
     * Send a GET request to an url async
     *
     * @param url               the url to send to
     * @param connectionTimeout the max connection time
     * @param readTimeout       the max read time
     * @return a response from the GET request
     */
    public static CompletableFuture<RestResponse> get(@NotNull final URL url, final int connectionTimeout, final int readTimeout) {

        final CompletableFuture<RestResponse> completableFuture = new CompletableFuture<>();

        new Thread(() -> {

            try {
                completableFuture.complete(getSync(url, connectionTimeout, readTimeout));
            } catch (IOException exception) {
                logger.log(Level.SEVERE, "Exception in get-Thread", exception);
            }
        });

        return completableFuture;
    }

    /**
     * Send a GET request to an url sync
     *
     * @param url the url to send to
     * @return a response from the GET request
     * @throws IOException if something went wrong
     */
    public static RestResponse getSync(@NotNull final URL url) throws IOException {

        return RestRequest.getSync(url, 10000, 10000);
    }

    /**
     * Send a GET request to an url sync
     *
     * @param url               the url to send to
     * @param connectionTimeout the max connection time
     * @param readTimeout       the max read time
     * @return a response from the GET request
     * @throws IOException if something went wrong
     */
    public static RestResponse getSync(@NotNull final URL url, final int connectionTimeout, final int readTimeout) throws IOException {

        final BufferedReader bufferedReader;
        final StringBuilder responseContent = new StringBuilder();

        String line;

        final HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod(RestRequestMethode.GET.getMethode());
        connection.setConnectTimeout(connectionTimeout);
        connection.setReadTimeout(readTimeout);

        final int responseCode = connection.getResponseCode();

        if (responseCode > 299) {
            bufferedReader = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
            while ((line = bufferedReader.readLine()) != null) responseContent.append(line);
            bufferedReader.close();
            return new RestResponse(null);
        }

        bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        while ((line = bufferedReader.readLine()) != null) responseContent.append(line);
        bufferedReader.close();

        connection.disconnect();

        return new RestResponse(new JSONObject(responseContent.toString()));
    }
}
