package com.swisherdominicana.molde.controller

import com.swisherdominicana.molde.model.LoginCredential
import com.swisherdominicana.molde.model.Response
import com.swisherdominicana.molde.service.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("login")
class LoginController {

    @Autowired
    UserService userService

    @RequestMapping(value = "/get_token", method = RequestMethod.POST)
    def login(
            @RequestBody Response<LoginCredential> http
    ) {

        Response<LoginCredential> rs = new Response<LoginCredential>()

        try {

            rs.setData(new LoginCredential())
            rs.getData().setToken(userService.signin(http.getData().getUsername(),http.getData().getPassword()))
            return rs
        }
        catch (Exception e) {
            rs.setStatusCode(500)
            rs.setMessage(e.getMessage())
            return rs
        }
    }

    @RequestMapping(value = "/get_token", method = RequestMethod.GET)
    def getLogin(
            @RequestBody Response<LoginCredential> http
    ) {

        Response<LoginCredential> rs = new Response<LoginCredential>()

        try {

            rs.setData(new LoginCredential())
            rs.getData().setToken(userService.signin(http.getData().getUsername(),http.getData().getPassword()))
            return rs
        }
        catch (Exception e) {
            rs.setStatusCode(500)
            rs.setMessage(e.getMessage())
            return rs
        }
    }

//    @RequestMapping(value = "/get_menu", method = RequestMethod.POST)
//    def getMenu(
//            @RequestBody RequestData data
//    ) {
//        Map rs = ["menu": userService.getUsuarioMenu(data.dataMap.get("f_id_usuario",0).toLong())]
//        return rs
//    }


//
//    @RequestMapping(value = "/get_permisos", method = RequestMethod.POST)
//    def getPermisos(
//            @RequestBody RequestData data
//    ) {
//        Map rs = ["permisos": userService.getAllPermisos(data.dataMap.get("f_id_usuario",0).toLong())]
//        return rs
//    }

//    @RequestMapping(value="/set_permisos", method = RequestMethod.POST)
//    def setPermisos(
//            @RequestBody RequestData data
//    ) {
//        try {
//            this.userService.setPermisos(data.dataMap)
//            return [:]
//        } catch (Exception e) {
//            return new CustomException("Ocurrio un error", HttpStatus.UNPROCESSABLE_ENTITY)
//        }
//    }


}
