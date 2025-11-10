package Birger.SMS.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import Birger.SMS.model.User;
import Birger.SMS.repository.UserRepository;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // Créer un utilisateur avec mot de passe haché
    public User createUser(String username, String role, String rawPassword) {
        User user = new User();
        user.setUsername(username);
        user.setRole(role);
        // Hachage du mot de passe
        user.setPassword(passwordEncoder.encode(rawPassword));
        return userRepository.save(user);
    }
}
