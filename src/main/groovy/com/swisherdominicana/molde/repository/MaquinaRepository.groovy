package com.swisherdominicana.molde.repository

import com.swisherdominicana.molde.model.TMaquina
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

interface MaquinaRepository extends JpaRepository<TMaquina, Integer> {
    List<TMaquina> findAllByfIdLinea(Long fIdLinea)

    @Query(
            value = "select m.* from produccion.t_maquinas m where m.f_id = :fId",
            nativeQuery = true
    )
    TMaquina findByfId(@Param("fId") Long fId)
    //List<TMaquina> findAllByOrderByfOrdenAscfIdLinea(Long fIdLinea)
}
