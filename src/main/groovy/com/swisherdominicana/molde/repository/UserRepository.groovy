package com.swisherdominicana.molde.repository

import com.swisherdominicana.molde.model.TUsuarios
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

interface UserRepository extends JpaRepository<TUsuarios, Integer> {

    boolean existsByUsername(String username);

    TUsuarios findByUsername(String username);

    @Query(
            value = "select u.f_codigo_usuario, u.f_nombre,u.f_id_usuario,u.f_auditor,u.f_activo, u.f_apellido, '' as f_password from t_usuario u where u.f_auditor = true and u.f_activo = true",
            nativeQuery = true
    )
    List<TUsuarios> findByAuditor();

    @Query(
            value = "select u.f_editar_peso,u.f_cancelar_canasta,u.f_codigo_usuario, u.f_nombre,u.f_id_usuario,u.f_auditor,u.f_activo, u.f_apellido, '' as f_password from t_usuario u where u.f_activo = true and (u.f_nombre ilike :nombre% OR u.f_apellido ilike :nombre% OR u.f_codigo_usuario = :codigo) order by u.f_codigo_usuario DESC ",
            nativeQuery = true
    )
    List<TUsuarios> findUsuarioByIdOrName(
            @Param("codigo") Long codigo,
            @Param("nombre") String nombre);


}
