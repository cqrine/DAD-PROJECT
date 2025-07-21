package shared;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import org.json.JSONArray;
import org.json.JSONObject;

public class ApiClient {
    private String token;

    public ApiClient(String token) {
        this.token = token;
    }

    public static JSONObject login(String username, String password) {
        try {
            URL url = new URL("http://localhost/ecommerce-api/api/login.php");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);

            // Create JSON payload
            JSONObject json = new JSONObject();
            json.put("username", username);
            json.put("password", password);

            // Send request
            try (OutputStream os = conn.getOutputStream()) {
                byte[] input = json.toString().getBytes("utf-8");
                os.write(input, 0, input.length);
            }

            // Read response (handle both success and error)
            BufferedReader br;
            if (conn.getResponseCode() >= 200 && conn.getResponseCode() < 300) {
                br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"));
            } else {
                br = new BufferedReader(new InputStreamReader(conn.getErrorStream(), "utf-8"));
            }

            StringBuilder response = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                response.append(line.trim());
            }

            // Print raw response for debugging
            System.out.println("Raw login response: " + response);

            return new JSONObject(response.toString());

        } catch (Exception e) {
            e.printStackTrace();
            JSONObject error = new JSONObject();
            error.put("success", false);
            error.put("message", "Unable to connect to server: " + e.getMessage());
            return error;
        }
    }

    public static JSONObject register(String username, String email, String password, String phone) {
        try {
            URL url = new URL("http://localhost/ecommerce-api/api/users/register.php");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);

            JSONObject json = new JSONObject();
            json.put("username", username);
            json.put("email", email);
            json.put("password", password);
            json.put("phone", phone);

            try (OutputStream os = conn.getOutputStream()) {
                byte[] input = json.toString().getBytes("utf-8");
                os.write(input, 0, input.length);
            }

            BufferedReader br;
            if (conn.getResponseCode() >= 200 && conn.getResponseCode() < 300) {
                br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"));
            } else {
                br = new BufferedReader(new InputStreamReader(conn.getErrorStream(), "utf-8"));
            }

            StringBuilder response = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                response.append(line.trim());
            }

            return new JSONObject(response.toString());

        } catch (Exception e) {
            e.printStackTrace();
            JSONObject error = new JSONObject();
            error.put("success", false);
            error.put("message", "Unable to connect to server: " + e.getMessage());
            return error;
        }
    }

    public JSONArray getProducts() throws IOException {
        URL url = new URL("http://localhost/ecommerce-api/api/products/index.php");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Authorization", "Bearer " + token);

        BufferedReader br;
        if (conn.getResponseCode() >= 200 && conn.getResponseCode() < 300) {
            br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"));
        } else {
            br = new BufferedReader(new InputStreamReader(conn.getErrorStream(), "utf-8"));
        }

        StringBuilder response = new StringBuilder();
        String line;
        while ((line = br.readLine()) != null) {
            response.append(line.trim());
        }

        // Parse as JSONObject and extract the array from "products" key
        JSONObject jsonResponse = new JSONObject(response.toString());
        if (jsonResponse.has("products")) {
            return jsonResponse.getJSONArray("products");
        } else {
            throw new IOException("Unexpected response: missing 'products' key");
        }
    }

    public JSONObject createOrder(JSONObject orderData) throws IOException {
        URL url = new URL("http://localhost/ecommerce-api/api/orders/index.php");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setRequestProperty("Authorization", "Bearer " + token);
        conn.setDoOutput(true);

        try (OutputStream os = conn.getOutputStream()) {
            byte[] input = orderData.toString().getBytes("utf-8");
            os.write(input, 0, input.length);
        }

        BufferedReader br;
        if (conn.getResponseCode() >= 200 && conn.getResponseCode() < 300) {
            br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"));
        } else {
            br = new BufferedReader(new InputStreamReader(conn.getErrorStream(), "utf-8"));
        }

        StringBuilder response = new StringBuilder();
        String line;
        while ((line = br.readLine()) != null) {
            response.append(line.trim());
        }

        return new JSONObject(response.toString());
    }
    
public JSONObject updatePaymentStatus(String orderId, String paymentMethod) throws IOException {
    String urlString = "http://localhost/ecommerce-api/api/orders/" + orderId;
    URL url = new URL(urlString);
    
    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
    conn.setRequestMethod("POST"); // Simulate PATCH
    conn.setRequestProperty("X-HTTP-Method-Override", "PATCH");
    conn.setRequestProperty("Authorization", "Bearer " + token);
    conn.setRequestProperty("Content-Type", "application/json; utf-8");
    conn.setDoOutput(true);

    JSONObject body = new JSONObject();
    body.put("status", "Paid");
    body.put("payment_method", paymentMethod);

    System.out.println("Sending payment request to: " + urlString);
    System.out.println("Request body: " + body.toString());

    try (OutputStream os = conn.getOutputStream()) {
        byte[] input = body.toString().getBytes("utf-8");
        os.write(input, 0, input.length);
    }

    int responseCode = conn.getResponseCode();
    System.out.println("Response code: " + responseCode);

    InputStream is = (responseCode >= 200 && responseCode < 300)
            ? conn.getInputStream()
            : conn.getErrorStream();

    BufferedReader in = new BufferedReader(new InputStreamReader(is, "utf-8"));
    StringBuilder response = new StringBuilder();
    String line;
    while ((line = in.readLine()) != null) {
        response.append(line);
    }
    in.close();

    String responseText = response.toString();
    System.out.println("Raw response: '" + responseText + "'");
    System.out.println("Response length: " + responseText.length());
    
    // Check if response is empty or starts with whitespace
    if (responseText.trim().isEmpty()) {
        JSONObject error = new JSONObject();
        error.put("success", false);
        error.put("message", "Empty response from server");
        return error;
    }
    
    // Check if response starts with proper JSON
    String trimmed = responseText.trim();
    if (!trimmed.startsWith("{") && !trimmed.startsWith("[")) {
        System.out.println("Invalid JSON response. First 100 chars: " + 
                          responseText.substring(0, Math.min(100, responseText.length())));
        JSONObject error = new JSONObject();
        error.put("success", false);
        error.put("message", "Invalid JSON response: " + trimmed.substring(0, Math.min(50, trimmed.length())));
        return error;
    }

    try {
        return new JSONObject(responseText);
    } catch (Exception e) {
        System.out.println("JSON parsing error: " + e.getMessage());
        JSONObject error = new JSONObject();
        error.put("success", false);
        error.put("message", "JSON parsing error: " + e.getMessage());
        return error;
    }
}

}