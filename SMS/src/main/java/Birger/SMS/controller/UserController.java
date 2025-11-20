package Birger.SMS.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import Birger.SMS.dto.UserDTO;
import Birger.SMS.model.User;
import Birger.SMS.repository.UserRepository;
import Birger.SMS.service.UserService;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;
    private final UserRepository userRepository;

    public UserController(UserService userService, UserRepository userRepository) {
        this.userService = userService;
        this.userRepository = userRepository;
    }

    // ------------------ Enregistrement ------------------
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody UserDTO userDTO) {
        if (userRepository.existsByUsername(userDTO.getUsername())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Erreur : nom d'utilisateur déjà utilisé !");
        }

        User newUser = userService.createUser(
                userDTO.getUsername(),
                userDTO.getRole(),
                userDTO.getPassword()
        );

        return ResponseEntity.status(HttpStatus.CREATED)
                .body("Utilisateur créé avec succès : " + newUser.getUsername());
    }

    // ------------------ Récupérer utilisateur connecté ------------------
    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth == null || !(auth.getPrincipal() instanceof UserDetails)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Utilisateur non connecté !");
        }

        UserDetails userDetails = (UserDetails) auth.getPrincipal();

        // Récupérer l'utilisateur complet depuis la DB
        User user = userRepository.findByUsername(userDetails.getUsername())
                .orElse(null);

        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Utilisateur non trouvé en base !");
        }

        // Remplir le DTO
        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setUsername(user.getUsername());
        userDTO.setRole(user.getRole());
        userDTO.setPassword(null); // ne jamais renvoyer le mot de passe

        return ResponseEntity.ok(userDTO);
    }
}
