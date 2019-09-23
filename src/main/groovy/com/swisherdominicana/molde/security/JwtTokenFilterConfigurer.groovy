package com.swisherdominicana.molde.security

import org.springframework.security.config.annotation.SecurityConfigurerAdapter
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.web.DefaultSecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

class JwtTokenFilterConfigurer extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity>{

    JwtTokenProvider jwtTokenProvider

    JwtTokenFilterConfigurer(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider
    }

    @Override
    void configure(HttpSecurity http) throws Exception {
        JwtTokenFilter filter = new JwtTokenFilter(jwtTokenProvider)
        http.addFilterBefore(filter, UsernamePasswordAuthenticationFilter.class)
    }

}
