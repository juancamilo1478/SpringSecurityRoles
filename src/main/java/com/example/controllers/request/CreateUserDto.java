package com.example.controllers.request;


import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateUserDto {
    @NotBlank
    private String email;
    @NotBlank
    private String username;
    @NotBlank
    private String password;
    private Set<String> roles;
}
