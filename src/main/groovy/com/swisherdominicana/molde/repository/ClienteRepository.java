package com.swisherdominicana.molde.repository;

import com.swisherdominicana.molde.model.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ClienteRepository extends JpaRepository<Cliente,Long> {

    List<Cliente> findAllByActivo(Boolean activo);
    Cliente findByIdAndActivo(Long id, Boolean activo);
}
