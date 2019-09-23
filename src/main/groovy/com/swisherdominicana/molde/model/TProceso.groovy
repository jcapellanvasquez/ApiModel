package com.swisherdominicana.molde.model

import javax.persistence.*

@Entity
@Table(name = "t_procesos", schema = "produccion")
class TProceso {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    BigDecimal f_id

    @Column(name="f_estado")
    Boolean fEstado

    String f_descripcion
}
