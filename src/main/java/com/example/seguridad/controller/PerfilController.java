package com.example.seguridad.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.seguridad.entity.Usuario;
import com.example.seguridad.repository.UsuarioRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;

@Controller
public class PerfilController {


    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UsuarioRepository usuarioRepository;
    
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/perfil")
    public String perfil(Model model, Authentication auth) {
        model.addAttribute("username", auth.getName());
        return "perfil";
    }

    @PostMapping("/perfil/cambiar-contrasena")
    public String cambiarContrasena(@RequestParam String actualContrasena,
                                    @RequestParam String nuevaContrasena,
                                    @RequestParam String repetirContrasena,
                                    RedirectAttributes redirectAttributes,
                                    HttpServletRequest request,
                                    Authentication auth) throws ServletException {

        String username = auth.getName();
        Usuario usuario = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));

        if (!passwordEncoder.matches(actualContrasena, usuario.getPassword())) {
            redirectAttributes.addFlashAttribute("error", "La contraseña actual es incorrecta.");
            return "redirect:/perfil";
        }

        if (!nuevaContrasena.equals(repetirContrasena)) {
            redirectAttributes.addFlashAttribute("error", "Las nuevas contraseñas no coinciden.");
            return "redirect:/perfil";
        }
        usuario.setPassword(passwordEncoder.encode(nuevaContrasena));
        usuarioRepository.save(usuario);
        request.logout();
        redirectAttributes.addFlashAttribute("mensaje", "Contraseña actualizada. Inicie sesión nuevamente.");
        return "redirect:/login";
    }
}
