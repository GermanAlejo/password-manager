package com.passwordmanager.password_manager.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import com.passwordmanager.password_manager.model.User;
import com.passwordmanager.password_manager.model.UserDetailsImpl;
import com.passwordmanager.password_manager.repository.UserRepository;

public class UserDetailsServiceImpl implements UserDetailsService {

  private final UserRepository userRepository;

  public UserDetailsServiceImpl(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  @Override
  public UserDetails loadUserByUsername(final String username) throws UsernameNotFoundException {
    User user = userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User not found"));
    return new UserDetailsImpl(user);
  }
}
