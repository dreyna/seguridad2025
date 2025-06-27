package com.example.seguridad.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import jakarta.servlet.http.HttpServletResponse;

@Controller
public class LoginController {
	
	@GetMapping("/login")
    public String login() {
        return "login"; // login.html
    }

    @GetMapping("/dashboard")
    public String dashboard(HttpServletResponse response) {
    	response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate"); // HTTP 1.1
        response.setHeader("Pragma", "no-cache"); // HTTP 1.0
        response.setDateHeader("Expires", 0); // Proxies
        return "dashboard"; // dashboard.html
    }
    @GetMapping("/logout")
    public String logout() {
        return "login"; // login.html
    }
}
