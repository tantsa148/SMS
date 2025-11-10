package Birger.SMS.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

}
