package com.passwordmanager.password_manager.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import com.passwordmanager.password_manager.model.User;
import com.passwordmanager.password_manager.model.UserDetailsImpl;
import com.passwordmanager.password_manager.repository.UserRepository;
import org.springframework.stereotype.Service;

//TODO: This might not be needed
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

  private final UserService userService;

  public UserDetailsServiceImpl(UserService userService) {
    this.userService = userService;
  }

  @Override
  public UserDetails loadUserByUsername(final String identifier) throws UsernameNotFoundException {
    User user = userService.findUserByUsernameOrEmail(identifier);
    return new UserDetailsImpl(user);
  }
}
