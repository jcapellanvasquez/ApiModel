package com.swisherdominicana.molde.service

import com.swisherdominicana.molde.exception.CustomException
import com.swisherdominicana.molde.model.TUsuarios
import com.swisherdominicana.molde.model.Usuario
import com.swisherdominicana.molde.repository.UserRepository
import com.swisherdominicana.molde.repository.UsuarioRepository
import com.swisherdominicana.molde.security.JwtTokenProvider
import com.swisherdominicana.molde.utils.HashUtil
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.jdbc.core.BeanPropertyRowMapper
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

import javax.servlet.http.HttpServletRequest
import java.util.concurrent.ExecutionException

@Service
class UserService {

    @Autowired
    private UserRepository userRepository

    @Autowired
    private UsuarioRepository usuarioRepository

    @Autowired
    PasswordEncoder passwordEncoder

    @Autowired
    private JwtTokenProvider jwtTokenProvider

    @Autowired
    private AuthenticationManager authenticationManager

    @Autowired
    private JdbcTemplate jdbcTemplate

    @Autowired
    private SqlService sqlService

    String signin(String username, String password) throws Exception {

        Usuario user

        try {
            //password = HashUtil.getMD5(password).toUpperCase()
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password))
            user = usuarioRepository.findByUsername(username)
            return jwtTokenProvider.createToken(user)
        }
        catch (Exception e) {
           // e.printStackTrace()
           // throw new CustomException("Invalid username/password supplied", HttpStatus.UNPROCESSABLE_ENTITY)
            throw new UsernameNotFoundException("Usuario no pudo ser encontrado: " + username)
        }
    }

    String signin2(String username, String password) {

        return passwordEncoder.encode(password)
    }


    String signup(TUsuarios user) {
        if (!userRepository.existsByUsername(user.getUsername())) {
            user.setPassword(passwordEncoder.encode(user.getPassword()))
            userRepository.save(user)
            return jwtTokenProvider.createToken(user.getUsername())
        } else {
            throw new CustomException("Username is already in use", HttpStatus.UNPROCESSABLE_ENTITY)
        }
    }


    TUsuarios search(String username) {
        TUsuarios user = userRepository.findByUsername(username)
        if (user == null) {
            throw new CustomException("The user doesn't exist", HttpStatus.NOT_FOUND)
        }
        return user
    }


    Usuario customFindByUsername(String username) {
        String query = "select \n" +
                "substring(a.passwd,4,length(a.passwd)) as f_password,\n" +
                "    b.f_codigo_usuario,\n" +
                "    b.f_id_usuario\n" +
                "from pg_shadow a\n" +
                "     inner join t_usuario b on a.usename = b.f_id_usuario\n" +
                "where\n" +
                "a.usename = '${username}'"

        Map u = jdbcTemplate.queryForMap(query)


        Usuario user = new Usuario()

        user.setF_codigo_usuario(u.get("f_codigo_usuario", 0).toString().toBigDecimal())
        user.setUsername(u.get("f_id_usuario", "").toString())
        user.setPassword(u.get("f_password", "").toString())


        if (user == null) {
            throw new CustomException("The user doesn't exist", HttpStatus.NOT_FOUND)
        }

        return user
    }

    TUsuarios whoami(HttpServletRequest req) {
        return userRepository.findByUsername(jwtTokenProvider.getUsername(jwtTokenProvider.resolveToken(req)))
    }

    String refresh(String username) {
        return jwtTokenProvider.createToken(username)
    }

    List<Map> getUsuarioMenu(Long f_id_usuario) {
        return sqlService.executeQueryAsList("SELECT \n" +
                "  m.f_id,\n" +
                "  m.f_descripcion,\n" +
                "  m.f_url,\n" +
                "  m.f_icon\n" +
                "FROM \n" +
                "  control_calidad.t_menu_web m \n" +
                "  inner join control_calidad.t_usuario_menu um \n" +
                "  ON m.f_id=um.f_id_menu\n" +
                "  where um.f_id_usuario=${f_id_usuario} order by f_orden")
    }

    List<Map> getAllPermisos(Long f_id_usuario) {
        return sqlService.executeQueryAsList("\n" +
                "SELECT \n" +
                "  m.f_id,\n" +
                "  m.f_descripcion,\n" +
                "  m.f_url,\n" +
                "  m.f_icon,\n" +
                "  case when um.f_id_usuario is null then FALSE ELSE TRUE END as f_tiene_permiso\n" +
                "FROM \n" +
                "  control_calidad.t_menu_web m\n" +
                "  LEFT JOIN control_calidad.t_usuario_menu um \n" +
                "  ON m.f_id=um.f_id_menu and um.f_id_usuario = ${f_id_usuario} order by m.f_orden")
    }

    void setPermisos(Map data) {
        List<Map> permisos = data.get("permisos") as List
        sqlService.executeQueryInsertUpdate("DELETE FROM control_calidad.t_usuario_menu WHERE f_id_usuario = ${data.get("f_id_usuario",0)}")
        permisos.each { row->
            if (row.get("f_tiene_permiso")) {
                sqlService.executeQueryInsertUpdate("INSERT INTO \n" +
                        "  control_calidad.t_usuario_menu\n" +
                        "(\n" +
                        "  f_id_menu,\n" +
                        "  f_id_usuario\n" +
                        ")\n" +
                        "VALUES (\n" +
                        "  ${row.get("f_id",0)},\n" +
                        "  ${data.get("f_id_usuario",0)}\n" +
                        ");")
            }
        }
    }

    List<TUsuarios> getUsuarios(Long f_codigo_usuario, String nombre, Long f_id_empleado) {
        String query = """
                select u.f_id_empleado,u.f_editar_peso,u.f_cancelar_canasta,u.f_codigo_usuario, u.f_nombre,u.f_id_usuario,u.f_auditor,u.f_activo, u.f_apellido, '' as f_password 
                from t_usuario u where u.f_activo = true """

        query += !nombre.isEmpty() ? " and (u.f_nombre ilike '${nombre}%' OR u.f_apellido ilike '${nombre}%')" : ""
        query += f_codigo_usuario > 0 ? " and u.f_codigo_usuario = ${f_codigo_usuario}" : ""
        query += f_id_empleado > 0 ? " and u.f_id_empleado = ${f_id_empleado}" : ""
        query += " order by u.f_codigo_usuario DESC "
        return sqlService.getJdbcTemplate().query(query,new BeanPropertyRowMapper(TUsuarios))
    }
}
