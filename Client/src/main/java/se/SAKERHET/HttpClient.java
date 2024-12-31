package se.SAKERHET;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

import com.google.gson.Gson;

public class HttpClient {

    private final String baseUrl;

    public HttpClient(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public String sendPostRequest(String endpoint, Object payload, String token) throws Exception {
        URI uri = new URI(baseUrl + endpoint);
        URL url = uri.toURL();
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");

        conn.setRequestProperty("Content-Type", "application/json");

        // Add Authorization header if token is provided
        if (token != null && !token.isEmpty()) {
            conn.setRequestProperty("Authorization", "Bearer " + token);
        }

        // Set connection to allow output
        conn.setDoOutput(true);

        // Serialize payload to JSON using Gson
        Gson gson = new Gson();
        String jsonPayload = gson.toJson(payload);

        System.out.println("\n***********DEBUG**********");
        System.out.println("Request Method: " + conn.getRequestMethod());
        System.out.println("Request URL: " + conn.getURL());
        System.out.println("Payload: " + jsonPayload);
        System.out.println("***********DEBUG**********\n");

        // Write the payload to the request body
        try (OutputStream os = conn.getOutputStream()) {
            byte[] input = jsonPayload.getBytes(StandardCharsets.UTF_8);
            os.write(input, 0, input.length);
        }

        // Process the response
        return processResponse(conn);
    }

    public String sendGetRequest(String endpoint, String token) throws Exception {
        URL url = new URL(baseUrl + endpoint);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");

        if (token != null && !token.isEmpty()) {
            conn.setRequestProperty("Authorization", "Bearer " + token);
        }

        return processResponse(conn);
    }

    private String processResponse(HttpURLConnection conn) throws Exception {
        int responseCode = conn.getResponseCode();
        try (var br = new BufferedReader(new InputStreamReader(
                responseCode >= 200 && responseCode < 300 ? conn.getInputStream() : conn.getErrorStream(),
                StandardCharsets.UTF_8))) {
            return br.lines().collect(Collectors.joining(System.lineSeparator()));
        }
    }
}