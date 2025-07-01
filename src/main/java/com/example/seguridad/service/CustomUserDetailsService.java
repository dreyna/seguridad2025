package com.example.seguridad.service;

import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.seguridad.entity.Usuario;
import com.example.seguridad.repository.UsuarioRepository;
@Service
public class CustomUserDetailsService implements UserDetailsService{
	 @Autowired
	 private UsuarioRepository usuarioRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

		Usuario usuario = usuarioRepository.findByUsername(username)
	            .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));

	        Set<GrantedAuthority> authorities = usuario.getRoles().stream()
	            .map(rol -> new SimpleGrantedAuthority("ROLE_" + rol.getNombre()))
	            .collect(Collectors.toSet());

	        return new org.springframework.security.core.userdetails.User(
	                usuario.getUsername(), 
	                usuario.getPassword(), 
	                usuario.getEstado(),
	                true, true, true, authorities);
	    }
}
