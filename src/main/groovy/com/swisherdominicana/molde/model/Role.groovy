package com.swisherdominicana.molde.model

import org.springframework.security.core.GrantedAuthority

enum Role implements GrantedAuthority{
    ROLE_ADMIN, ROLE_CLIENT;

    String getAuthority() {
        return name()
    }
}
