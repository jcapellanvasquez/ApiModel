package com.swisherdominicana.molde.security

import com.swisherdominicana.molde.exception.CustomException
import com.swisherdominicana.molde.model.TUsuarios
import com.swisherdominicana.molde.model.Usuario
import io.jsonwebtoken.Claims
import io.jsonwebtoken.JwtException
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpStatus
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Component

import javax.annotation.PostConstruct
import javax.servlet.http.HttpServletRequest

@Component
class JwtTokenProvider {

    @Autowired
    private MyUserDetails myUserDetails


    // TODO generar una key mas seguro por el momento es de prueba
    @Value('${jwt.secret}')
    private String secretKey

    private long validityInMilliseconds = 3600000 * 10 // 1h
    //private long validityInMilliseconds = 3000 // 1h

    @PostConstruct
    protected void init() {
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes())
    }

    String createToken(String username) {

        Claims claims = Jwts.claims().setSubject(username)

        claims.put("auth", new SimpleGrantedAuthority("ROLE_ADMIN"))

        Date now = new Date()
        Date validity = new Date(now.getTime() + validityInMilliseconds)

        return Jwts.builder()//
                .setClaims(claims)//
                .setIssuedAt(now)//
                .setExpiration(validity)//
                .signWith(SignatureAlgorithm.HS256, secretKey)//
                .compact()
    }

    String createToken(String username, BigDecimal id, List<Map> permisos) {

        Claims claims = Jwts.claims().setSubject(username)

        // TODO este rol solo es para el test, preguntar mas adelante si se utilza algun tipo de estructura para manejar los roles
        claims.put("auth", new SimpleGrantedAuthority("ROLE_ADMIN"))
        claims.put("id", id)
        claims.put("permisos", permisos)

        Date now = new Date()
        Date validity = new Date(now.getTime() + validityInMilliseconds)

        return Jwts.builder()//
                .setClaims(claims)//
                .setIssuedAt(now)//
                .setExpiration(validity)//
                .signWith(SignatureAlgorithm.HS256, secretKey)//
                .compact()
    }

    String createToken(String username, TUsuarios usuario) {

        Claims claims = Jwts.claims().setSubject(username)

        // TODO este rol solo es para el test, preguntar mas adelante si se utilza algun tipo de estructura para manejar los roles
        claims.put("auth", new SimpleGrantedAuthority("ROLE_ADMIN"))
        claims.put("id", usuario.f_codigo_usuario)

        // Eliminamos la password
        usuario.setPassword("")

        claims.put("user", usuario)

        Date now = new Date()
        Date validity = new Date(now.getTime() + validityInMilliseconds)

        return Jwts.builder()//
                .setClaims(claims)//
                .setIssuedAt(now)//
                .setExpiration(validity)//
                .signWith(SignatureAlgorithm.HS256, secretKey)//
                .compact()
    }

    String createToken(Usuario usuario) {

        Claims claims = Jwts.claims().setSubject(usuario.username)

        // TODO este rol solo es para el test, preguntar mas adelante si se utilza algun tipo de estructura para manejar los roles
        claims.put("auth", new SimpleGrantedAuthority("ROLE_ADMIN"))
        claims.put("id", usuario.getId())

        // Eliminamos la password
        usuario.setPassword("")

        claims.put("user", usuario)

        Date now = new Date()
        Date validity = new Date(now.getTime() + validityInMilliseconds)

        return Jwts.builder()//
                .setClaims(claims)//
                .setIssuedAt(now)//
                .setExpiration(validity)//
                .signWith(SignatureAlgorithm.HS256, secretKey)//
                .compact()
    }

    Authentication getAuthentication(String token) {
        UserDetails userDetails = myUserDetails.loadUserByUsername(getUsername(token))
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities())
    }

    String getUsername(String token) {
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().getSubject()
    }

    String resolveToken(HttpServletRequest req) {
        String bearerToken = req.getHeader("Authorization")
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7)
        }
        return null
    }

    boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token)
            return true
        } catch (JwtException | IllegalArgumentException e) {
            throw new CustomException("Expired or invalid JWT token", HttpStatus.INTERNAL_SERVER_ERROR)
        }
    }

}
