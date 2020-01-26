package com.swisherdominicana.molde.controller

import com.swisherdominicana.molde.model.Cliente
import com.swisherdominicana.molde.model.Response
import com.swisherdominicana.molde.model.Usuario
import com.swisherdominicana.molde.repository.UsuarioRepository
import com.swisherdominicana.molde.utils.HashUtil
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController


@RestController
class UsuarioController {

    @Autowired
    UsuarioRepository usuarioRepository

    @RequestMapping(value="usuarios", method = RequestMethod.GET)
    def getUsuarios() {

        Response<List<Usuario>> rs = new Response<>()
        rs.setData(this.usuarioRepository.findAll())
        return rs
    }

    @RequestMapping(value="usuario", method=RequestMethod.POST)
    def setUsuario(
            @RequestBody Response<Usuario> http
    ) {
        try {
            Usuario usuario = http.getData()
            usuario.setPassword(HashUtil.getMD5(http.getData().getPassword()))
            this.usuarioRepository.save(usuario)
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

    @RequestMapping(value="usuario", method=RequestMethod.PUT)
    def updateUsuario(
            @RequestBody Response<Usuario> http
    ) {
        try {

            Usuario usuario = this.usuarioRepository.findById(http.getData().getId())

            usuario.setNombre(http.getData().getNombre())
            usuario.setApellido(http.getData().getApellido())
            usuario.setUsername(http.getData().getUsername())

            if (http.getData().getPassword() != null && !http.getData().getPassword().isEmpty()) {
                usuario.setPassword(HashUtil.getMD5(http.getData().getPassword()))
            }

            this.usuarioRepository.save(usuario)
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
