package com.example.controllers;

import com.example.controllers.request.CreateUserDto;
import com.example.models.Erole;
import com.example.models.RoleEntity;
import com.example.models.UserEntity;
import com.example.repositories.UserRepository;
import jakarta.validation.Valid;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;
import java.util.stream.Collectors;

@RestController
public class PrincipalController {
    @Autowired
    private UserRepository userRepository;
    @GetMapping("/hello")
    public String getHello() {
        return "hello word not security";
    }

    @GetMapping("/hellosecurety")
    public String getHelloSecurity() {
        return "hello word security";
    }

    @PostMapping("/CreateUser")
    public ResponseEntity<?> createUser(@Valid @RequestBody CreateUserDto createUserDto) {
        Set<RoleEntity> rol = createUserDto.getRoles().stream().map(role ->
                RoleEntity.builder()
                        .name(Erole.valueOf(role))
                        .build()).collect(Collectors.toSet());

        UserEntity userEntity = UserEntity.builder()
                .username(createUserDto.getUsername())
                .password(createUserDto.getPassword())
                .email(createUserDto.getEmail())
                .roles(rol)
                .build();

        userRepository.save(userEntity);
        return ResponseEntity.ok(userEntity);
    }
@DeleteMapping("/deleteUser")
    public String DeleteUser(@RequestParam String id){
        userRepository.deleteById(Long.parseLong(id));
        return "usaurio " + id +"borrado correctamente";
    }
}
