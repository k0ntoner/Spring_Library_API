package org.example.utils;

import lombok.extern.slf4j.Slf4j;
import org.example.enums.Role;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

@Slf4j
public class Auth0Util {

    private static String cachedToken;

    private static long expirationTime = 0;

    public static String login(String email, String password, String auth0Domain, String clientIdLog, String clientSecretLog, String audience) throws IOException {
        URL url = new URL(auth0Domain + "/oauth/token");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setDoOutput(true);

        String requestBody = String.format("""
                {
                    "grant_type": "password",
                    "client_id": "%s",
                    "client_secret": "%s",
                    "username": "%s",
                    "password": "%s",
                    "audience": "%s",
                    "scope": "openid profile email",
                    "realm": "Username-Password-Authentication"
                }
                """, clientIdLog, clientSecretLog, email, password, audience);

        try (OutputStream os = connection.getOutputStream()) {
            os.write(requestBody.getBytes(StandardCharsets.UTF_8));
        }

        int responseCode = connection.getResponseCode();
        String responseBody = null;

        InputStream stream = (responseCode >= 200 && responseCode < 300) ? connection.getInputStream() : connection.getErrorStream();

        if (responseCode == 200) {
            String jsonResponse = new String(connection.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
            return new JSONObject(jsonResponse).getString("access_token");
        }

        if (stream != null) {
            responseBody = new String(stream.readAllBytes(), StandardCharsets.UTF_8);
            log.error("Auth0 response [{}]: {}", responseCode, responseBody);
        }

        if (responseCode == 403) {
            throw new IllegalArgumentException(responseBody);
        } else {
            throw new IOException("Unexpected response from Auth0: " + responseCode);
        }
    }

    public static String register(String email, String password, Role role, String auth0Domain, String clientIdReg, String clientSecretReg, String auth0Connection, String audience) throws IOException {
        String apiUrl = auth0Domain + "/api/v2/users";
        String managementToken = getManagementToken(auth0Domain, clientIdReg, clientSecretReg, audience);
        String requestBody = String.format(
                "{\"email\": \"%s\", \"password\": \"%s\", \"connection\": \"%s\"}",
                email, password, auth0Connection
        );

        HttpURLConnection connection = (HttpURLConnection) new URL(apiUrl).openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Authorization", "Bearer " + managementToken);
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setDoOutput(true);

        try (OutputStream os = connection.getOutputStream()) {
            os.write(requestBody.getBytes(StandardCharsets.UTF_8));
        }

        int responseCode = connection.getResponseCode();
        if (responseCode == 200 || responseCode == 201) {
            String jsonResponse = new String(connection.getInputStream().readAllBytes(), StandardCharsets.UTF_8);

            String userId = new JSONObject(jsonResponse).getString("user_id");

            assignRoleToUser(userId, managementToken, role, auth0Domain);
            return userId;
        }

        InputStream errorStream = connection.getErrorStream();
        if (errorStream != null) {
            String errorResponse = new String(errorStream.readAllBytes(), StandardCharsets.UTF_8);
            log.error("Auth0 Error: " + errorResponse);
        }
        throw new RuntimeException("Error with auth0: " + responseCode);
    }

    private static void assignRoleToUser(String userId, String token, Role role, String auth0Domain) throws IOException {
        String roleId;

        if (role.equals(Role.ROLE_EMPLOYEE)) {
            roleId = "rol_65wiU5wAcQpdqnd4";
        } else if (role.equals(Role.ROLE_CUSTOMER)) {
            roleId = "rol_VgyNFq0QZbnRuIF6";
        } else {
            throw new IllegalArgumentException("Unknown user type: " + role);
        }

        String apiUrl = auth0Domain + "/api/v2/users/" + userId + "/roles";

        HttpURLConnection conn = (HttpURLConnection) new URL(apiUrl).openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Authorization", "Bearer " + token);
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setDoOutput(true);

        String requestBody = String.format("{\"roles\": [\"%s\"]}", roleId);
        try (OutputStream os = conn.getOutputStream()) {
            os.write(requestBody.getBytes(StandardCharsets.UTF_8));
        }

        int responseCode = conn.getResponseCode();
        if (responseCode != 204) {
            String error = new String(conn.getErrorStream().readAllBytes(), StandardCharsets.UTF_8);
            log.error("Failed to assign role: " + error);
            throw new RuntimeException("Failed to assign role: " + responseCode);
        }
    }

    private static String getManagementToken(String auth0Domain, String clientIdReg, String clientSecretReg, String audience) throws IOException {
        if (cachedToken != null && System.currentTimeMillis() < expirationTime) {
            return cachedToken;
        }

        URL url = new URL(auth0Domain + "/oauth/token");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setDoOutput(true);

        String requestBody = String.format("""
        {
          "client_id": "%s",
          "client_secret": "%s",
          "audience": "%s",
          "grant_type": "client_credentials"
        }
        """, clientIdReg, clientSecretReg, audience);

        try (OutputStream os = connection.getOutputStream()) {
            os.write(requestBody.getBytes(StandardCharsets.UTF_8));
        }

        int responseCode = connection.getResponseCode();
        if (responseCode != 200) {
            throw new IOException("Failed to get token from Auth0: " + responseCode);
        }

        String jsonResponse = new String(connection.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
        JSONObject json = new JSONObject(jsonResponse);
        cachedToken = json.getString("access_token");
        int expiresIn = json.getInt("expires_in");
        expirationTime = System.currentTimeMillis() + expiresIn * 1000L - 10_000L;

        return cachedToken;

    }

}
