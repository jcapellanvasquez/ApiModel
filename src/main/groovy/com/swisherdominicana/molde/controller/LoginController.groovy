package com.swisherdominicana.molde.controller

import com.swisherdominicana.molde.exception.CustomException
import com.swisherdominicana.molde.model.RequestData
import com.swisherdominicana.molde.service.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("login")
class LoginController {

    @Autowired
    UserService userService

    @RequestMapping(value = "/get_token", method = RequestMethod.POST)
    def login(
            @RequestBody RequestData data
    ) {
        Map rs = [
                "token": userService.signin(
                        data.dataMap.get("username",""),
                        data.dataMap.get("password","")
                )
        ]
        return rs
    }

    @RequestMapping(value = "/get_menu", method = RequestMethod.POST)
    def getMenu(
            @RequestBody RequestData data
    ) {
        Map rs = ["menu": userService.getUsuarioMenu(data.dataMap.get("f_id_usuario",0).toLong())]
        return rs
    }



    @RequestMapping(value = "/get_permisos", method = RequestMethod.POST)
    def getPermisos(
            @RequestBody RequestData data
    ) {
        Map rs = ["permisos": userService.getAllPermisos(data.dataMap.get("f_id_usuario",0).toLong())]
        return rs
    }

    @RequestMapping(value="/set_permisos", method = RequestMethod.POST)
    def setPermisos(
            @RequestBody RequestData data
    ) {
        try {
            this.userService.setPermisos(data.dataMap)
            return [:]
        } catch (Exception e) {
            return new CustomException("Ocurrio un error", HttpStatus.UNPROCESSABLE_ENTITY)
        }
    }


}
