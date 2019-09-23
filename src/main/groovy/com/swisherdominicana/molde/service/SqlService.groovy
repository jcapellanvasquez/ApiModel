package com.swisherdominicana.molde.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Service

import javax.sql.DataSource
import java.sql.Timestamp

@Service
class SqlService {

    @Autowired
    JdbcTemplate jdbcTemplate

    Map executeQueryAsMap(String query) {

        try {
            return jdbcTemplate.queryForMap(query)
        } catch (Exception e) {
            println e.getStackTrace()
            return [:]
        }
    }

    DataSource getDataSource() {
        return this.jdbcTemplate.dataSource
    }

    List executeQueryAsList(String query) {

        try {
            return jdbcTemplate.queryForList(query)
        } catch (Exception e) {
            println e.getStackTrace()
            println e.getMessage()
            return []
        }
    }

    void executeQueryInsertUpdate(String query) {
        jdbcTemplate.update(query)
    }

    Long getSecuenciaDocumento(String tipoDocumento) {
        Long secuencia = 0
        secuencia = jdbcTemplate.queryForObject("select get_secuencia('${tipoDocumento}')", Long.class)
        return secuencia
    }

    Long getSecuencia(String tipoDocumento) {
        Long secuencia = 0
        secuencia = jdbcTemplate.queryForObject("select get_secuencia('${tipoDocumento}'::character varying)", Long.class)
        return secuencia
    }

    String getTipoDocumento(Long indiceOrdernador) {
        String tipoDocumento = ""
        tipoDocumento = jdbcTemplate.queryForObject("select f_tipodoc from t_tiposdocumentos where f_indiceordenador = ${indiceOrdernador}", String.class)
        return tipoDocumento
    }

    Long getIndiceOrdenadorByConceptoId(Long conceptoId) {
        Long indiceOrdenador = 0
        indiceOrdenador = jdbcTemplate.queryForObject("""
                    SELECT 
                      f_tipo_documento
                    FROM 
                      public.t_conceptos_entrada_inv where f_id = ${conceptoId}
        """, Long.class)
        return indiceOrdenador
    }

    String getSecuenciaDocumentoFormateado(String tipoDocumento, Long secuencia) {
        String wholenum = ""
        wholenum = jdbcTemplate.queryForObject("select get_wholenum('${tipoDocumento}',${secuencia})", String.class)
        return wholenum
    }

    Date getCurrentDateFormBD() {
        return jdbcTemplate.queryForObject("select now()",Date.class)
    }

    Timestamp getCurrentTimestampFormBD() {
        return jdbcTemplate.queryForObject("select now()",Timestamp.class)
    }

}
