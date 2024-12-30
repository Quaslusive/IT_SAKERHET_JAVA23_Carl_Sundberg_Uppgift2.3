package se.sakerhet.server.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import se.sakerhet.server.dto.UserRequest;
import se.sakerhet.server.service.UserService;
import se.sakerhet.server.config.JwtTokenProvider;
import se.sakerhet.server.entity.User;

import java.util.Map;
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

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody UserRequest userRequest) {
        userService.registerUser(userRequest.getEmail(), userRequest.getPassword());
        return ResponseEntity.ok("User registered successfully");
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody UserRequest userRequest) {
        String email = userRequest.getEmail();
        String password = userRequest.getPassword();

        User user = userService.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (userService.checkPassword(password, user.getPassword())) {
            String token = jwtTokenProvider.generateToken(email);
            return ResponseEntity.ok(token);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
        }
    }





    /*@PostMapping("/login")
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
    }*/
}
