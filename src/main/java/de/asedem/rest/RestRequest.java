package de.asedem.rest;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.CompletableFuture;

public class RestRequest {

    /**
     * Send a POST request to an url
     * @param url the url to send to
     * @param json the json which should be sendet
     * @throws IOException if something went wrong
     */
    public void post(URL url, String json) throws IOException {

        HttpURLConnection connection;

        connection = (HttpURLConnection) url.openConnection();
        connection.addRequestProperty("Content-Type", "application/json");
        connection.addRequestProperty("User-Agent", "request");
        connection.setDoOutput(true);
        connection.setRequestMethod(RestRequestMethode.POST.getMethode());

        OutputStream stream = connection.getOutputStream();
        stream.write(json.getBytes());
        stream.flush();
        stream.close();

        connection.getInputStream().close();
        connection.disconnect();
    }

    /**
     * Send a GET request to an url async
     * @param url the url to send to
     * @return a response from the GET request
     */
    public CompletableFuture<Response> get(URL url) {

        return this.get(url, 10000, 10000);
    }

    /**
     * Send a GET request to an url async
     * @param url the url to send to
     * @param connectionTimeout the max connection time
     * @param readTimeout the max read time
     * @return a response from the GET request
     */
    public CompletableFuture<Response> get(URL url, int connectionTimeout, int readTimeout) {

        CompletableFuture<Response> completableFuture = new CompletableFuture<>();

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
     * @param url the url to send to
     * @return a response from the GET request
     * @throws IOException if something went wrong
     */
    public Response getSync(URL url) throws IOException {

        return this.getSync(url, 10000, 10000);
    }

    /**
     * Send a GET request to an url sync
     * @param url the url to send to
     * @param connectionTimeout the max connection time
     * @param readTimeout the max read time
     * @return a response from the GET request
     * @throws IOException if something went wrong
     */
    public Response getSync(URL url, int connectionTimeout, int readTimeout) throws IOException {

        HttpURLConnection connection;

        BufferedReader bufferedReader;
        String line;
        StringBuilder responseContent = new StringBuilder();

        connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod(RestRequestMethode.GET.getMethode());
        connection.setConnectTimeout(connectionTimeout);
        connection.setReadTimeout(readTimeout);

        int responseCode = connection.getResponseCode();

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
