package com.swisherdominicana.molde.controller

import com.swisherdominicana.molde.model.Cliente
import com.swisherdominicana.molde.model.Interacciones
import com.swisherdominicana.molde.model.Response
import com.swisherdominicana.molde.model.Usuario
import com.swisherdominicana.molde.repository.ClienteRepository
import com.swisherdominicana.molde.repository.InteraccionRepository
import com.swisherdominicana.molde.repository.UsuarioRepository
import com.swisherdominicana.molde.service.SqlService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController

@RestController
class InteraccionController {

    @Autowired
    InteraccionRepository interaccionRepository

    @Autowired
    ClienteRepository clienteRepository

    @Autowired
    UsuarioRepository usuarioRepository

    @Autowired
    SqlService SQL


    @RequestMapping(value="interacciones", method = RequestMethod.GET)
    def getInteracciones() {
        Response<List<Interacciones>> rs = new Response<>()
        rs.setData(this.interaccionRepository.findAll())
        return rs
    }

    @RequestMapping(value="interaccion", method=RequestMethod.POST)
    def setInteracciones(
            @RequestBody Response<Interacciones> http
    ) {
        try {
            Interacciones interaccion = new Interacciones()
            Cliente cliente =  this.clienteRepository.findByIdAndActivo(http.getData().getCliente().getId(),true)
            Usuario usuario = this.usuarioRepository.findById(http.getData().getUsuario().getId())
            interaccion.setCliente(cliente)
            interaccion.setUsuario(usuario)
            interaccion.setMemo(http.getData().getMemo())
            interaccion.setFecha(SQL.getCurrentTimestampFormBD())

            this.interaccionRepository.save(interaccion)
            http.statusCode = 201
            http.message = 'Registro exitoso'
            return http
        } catch(Exception e) {
            e.printStackTrace()
            http.statusCode = -1
            http.message = 'Registro fallido'
            return http
        }
    }

    @RequestMapping(value="interaccion", method=RequestMethod.PUT)
    def updateInteracciones(
            @RequestBody Response<Interacciones> http
    ) {
        try {

            Interacciones interaccion = this.interaccionRepository.findById(http.getData().getId())
            interaccion.setMemo(http.getData().getMemo())
            this.interaccionRepository.save(interaccion)
            http.statusCode = 201
            http.message = 'Registro exitoso'
            return http
        } catch(Exception e) {
            e.printStackTrace()
            http.statusCode = -1
            http.message = 'Registro fallido'
            return http
        }
    }


}
