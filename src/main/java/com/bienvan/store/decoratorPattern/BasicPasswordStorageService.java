package com.bienvan.store.decoratorPattern;

import com.bienvan.store.model.User;
import com.bienvan.store.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BasicPasswordStorageService implements PasswordStorageService {
    @Autowired
    UserRepository userRepository;
    public void changePassword(String password, Long id) {
        User user = userRepository.findById(id).get();
        user.setPassword(password);
        userRepository.save(user);
    }
}
