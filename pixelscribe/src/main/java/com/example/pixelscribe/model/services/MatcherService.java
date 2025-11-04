package com.example.pixelscribe.model.services;

import com.example.pixelscribe.model.entities.User;
import com.example.pixelscribe.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MatcherService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public User authenticate(String email, String rawPassword) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        if (!passwordEncoder.matches(rawPassword, user.getPassword())) {
            throw new RuntimeException("Incorrect password");
        }
        return user;
    }

    public User register(String email, String rawPassword) {
        if (userRepository.findByEmail(email).isPresent()) {
            throw new RuntimeException("Email already registered");
        }
        User newUser = new User(email, passwordEncoder.encode(rawPassword));
        return userRepository.save(newUser);
    }
}
