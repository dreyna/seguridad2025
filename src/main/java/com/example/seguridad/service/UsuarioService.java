package com.example.seguridad.service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.seguridad.entity.Rol;
import com.example.seguridad.entity.Usuario;
import com.example.seguridad.repository.RolRepository;
import com.example.seguridad.repository.UsuarioRepository;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Autowired
    private RolRepository rolRepository;
    
    public List<Usuario> listarUsuarios() {
        return usuarioRepository.findAll();
    }

    public Usuario obtenerPorId(Long id) {
        return usuarioRepository.findById(id).orElse(null);
    }

    public void guardar(Usuario usuario) {
    	Optional<Usuario> existente = usuarioRepository.findByUsername(usuario.getUsername());
        if (usuario.getId() == null && existente.isPresent()) {
            throw new IllegalArgumentException("El nombre de usuario ya existe.");
        }
        if (usuario.getId() != null && existente.isPresent() && !existente.get().getId().equals(usuario.getId())) {
            throw new IllegalArgumentException("El nombre de usuario ya está en uso por otro usuario.");
        }

        if ((usuario.getId() == null || usuario.getId() != null) && (!usuario.getPassword().equals(existente.map(Usuario::getPassword).orElse(""))  || usuario.getPassword().equals(existente.map(Usuario::getPassword).orElse("")))) {
            usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
        }
        Set<Rol> rolesCompletos = usuario.getRoles().stream()
            .map(r -> rolRepository.findById(r.getId()).orElseThrow())
            .collect(Collectors.toSet());

        usuario.setRoles(rolesCompletos);
        usuarioRepository.save(usuario);
    }

    public void eliminar(Long id) {
        usuarioRepository.deleteById(id);
    }

    public List<Usuario> buscarPorNombre(String nombre) {
        return usuarioRepository.findByUsernameContainingIgnoreCase(nombre);
    }
    public void cambiarContrasena(String username, String nuevaContrasena) {
        Usuario usuario = usuarioRepository.findByUsername(username)
            .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));
        usuario.setPassword(passwordEncoder.encode(nuevaContrasena));
        usuarioRepository.save(usuario);
    }
    public Page<Usuario> listarUsuariosPaginado(String filtro, Pageable pageable) {
        if (filtro == null || filtro.isBlank()) {
            return usuarioRepository.findAll(pageable);
        }
        return usuarioRepository.findByUsernameContainingIgnoreCase(filtro, pageable);
    }
}