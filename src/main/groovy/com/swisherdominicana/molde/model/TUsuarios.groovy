package com.swisherdominicana.molde.model


import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

import javax.persistence.*

@Entity
@Table(name="t_usuario", schema = "public")
class TUsuarios implements UserDetails {

    @Id
    @Column(name="f_codigo_usuario")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    BigDecimal f_codigo_usuario

    @Column(name="f_id_empleado")
    Long f_id_empleado

    @Column(name="f_id_usuario")
    String username

    @Column(name = "f_password")
    String password

    @Column(name = "f_concepto_entrada")
    String fConceptoEntrada

    String f_nombre
    String f_apellido

    @Column(name = "f_auditor")
    Boolean auditor

    @Column(name = "f_activo")
    Boolean activo

    @Column(name = "f_modifica_fecha_recepcion_bobina")
    Boolean canChangeDate

    @Column(name = "f_editar_peso")
    Boolean fEditarPeso

    @Column(name = "f_editar_bobina")
    Boolean fEditarBobina

    @Column(name = "f_cancelar_canasta")
    Boolean fCancelarCanasta

    @Column(name = "f_cancelar_bobina")
    Boolean fCancelarBobina

    @Column(name = "f_editar_produccion_bobinas")
    Boolean fEditarProduccion

    // Esto es para evadir la solicitud de algun role
    @Override
    Collection<? extends GrantedAuthority> getAuthorities() {
        return new HashSet<GrantedAuthority>()
    }

    @Override
    boolean isAccountNonExpired() {
        return false
    }

    @Override
    boolean isAccountNonLocked() {
        return false
    }

    @Override
    boolean isCredentialsNonExpired() {
        return false
    }

    @Override
    boolean isEnabled() {
        return false
    }
}
