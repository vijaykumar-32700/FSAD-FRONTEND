package com.peer.config;

import com.peer.entity.User;
import com.peer.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DataSeeder {

    @Bean
    CommandLineRunner seedUsers(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            if (userRepository.findByEmail("teacher@peerlearn.com").isEmpty()) {
                User teacher = User.builder()
                        .fullName("Dr. Sarah Johnson")
                        .email("teacher@peerlearn.com")
                        .password(passwordEncoder.encode("password123"))
                        .role(User.Role.TEACHER)
                        .section("A")
                        .department("Computer Science")
                        .userId("T001")
                        .active(true)
                        .build();
                userRepository.save(teacher);
            }

            if (userRepository.findByEmail("student1@peerlearn.com").isEmpty()) {
                User s1 = User.builder()
                        .fullName("Aria Moore")
                        .email("student1@peerlearn.com")
                        .password(passwordEncoder.encode("password123"))
                        .role(User.Role.STUDENT)
                        .section("A")
                        .department("Computer Science")
                        .userId("STU-1001")
                        .active(true)
                        .build();
                userRepository.save(s1);
            }

            if (userRepository.findByEmail("student2@peerlearn.com").isEmpty()) {
                User s2 = User.builder()
                        .fullName("Lily Walker")
                        .email("student2@peerlearn.com")
                        .password(passwordEncoder.encode("password123"))
                        .role(User.Role.STUDENT)
                        .section("A")
                        .department("Computer Science")
                        .userId("STU-1002")
                        .active(true)
                        .build();
                userRepository.save(s2);
            }
        };
    }
}