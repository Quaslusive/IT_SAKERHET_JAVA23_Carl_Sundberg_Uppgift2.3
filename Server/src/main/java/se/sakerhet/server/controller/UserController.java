package se.sakerhet.server.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import se.sakerhet.server.dto.UserDTO;
import se.sakerhet.server.service.UserService;
import se.sakerhet.server.config.JwtTokenProvider;
import se.sakerhet.server.entity.User;

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
    public ResponseEntity<String> registerUser(@RequestBody UserDTO userDTO) {
        userService.registerUser(userDTO.getEmail(), userDTO.getPassword());
        return ResponseEntity.ok("User registered successfully");
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody UserDTO userDTO) {
        String email = userDTO.getEmail();
        String password = userDTO.getPassword();

        User user = userService.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (userService.checkPassword(password, user.getPassword())) {
            String token = jwtTokenProvider.generateToken(email);
            return ResponseEntity.ok(token);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
        }
    }
}