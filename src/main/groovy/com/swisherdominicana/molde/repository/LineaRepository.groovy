package com.swisherdominicana.molde.repository

import com.swisherdominicana.molde.model.TLinea
import org.springframework.data.jpa.repository.JpaRepository

interface LineaRepository extends JpaRepository<TLinea, Integer> {

    List<TLinea> findAllByfIdProceso(Long fIdProceso)
}
