package com.bienvan.store.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bienvan.store.model.PasswordResetToken;
import com.bienvan.store.model.User;

public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Long> {

    PasswordResetToken findByToken(String token);

    PasswordResetToken findByUser(User user);

}
