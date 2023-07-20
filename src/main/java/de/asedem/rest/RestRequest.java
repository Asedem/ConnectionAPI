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

public class RestRequest {

    private RestRequest() {
    }

    /**
     * Send a POST request to an url
     *
     * @param url  the url to send to
     * @param json the json which should be sendet
     * @throws IOException if something went wrong
     */
    public static void post(@NotNull final URL url, @NotNull final String json) throws IOException {

        final HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.addRequestProperty("Content-Type", "application/json");
        connection.addRequestProperty("User-Agent", "request");
        connection.setDoOutput(true);
        connection.setRequestMethod(RestRequestMethode.POST.getMethode());

        final OutputStream stream = connection.getOutputStream();
        stream.write(json.getBytes());
        stream.flush();
        stream.close();

        connection.getInputStream().close();
        connection.disconnect();
    }

    /**
     * Send a GET request to an url async
     *
     * @param url the url to send to
     * @return a response from the GET request
     */
    public static CompletableFuture<Response> get(@NotNull final URL url) {

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
    public static CompletableFuture<Response> get(@NotNull final URL url, final int connectionTimeout, final int readTimeout) {

        final CompletableFuture<Response> completableFuture = new CompletableFuture<>();

        new Thread(() -> {

            try {
                completableFuture.complete(getSync(url, connectionTimeout, readTimeout));
            } catch (IOException exception) {
                exception.printStackTrace();
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
    public static Response getSync(@NotNull final URL url) throws IOException {

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
    public static Response getSync(@NotNull final URL url, final int connectionTimeout, final int readTimeout) throws IOException {


        BufferedReader bufferedReader;
        String line;
        StringBuilder responseContent = new StringBuilder();

        final HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod(RestRequestMethode.GET.getMethode());
        connection.setConnectTimeout(connectionTimeout);
        connection.setReadTimeout(readTimeout);

        final int responseCode = connection.getResponseCode();

        if (responseCode > 299) {
            bufferedReader = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
            while ((line = bufferedReader.readLine()) != null) responseContent.append(line);
            bufferedReader.close();
            return new Response(null);
        }

        bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        while ((line = bufferedReader.readLine()) != null) responseContent.append(line);
        bufferedReader.close();

        connection.disconnect();

        return new Response(new JSONObject(responseContent.toString()));
    }
}
