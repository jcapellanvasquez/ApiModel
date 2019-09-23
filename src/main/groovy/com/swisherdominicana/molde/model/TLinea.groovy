package com.swisherdominicana.molde.model

import javax.persistence.*

@Entity
@Table(name="t_linea_produccion", schema = "produccion")
class TLinea {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long f_id

    @Column(name="f_id_proceso")
    Long fIdProceso

    String f_descripcion
}
