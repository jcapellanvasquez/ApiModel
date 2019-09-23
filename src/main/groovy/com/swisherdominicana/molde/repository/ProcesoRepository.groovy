package com.swisherdominicana.molde.repository

import com.swisherdominicana.molde.model.TProceso
import org.springframework.data.jpa.repository.JpaRepository

interface ProcesoRepository extends JpaRepository<TProceso, Integer> {

    List<TProceso> findAllByfEstado(Boolean fEstado)
    List<TProceso> findAll()
}
