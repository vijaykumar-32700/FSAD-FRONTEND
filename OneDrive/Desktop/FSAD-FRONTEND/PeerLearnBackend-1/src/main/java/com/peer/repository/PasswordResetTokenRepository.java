package com.peer.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import com.peer.entity.PasswordResetToken;

public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Long> {
    Optional<PasswordResetToken> findByToken(String token);
    void deleteByToken(String token);
    void deleteByUserId(Long userId);
}