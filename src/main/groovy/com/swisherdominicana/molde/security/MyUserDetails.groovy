package com.swisherdominicana.molde.security

import com.swisherdominicana.molde.model.TUsuarios
import com.swisherdominicana.molde.repository.UserRepository
import com.swisherdominicana.molde.service.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

@Service
class MyUserDetails implements UserDetailsService {

    @Autowired
    private UserRepository userRepository

    @Autowired
    UserService userService


    @Override
    UserDetails loadUserByUsername(String username) {
        try {
            //final TUsuarios tuser = userRepository.findByUsername(username)
            final TUsuarios tuser = userService.customFindByUsername(username)

            if (tuser == null) {
                throw new UsernameNotFoundException("User '" + username + "' not found")
            }

            return User//
                    .withUsername(username)//
                    .password(tuser.getPassword())//
                    .authorities(tuser.getAuthorities())//
                    .accountExpired(false)//
                    .accountLocked(false)//
                    .credentialsExpired(false)//
                    .disabled(false)//
                    .build()
        } catch(Exception e) {
            println e.getStackTrace()

        }
    }
}
