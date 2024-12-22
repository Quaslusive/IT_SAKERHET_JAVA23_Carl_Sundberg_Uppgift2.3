package se.SAKERHET;

import java.util.Scanner;

public class ConsoleClient {

    private static final HttpClient httpClient = new HttpClient("http://localhost:8080/api/auth");
    private static String token;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("1. Register");
            System.out.println("2. Login");
            System.out.println("3. Exit");
            System.out.print("Choose an option: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

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
                    token = loginUser(emailLogin, passwordLogin);
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
            String payload = "email=" + email + "&password=" + password;
            String response = httpClient.sendPostRequest("/register", payload, null);
            System.out.println(response);
        } catch (Exception e) {
            System.out.println("Error registering user: " + e.getMessage());
        }
    }

    private static String loginUser(String email, String password) {
        try {
            String payload = "email=" + email + "&password=" + password;
            return httpClient.sendPostRequest("/login", payload, null);
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
            scanner.nextLine(); // Consume newline

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
            String payload = "message=" + message;
            String response = httpClient.sendPostRequest("/capsules/create", payload, token);
            System.out.println("Capsule created: " + response);
        } catch (Exception e) {
            System.out.println("Error creating capsule: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void viewCapsules(String token) {
        try {
            String response = httpClient.sendGetRequest("/capsules", token);
            System.out.println("Your Capsules: " + response);
        } catch (Exception e) {
            System.out.println("Error fetching capsules: " + e.getMessage());
        }
    }

    private static void viewDecryptedCapsules(String token) {
        try {
            String response = httpClient.sendGetRequest("/capsules/decrypted", token);
            System.out.println("Decrypted Capsules: " + response);
        } catch (Exception e) {
            System.out.println("Error fetching decrypted capsules: " + e.getMessage());
        }
    }
}
