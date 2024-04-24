package com.bienvan.store.controller;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import com.bienvan.store.decoratorPattern.BasicPasswordStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bienvan.store.dto.UserDto;
import com.bienvan.store.model.ERole;
import com.bienvan.store.model.Role;
import com.bienvan.store.model.User;
import com.bienvan.store.model.mapper.UserMapper;
import com.bienvan.store.payload.response.MessageResponse;
import com.bienvan.store.service.ProductService;
import com.bienvan.store.service.RoleService;
import com.bienvan.store.service.UserService;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    UserService userService;

    @Autowired
    ProductService productService;

    @Autowired
    RoleService roleService;

    @Autowired
    BasicPasswordStorageService basicPasswordStorageService;

    @GetMapping // get all user
    public ResponseEntity<?> getListUser() {
        List<UserDto> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/{id}") // get 1 user
    public ResponseEntity<?> getUserById(@PathVariable Long id) {
        User user = userService.getUserById(id);
        if (user != null) {
            return ResponseEntity.ok(new MessageResponse(0, "Find user success", UserMapper.toUserDto(user)));
        }
        return ResponseEntity.ok(new MessageResponse(1, "Find user failed", null));
    }

    @PostMapping // tạo user mới
    public ResponseEntity<?> createUser(@RequestBody @Valid User user,
            BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity
                    .ok(new MessageResponse(1, bindingResult.getAllErrors().get(0).getDefaultMessage(), null));
        }
        if (user.getPassword().length() < 6) {
            return ResponseEntity.ok(new MessageResponse(1, "Password phải trên 6 kí tự", null));
        }

        User checkUser = userService.findByEmail(user.getEmail());
        if (checkUser == null) {
            User newUser = userService.createUser(user);
            basicPasswordStorageService.changePassword(user.getPassword(), newUser.getId());
            return ResponseEntity.ok(new MessageResponse(0, "Add user successfully", newUser));
        }
        return ResponseEntity.ok(new MessageResponse(1, "Email đã tồn tại", null));
    }

    @PutMapping // update
    public ResponseEntity<?> updateUser(@RequestBody @Valid User user,
            BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity
                    .ok(new MessageResponse(1, bindingResult.getAllErrors().get(0).getDefaultMessage(), null));
        }

        // kiểm tra xem người dùng có tồn tại hay không
        User existingUser = userService.getUserById(user.getId());

        if (existingUser != null) {
            existingUser.setEmail(user.getEmail());
            existingUser.setName(user.getName());
            existingUser.setGender(user.getGender());
            existingUser.setUserRoles(user.getUserRoles());
            userService.createUser(existingUser);
            return ResponseEntity.ok(new MessageResponse(0, "Update user successfully", null));
        }
        return ResponseEntity.ok(new MessageResponse(1, "Update user failed", null));
    }

    @DeleteMapping("/{id}") // xóa user
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        User existingUser = userService.getUserById(id);
        if (existingUser != null) {
            userService.deleteUser(id);
            return ResponseEntity.ok(new MessageResponse(0, "Delete user successfully", null));
        }
        return ResponseEntity.ok(new MessageResponse(1, "Delete user failed", null));
    }

    @PostMapping(value = { "/login" })
    public ResponseEntity<?> checkLogin(@RequestParam("email") String email, @RequestParam("password") String password,
            HttpSession session) {
        if (userService.checkLogin(email, password)) {
            User info = userService.findByEmail(email);
            for (Role r : info.getUserRoles()) {
                session.setAttribute(r.getName().toString(), r.getName().toString());
                System.out.println(r.getName().toString());
            }
            session.setAttribute("id", info.getId());
            session.setAttribute("email", email);
            session.setAttribute("name", info.getName());
            session.setAttribute("gender", info.getGender());
            


            return ResponseEntity.ok(new MessageResponse(0, "Đăng nhập thành công", null));
        }
        return ResponseEntity.ok(new MessageResponse(1, "Email hoặc mật khẩu không chính xác", null));
    }

    @PostMapping(value = { "/register" })
    public ResponseEntity<?> register(@RequestBody @Valid User user,
            BindingResult bindingResult,
            HttpSession session) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity
                    .ok(new MessageResponse(1, bindingResult.getAllErrors().get(0).getDefaultMessage(), null));
        }
        if (user.getPassword().length() < 6) {
            return ResponseEntity.ok(new MessageResponse(1, "Password phải trên 6 kí tự", null));
        }
        User checkUser = userService.findByEmail(user.getEmail());
        if (checkUser == null) {
            Role role = roleService.findByName(ERole.ROLE_USER).get();
            Set<Role> listRoles = new HashSet<Role>();

            listRoles.add(role);
            user.setUserRoles(listRoles);
            User newUser = userService.createUser(user);
            basicPasswordStorageService.changePassword(user.getPassword(), newUser.getId());
            for (Role r : listRoles) {
                session.setAttribute(r.getName().toString(), r.getName().toString());
            }

            session.setAttribute("id", newUser.getId());
            session.setAttribute("email", newUser.getEmail());
            session.setAttribute("name", newUser.getName());
            session.setAttribute("gender", newUser.getGender());

            return ResponseEntity.ok(new MessageResponse(0, "Đăng ký thành công", null));

        }
        return ResponseEntity.ok(new MessageResponse(1, "Email đã tồn tại", null));
    }

    @GetMapping("logout")
    public ResponseEntity<?> logout(HttpSession session) {
        session.invalidate();
        return ResponseEntity.ok(new MessageResponse(0, "Đăng xuất thành công", null));
    }
}
