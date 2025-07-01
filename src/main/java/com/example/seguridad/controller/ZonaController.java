package com.example.seguridad.controller;


import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ZonaController {

    @GetMapping("/user")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public String zonaUsuario() {
        return "zona-usuario";
    }

    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public String zonaAdmin() {
        return "zona-admin";
    }
    @GetMapping("/usuario-info")
    public String userPage() {
        return "info-user";
    }
    
}
