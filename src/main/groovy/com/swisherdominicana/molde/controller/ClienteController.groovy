package com.swisherdominicana.molde.controller

import com.swisherdominicana.molde.model.Cliente
import com.swisherdominicana.molde.model.Response
import com.swisherdominicana.molde.repository.ClienteRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.RestController


@RestController
class ClienteController {

    @Autowired
    ClienteRepository clienteRepository


    @RequestMapping(value="clientes", method=RequestMethod.GET)
    def getClientes() {
        Response<List<Cliente>> rs = new Response<>()
        rs.setData(this.clienteRepository.findAllByActivo(true))
        return rs
    }

    @RequestMapping(value="cliente", method=RequestMethod.POST)
    def setClientes(
            @RequestBody Response<Cliente> http
    ) {
        try {
            this.clienteRepository.save(http.getData())
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

    @RequestMapping(value="cliente", method=RequestMethod.PUT)
    def updateClientes(
            @RequestBody Response<Cliente> http
    ) {
        try {

            Cliente cliente = this.clienteRepository.findByIdAndActivo(http.getData().getId(), true)

            cliente.setEmail(http.getData().getEmail())
            cliente.setNombre(http.getData().getNombre())
            cliente.setTelefono(http.getData().getTelefono())
            cliente.setActivo(http.getData().getActivo())

            this.clienteRepository.save(cliente)
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
