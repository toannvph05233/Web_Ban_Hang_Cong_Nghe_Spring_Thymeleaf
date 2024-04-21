package com.bienvan.store.service;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.bienvan.store.dto.UserDto;
import com.bienvan.store.model.*;
import com.bienvan.store.model.mapper.UserMapper;
import com.bienvan.store.repository.*;

@Service
public class UserService {
    @Autowired
    UserRepository userRepository;

    public User createUser(User user) {
        return userRepository.save(user);
    }

    public List<User> findAll(){
        return userRepository.findAll();
    }

    public List<UserDto> getAllUsers() {
        List<User> users = userRepository.findAll();
        List<UserDto> userDtos = new ArrayList<UserDto>();
        if(!users.isEmpty()){
            for(User u: users){
                UserDto tmp = UserMapper.toUserDto(u);
                userDtos.add(tmp);
            }
            return userDtos;
        }
        return null;
    }

    public User getUserById(Long id) {
        if(id == null){
            return null;
        }
        return userRepository.findById(id).orElse(null);
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    public User findByEmail(String email){
        return userRepository.findByEmail(email).orElse(null);
    }

    public boolean existsByEmail(String email){
        return userRepository.existsByEmail(email);
    }

    public boolean checkLogin(String email, String rawPassword){
        User user = findByEmail(email);
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
      
        if(user != null && passwordEncoder.matches(rawPassword, user.getPassword())){
            return true;
        }
        return false;
    }
}
