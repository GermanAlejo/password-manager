package com.passwordmanager.password_manager.model;

import java.util.Collection;
import java.util.List;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class UserDetailsImpl implements UserDetails {

  private final User user;

  public UserDetailsImpl(User user) {
    this.user = user;
  }

  // Return roles/authorities as needed. For now:
  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return List.of();
  }

  @Override
  public String getPassword() {
    return user.getMasterPasswordHash();
  }

  @Override
  public String getUsername() {
    return user.getUsername();
  }

  // implement other methods like isAccountNonExpired, etc., return true if unused

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return true;
  }
}
