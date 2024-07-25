package com.example.controllers;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestRolesControllers {

    @GetMapping("/accesAdmin")
    @PreAuthorize("hasRole('ADMIN')")
    public String accesAdmin(){
        return "hola mundo admin tienes admin";
    }
    @GetMapping("/accesUser")
    @PreAuthorize("hasRole('USER')")
    public String accesUser(){
        return "hola mundo user tienes user";
    }
    @GetMapping("/accesInvite")
    @PreAuthorize("hasAnyRole('INVITED','ADMIN','USER')")
    public String accesInvite(){
        return "hola invitado   tienes invitado";
    }
}
