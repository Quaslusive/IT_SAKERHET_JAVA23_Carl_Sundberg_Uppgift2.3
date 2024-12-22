package se.SAKERHET;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class HttpClient {

    private final String baseUrl;

    public HttpClient(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public String sendPostRequest(String endpoint, String payload, String token) throws Exception {
        URL url = new URL(baseUrl + endpoint);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

        if (token != null && !token.isEmpty()) {
            conn.setRequestProperty("Authorization", "Bearer " + token);
        }

        conn.setDoOutput(true);

        try (OutputStream os = conn.getOutputStream()) {
            byte[] input = payload.getBytes(StandardCharsets.UTF_8);
            os.write(input, 0, input.length);
        }

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
        InputStream responseStream = (responseCode >= 200 && responseCode < 300) ?
                conn.getInputStream() : conn.getErrorStream();

        if (responseStream == null) {
            throw new RuntimeException("Failed : HTTP error code : " + responseCode + ". No response body.");
        }

        try (BufferedReader br = new BufferedReader(new InputStreamReader(responseStream, StandardCharsets.UTF_8))) {
            StringBuilder response = new StringBuilder();
            String responseLine;
            while ((responseLine = br.readLine()) != null) {
                response.append(responseLine.trim());
            }

            if (responseCode >= 200 && responseCode < 300) {
                return response.toString();
            } else {
                throw new RuntimeException("Failed : HTTP error code : " + responseCode + ". Response: " + response);
            }
        }
    }
}
