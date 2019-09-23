package com.swisherdominicana.molde.security


import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.builders.WebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.password.MessageDigestPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.CorsConfigurationSource
import org.springframework.web.cors.UrlBasedCorsConfigurationSource

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
class WebSecurityConfig  extends WebSecurityConfigurerAdapter {

    @Autowired
    private JwtTokenProvider jwtTokenProvider

    @Bean
    @Override
    protected AuthenticationManager authenticationManager() throws Exception {
        return super.authenticationManager()
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        try {
            // Disable CSRF (cross site request forgery)
            http.csrf().disable()

            http.cors()

            // No session will be created or used by spring security
            http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)

            // Entry points
            http.authorizeRequests()//
                    .antMatchers("/login/get_token").permitAll()//
                    .antMatchers("/login/get_hash").permitAll()
                    .antMatchers("/h2-console/**/**").permitAll()
            // Disallow everything else..
                    .anyRequest().authenticated()

            // If a user try to access a resource without having enough permissions
            http.exceptionHandling().accessDeniedPage("/login")

            // Apply JWT
            http.apply(new JwtTokenFilterConfigurer(jwtTokenProvider))

            // Optional, if you want to test the API from a browser
            // http.httpBasic()
        } catch(Exception e ) {
            prinln e.getStackTrace()
        }
    }

    @Override
     void configure(WebSecurity web) throws Exception {
        // Allow swagger to be accessed without authentication
        web.ignoring()
                .antMatchers(HttpMethod.OPTIONS)
                .antMatchers("/v2/api-docs")//
                .antMatchers("/swagger-resources/**")//
                .antMatchers("/swagger-ui.html")//
                .antMatchers("/configuration/**")//
                .antMatchers("/webjars/**")//

        // Un-secure H2 Database (for testing purposes, H2 console shouldn't be unprotected in production)
                .and()
                .ignoring()
                .antMatchers("/h2-console/**/**")
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new MessageDigestPasswordEncoder("MD5")
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource()
    {
        CorsConfiguration configuration = new CorsConfiguration()
        configuration.setAllowedOrigins(Arrays.asList("*"))
        configuration.setAllowedMethods(Arrays.asList("GET","POST", "OPTIONS"))
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource()
        source.registerCorsConfiguration("/**", configuration)
        return source
    }

}
