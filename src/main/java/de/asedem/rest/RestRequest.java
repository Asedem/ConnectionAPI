package de.asedem.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONObject;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

public class RestRequest {

    private JSONObject value;

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

    public RestRequest get(URL url) throws IOException {

        return this.get(url, 10000, 10000);
    }

    public RestRequest get(URL url, int connectionTimeout, int readTimeout) throws IOException {

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
            this.value = null;
            return this;
        }

        bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        while ((line = bufferedReader.readLine()) != null) responseContent.append(line);
        bufferedReader.close();

        connection.disconnect();

        this.value = new JSONObject(responseContent.toString());
        return this;
    }

    public JSONObject asRawValue() {
        return value;
    }

    public String asValueString() {
        return this.value.toString();
    }

    public <T> JavaObjectHolder<T> asJavaObject(Class<T> targetClass) throws JsonProcessingException {

        return new JavaObjectHolder<>(new ObjectMapper().readValue(this.value.toString(), targetClass));
    }

    public record JavaObjectHolder<T>(T get) { }
}
