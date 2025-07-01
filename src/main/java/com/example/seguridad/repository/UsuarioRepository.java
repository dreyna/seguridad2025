package com.example.seguridad.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.seguridad.entity.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Long>{
	Optional<Usuario> findByUsername(String username);
	
	List<Usuario> findByUsernameContainingIgnoreCase(String username);
	Page<Usuario> findByUsernameContainingIgnoreCase(String username, Pageable pageable);
}
