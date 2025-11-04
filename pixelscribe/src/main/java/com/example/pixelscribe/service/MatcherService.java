package com.example.pixelscribe.service;


import com.example.pixelscribe.model.entities.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MatcherService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public User authenticate(String email, String rawPassword) {
        System.out.println("Attempting login for email: " + email);

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        System.out.println("User found: " + user.getEmail());
        boolean passwordMatches = passwordEncoder.matches(rawPassword, user.getPassword());
        System.out.println("Password matches: " + passwordMatches);

        if (!passwordMatches) {
            throw new RuntimeException("Incorrect password");
        }

        // ✅ Retorna el usuario autenticado
        return user;
    }

    public User getUser(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }
}
