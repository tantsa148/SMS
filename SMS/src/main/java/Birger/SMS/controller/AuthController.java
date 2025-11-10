package Birger.SMS.controller;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import Birger.SMS.security.JwtUtil;

@RestController
@RequestMapping("/")
public class AuthController {

private final AuthenticationManager authenticationManager;

public AuthController(AuthenticationManager authenticationManager) {
    this.authenticationManager = authenticationManager;
}

@PostMapping("/login")
public ResponseEntity<?> login(@RequestBody Map<String, String> user) {
    String username = user.get("username");
    String password = user.get("password");

    try {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, password)
        );
        String token = JwtUtil.generateToken(username);
        return ResponseEntity.ok(Map.of("token", token));
    } catch (AuthenticationException e) {
        return ResponseEntity.status(401).body("Nom d'utilisateur ou mot de passe invalide");
    }
}

}