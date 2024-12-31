package se.SAKERHET;

import com.google.gson.Gson;
import se.sakerhet.server.dto.UserDTO;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class ConsoleClient {

    private static final HttpClient httpClient = new HttpClient("http://localhost:8080/api/auth");

    private static final String REGISTER_ENDPOINT = "/register";
    private static final String LOGIN_ENDPOINT = "/login";
    private static final String CAPSULES_CREATE_ENDPOINT = "/capsules/create";
    private static final String CAPSULES_VIEW_ENDPOINT = "/capsules";
    private static final String CAPSULES_DECRYPT_ENDPOINT = "/capsules/decrypted";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("<<<< Create Encrypted Messages Application by \"Carl .S\" >>>>");
            System.out.println("1. Register");
            System.out.println("2. Login");
            System.out.println("3. Exit");
            System.out.print("Choose an option: ");
            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    System.out.print("Enter your email: ");
                    String email = scanner.nextLine();
                    System.out.print("Enter your password: ");
                    String password = scanner.nextLine();
                    registerUser(email, password);
                    break;

                case 2:
                    System.out.print("Enter your email: ");
                    String emailLogin = scanner.nextLine();
                    System.out.print("Enter your password: ");
                    String passwordLogin = scanner.nextLine();
                    String token = loginUser(emailLogin, passwordLogin);
                    if (token != null) {
                        System.out.println("Login successful! Token: " + token);
                        handleAuthenticatedActions(token);
                    }
                    break;

                case 3:
                    System.out.println("Exiting...");
                    scanner.close();
                    System.exit(0);

                default:
                    System.out.println("Invalid choice. Try again.");
            }
        }
    }

    private static void registerUser(String email, String password) {
        try {
            UserDTO userDTO = new UserDTO();
            userDTO.setEmail(email);
            userDTO.setPassword(password);

            String response = httpClient.sendPostRequest(REGISTER_ENDPOINT, userDTO, null);
            System.out.println("Registration Successful: " + response);
        } catch (Exception e) {
            System.out.println("Error registering user: " + e.getMessage());
        }
    }

    private static String loginUser(String email, String password) {
        try {
            UserDTO userDTO = new UserDTO();
            userDTO.setEmail(email);
            userDTO.setPassword(password);

            return httpClient.sendPostRequest(LOGIN_ENDPOINT, userDTO, null);
        } catch (Exception e) {
            System.out.println("Error logging in: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    private static void handleAuthenticatedActions(String token) {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("1. Create Capsule");
            System.out.println("2. View Capsules");
            System.out.println("3. View Decrypted Capsules");
            System.out.println("4. Logout");
            System.out.print("Choose an option: ");
            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    System.out.print("Enter your message: ");
                    String message = scanner.nextLine();
                    createCapsule(token, message);
                    break;

                case 2:
                    viewCapsules(token);
                    break;

                case 3:
                    viewDecryptedCapsules(token);
                    break;

                case 4:
                    System.out.println("Logged out.");
                    return;

                default:
                    System.out.println("Invalid choice. Try again.");
            }
        }
    }

    private static void createCapsule(String token, String message) {
        try {
            Gson gson = new Gson();
            Map<String, String> payloadMap = new HashMap<>();
            payloadMap.put("message", message);
            String payload = gson.toJson(payloadMap);

            String response = httpClient.sendPostRequest(CAPSULES_CREATE_ENDPOINT, payload, token);
            System.out.println("Capsule created: " + response);
        } catch (Exception e) {
            System.out.println("Error creating capsule: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void viewCapsules(String token) {
        try {
            String response = httpClient.sendGetRequest(CAPSULES_VIEW_ENDPOINT, token);
            System.out.println("Your Capsules: " + response);
        } catch (Exception e) {
            System.out.println("Error fetching capsules: " + e.getMessage());
        }
    }

    private static void viewDecryptedCapsules(String token) {
        try {
            String response = httpClient.sendGetRequest(CAPSULES_DECRYPT_ENDPOINT, token);
            System.out.println("Decrypted Capsules: " + response);
        } catch (Exception e) {
            System.out.println("Error fetching decrypted capsules: " + e.getMessage());
        }
    }
}
