package SharedApi;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject; 

public class ApiClient {
    private String token;

    public ApiClient(String token) {
        this.token = token;
    }

    public static JSONObject login(String username, String password) {
        try {
            URL url = new URL(ApiConfig.BASE_URL+ "login.php");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);

            JSONObject json = new JSONObject();
            json.put("username", username);
            json.put("password", password);

            try (OutputStream os = conn.getOutputStream()) {
                os.write(json.toString().getBytes("utf-8"));
            }

            return readResponse(conn);
        } catch (Exception e) {
            return errorResponse(e);
        }
    }

    public static JSONObject register(String username, String email, String password, String phone, String role) throws IOException {
        URL url = new URL(ApiConfig.BASE_URL + "users/register.php");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json; utf-8");
        conn.setDoOutput(true);

        JSONObject payload = new JSONObject();
        payload.put("username", username);
        payload.put("email", email);
        payload.put("password", password);
        payload.put("phone", phone);
        payload.put("role", role);

        try (OutputStream os = conn.getOutputStream()) {
            os.write(payload.toString().getBytes("utf-8"));
        }

        return readResponse(conn);
    }

    public JSONArray getAssignedDeliveries() throws IOException, JSONException {
        URL url = new URL(ApiConfig.BASE_URL + "deliveries/assigned.php");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Authorization", "Bearer " + token);

        int responseCode = conn.getResponseCode();
        if (responseCode == 200) {
            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = in.readLine()) != null) {
                response.append(line);
            }
            in.close();

            return new JSONArray(response.toString());
        } else {
            throw new IOException("HTTP error code: " + responseCode);
        }
    }

    public boolean markDeliveryAsDelivered(int deliveryId) throws IOException {
        URL url = new URL(ApiConfig.BASE_URL + "deliveries/mark_delivered.php");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Authorization", "Bearer " + token);
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setDoOutput(true);

        JSONObject request = new JSONObject();
        request.put("delivery_id", deliveryId);

        try (OutputStream os = conn.getOutputStream()) {
            os.write(request.toString().getBytes());
        }

        JSONObject jsonResponse = readResponse(conn);
        return jsonResponse.getBoolean("success");
    }

    private static JSONObject readResponse(HttpURLConnection conn) throws IOException {
        InputStream is = (conn.getResponseCode() < HttpURLConnection.HTTP_BAD_REQUEST)
                ? conn.getInputStream()
                : conn.getErrorStream();

        BufferedReader br = new BufferedReader(new InputStreamReader(is, "utf-8"));
        StringBuilder response = new StringBuilder();
        String line;
        while ((line = br.readLine()) != null) {
            response.append(line.trim());
        }
        br.close();

        return new JSONObject(response.toString());
    }

    private static JSONObject errorResponse(Exception e) {
        e.printStackTrace();
        JSONObject error = new JSONObject();
        error.put("success", false);
        error.put("message", "Unable to connect to server: " + e.getMessage());
        return error;
    }
    
    public static JSONObject getJsonWithAuth(String endpoint, String token) throws IOException {
        URL url = new URL(ApiConfig.BASE_URL + endpoint);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Authorization", "Bearer " + token);
        conn.setRequestProperty("Accept", "application/json");

        InputStream is = (conn.getResponseCode() < HttpURLConnection.HTTP_BAD_REQUEST)
                ? conn.getInputStream() : conn.getErrorStream();

        BufferedReader br = new BufferedReader(new InputStreamReader(is, "utf-8"));
        StringBuilder response = new StringBuilder();
        String line;
        while ((line = br.readLine()) != null) {
            response.append(line.trim());
        }
        br.close();

        return new JSONObject(response.toString());
    }

    public static JSONObject sendJsonWithAuth(String endpoint, String method, JSONObject body, String token) throws IOException {
        URL url = new URL(ApiConfig.BASE_URL + endpoint);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        // Simulate PATCH
        if (method.equalsIgnoreCase("PATCH")) {
            conn.setRequestMethod("POST");
            conn.setRequestProperty("X-HTTP-Method-Override", "PATCH");
        } else {
            conn.setRequestMethod(method);
        }

        conn.setRequestProperty("Authorization", "Bearer " + token);
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setDoOutput(true);

        try (OutputStream os = conn.getOutputStream()) {
            byte[] input = body.toString().getBytes("utf-8");
            os.write(input, 0, input.length);
        }

        BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"));
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = br.readLine()) != null) {
            sb.append(line.trim());
        }
        br.close();

        return new JSONObject(sb.toString());
    }


    
}
