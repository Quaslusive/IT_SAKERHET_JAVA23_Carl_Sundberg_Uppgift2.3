package se.sakerhet.server.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import se.sakerhet.server.service.UserService;
import se.sakerhet.server.config.JwtTokenProvider;
import se.sakerhet.server.entity.User;

import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
public class UserController {

    private final UserService userService;
    private final JwtTokenProvider jwtTokenProvider;

    public UserController(UserService userService, JwtTokenProvider jwtTokenProvider) {
        this.userService = userService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    // Registreringsendpoint där lösenordet hashades
    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestParam String email, @RequestParam String password) {
        // Kontrollera om användaren redan finns
        Optional<User> existingUser = userService.findByEmail(email);
        if (existingUser.isPresent()) {
            return ResponseEntity.badRequest().body("User with this email already exists");
        }

        // Registrera ny användare
        userService.registerUser(email, password);  // Lösenordet hashas i UserService
        return ResponseEntity.ok("User registered successfully");
    }

    // Inloggningsendpoint där lösenordet verifieras och JWT-token returneras om det är korrekt
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestParam String email, @RequestParam String password) {
        Optional<User> user = userService.findByEmail(email);

        // Kontrollera om användaren finns
        if (user.isEmpty()) {
            return ResponseEntity.badRequest().body("User not found");
        }

        // Verifiera lösenordet
        if (!userService.checkPassword(password, user.get().getPassword())) {
            return ResponseEntity.badRequest().body("Invalid password");
        }

        // Om lösenordet är korrekt, generera JWT-token
        String token = jwtTokenProvider.generateToken(email);  // Generera JWT-token
        return ResponseEntity.ok(token);  // Returnera JWT-token till klienten
    }
}
