package com.study.spring.library.security;

import com.study.spring.library.dao.UserDao;
import com.study.spring.library.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class LibraryUserDetailsService implements UserDetailsService {

  private final UserDao userDao;

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    User user = userDao.findByUserName(username)
                       .orElseThrow(() -> new UsernameNotFoundException(String.format("User %s not found", username)));

    return new LibraryUserDetails(user);
  }
}
