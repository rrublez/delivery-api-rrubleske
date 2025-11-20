package com.deliverytech.delivery.security;

import com.deliverytech.delivery.entity.Usuario;
import java.util.Collection;
import java.util.Optional;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

public final class SecurityUtils {

  private SecurityUtils() {
  }

  public static Optional<Authentication> getAuthentication() {
    return Optional.ofNullable(SecurityContextHolder.getContext().getAuthentication());
  }

  public static Optional<Usuario> getCurrentUser() {
    return getAuthentication()
        .filter(Authentication::isAuthenticated)
        .map(Authentication::getPrincipal)
        .flatMap(principal -> principal instanceof Usuario usuario ? Optional.of(usuario) : Optional.empty());
  }

  public static Long getCurrentUserId() {
    return getCurrentUser().map(Usuario::getId).orElse(null);
  }

  public static Optional<String> getCurrentUserEmail() {
    return getCurrentUser().map(Usuario::getEmail);
  }

  public static boolean hasRole(String role) {
    if (role == null) {
      return false;
    }
    String granted = role.startsWith("ROLE_") ? role : "ROLE_" + role;
    return getAuthentication()
        .map(Authentication::getAuthorities)
        .stream()
        .flatMap(Collection::stream)
        .map(GrantedAuthority::getAuthority)
        .anyMatch(authority -> authority.equals(granted));
  }
}
