package com.example.orn.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * Small helper used by the service layer to find out WHO is currently
 * logged in (from the SecurityContext populated by {@link JwtAuthenticationFilter})
 * and whether that user is an ADMIN.
 *
 * This does not change how authentication works - it only reads the
 * Authentication object that the existing JWT filter already puts into
 * the SecurityContext.
 */
public final class SecurityUtils {

    private SecurityUtils() {
    }

    /**
     * @return the username of the currently authenticated user, or null if
     *         there is no authenticated user (should not normally happen on
     *         endpoints protected by Spring Security, but handled defensively).
     */
    public static String getCurrentUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return null;
        }
        return authentication.getName();
    }

    /**
     * @return true if the currently authenticated user has the ADMIN role.
     */
    public static boolean isCurrentUserAdmin() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            return false;
        }
        for (GrantedAuthority authority : authentication.getAuthorities()) {
            if ("ROLE_ADMIN".equals(authority.getAuthority())) {
                return true;
            }
        }
        return false;
    }
}
