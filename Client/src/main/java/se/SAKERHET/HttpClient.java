package se.SAKERHET;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import com.google.gson.Gson;
public class HttpClient {

    private final String baseUrl;
    private final Gson gson = new Gson();
    public HttpClient(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public String sendPostRequest(String endpoint, Object payload, String token) throws Exception {
        URL url = new URL(baseUrl + endpoint);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");

        // Set Content-Type to application/json
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

        // Log request details
        System.out.println("Request Method: " + conn.getRequestMethod());
        System.out.println("Request URL: " + conn.getURL());
        System.out.println("Payload: " + jsonPayload);

        // Write the payload to the request body
        try (OutputStream os = conn.getOutputStream()) {
            byte[] input = jsonPayload.getBytes(StandardCharsets.UTF_8);
            os.write(input, 0, input.length);
        }

        // Process the response
        return processResponse(conn);
    }


/*    public String sendPostRequest(String endpoint, String payload, String token) throws Exception {
        URL url = new URL(baseUrl + endpoint);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

        System.out.println("Sending Request:");
        System.out.println("Endpoint: " + endpoint);
        System.out.println("Payload: " + payload);
        System.out.println("Token: " + token);


        if (token != null && !token.isEmpty()) {
            conn.setRequestProperty("Authorization", "Bearer " + token);
            System.out.println("sendPostRequest" + baseUrl);
        }

        conn.setDoOutput(true);

        try (OutputStream os = conn.getOutputStream()) {
            byte[] input = payload.getBytes(StandardCharsets.UTF_8);
            os.write(input, 0, input.length);
        }

        return processResponse(conn);
    }*/

    public String sendGetRequest(String endpoint, String token) throws Exception {
        URL url = new URL(baseUrl + endpoint);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");

        if (token != null && !token.isEmpty()) {
            conn.setRequestProperty("Authorization", "Bearer " + token);
            System.out.println("sendGetRequest" + baseUrl);
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



/*    private String processResponse(HttpURLConnection conn) throws Exception {
        int responseCode = conn.getResponseCode();
        InputStream responseStream = (responseCode >= 200 && responseCode < 300) ?
                conn.getInputStream() : conn.getErrorStream();
        System.out.println("processResponse" + baseUrl);

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
    }*/
}
