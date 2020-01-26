package com.swisherdominicana.molde.repository;

import com.swisherdominicana.molde.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {

    boolean existsByUsername(String username);


    Usuario findByUsername(String username);

    Usuario findById(Long id);

    List<Usuario> findAll();



}
