package com.bienvan.store.decoratorPattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class EncryptedPasswordStorageDecorator implements PasswordStorageService {
    private PasswordStorageService passwordStorageService;

    @Autowired
    BCryptPasswordEncoder passwordEncoder;

    public EncryptedPasswordStorageDecorator(PasswordStorageService passwordStorageService) {
        this.passwordStorageService = passwordStorageService;
    }

    public void changePassword(String password, Long id) {
        // Mã hóa mật khẩu trước khi lưu trữ
        String encryptedPassword = encrypt(password);
        passwordStorageService.changePassword(encryptedPassword, id);
    }

    private String encrypt(String password) {
        return passwordEncoder.encode(password);
    }
}
