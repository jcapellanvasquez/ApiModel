package com.swisherdominicana.molde.model

import javax.persistence.*

@Entity
@Table(name="t_maquinas", schema = "produccion")
class TMaquina {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long f_id

    Long f_id_proceso

    @Column(name="f_id_linea_produccion")
    Long fIdLinea

    @Column(name="f_orden")
    Long fOrden

    String f_descripcion
}
