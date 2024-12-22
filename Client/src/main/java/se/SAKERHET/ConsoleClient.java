package se.SAKERHET;

import java.util.Scanner;

public class ConsoleClient {

    private static final HttpClient httpClient = new HttpClient();
    private static String token;
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
     //   String token = null;

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
                    email = scanner.nextLine();
                    System.out.print("Enter your password: ");
                    password = scanner.nextLine();
                    token = loginUser(email, password);
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
            String response = httpClient.sendPostRequest("/register", payload);
            System.out.println(response);
        } catch (Exception e) {
            System.out.println("Error registering user: " + e.getMessage());
        }
    }

/*    public static String loginUser(String email, String password) {
        try {
            String payload = "email=" + email + "&password=" + password;
            String response = httpClient.sendPostRequest("/login", payload, null);
            return response;
        } catch (Exception e) {
            System.out.println("Error logging in: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }*/


    public static String loginUser(String email, String password) {
        try {
            String payload = "email=" + email + "&password=" + password;
            String response = httpClient.sendPostRequest("/login", payload);  // No JWT headers needed
            return response;
        } catch (Exception e) {
            System.out.println("Error logging in: " + e.getMessage());
            e.printStackTrace();  // Print the stack trace for more details
            return null;
        }
    }


    private static void handleAuthenticatedActions(String token) {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("1. Create Capsule");
            System.out.println("2. View Capsules");
            System.out.println("3. Logout");
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
                    viewCapsules();
                    break;

                case 3:
                    System.out.println("Logged out.");
                    return;  // Exit to the main menu

                default:
                    System.out.println("Invalid choice. Try again.");
            }
        }
    }

/*
    private static void createCapsule(String token, String message) {
        try {
            String payload = "message=" + message;
            String response = httpClient.sendPostRequest("/capsules/create", payload, token);
            System.out.println("Capsule created: " + response);
        } catch (Exception e) {
            System.out.println("Error creating capsule: " + e.getMessage());
            e.printStackTrace();  // Print stack trace for more details
        }
    }
*/


    private static void createCapsule(String token, String message) {
        try {
            String payload = "message=" + message;
            String response = httpClient.sendPostRequest("/create", payload + "&token=" + token);
            System.out.println("Capsule created: " + response);
        } catch (Exception e) {
            System.out.println("Error creating capsule: " + e.getMessage());
            e.printStackTrace();  // Print stack trace for more details
        }
    }



    private static void viewCapsules() {
        try {
            String response = httpClient.sendGetRequest("/capsules", token);
            System.out.println("Your Capsules: " + response);
        } catch (Exception e) {
            System.out.println("Error fetching capsules: " + e.getMessage());
        }
    }
}
