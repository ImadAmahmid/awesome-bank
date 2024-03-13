package com.awesome.bank.security.service;

import com.awesome.bank.security.api.model.AuthenticationRequest;
import com.awesome.bank.security.api.model.AuthenticationResponse;
import com.awesome.bank.security.user.User;
import com.awesome.bank.security.utils.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DefaultAuthenticationService implements AuthenticationService<User> {
  private final AuthenticationManager authenticationManager;
  private final JwtUtils jwtUtils;
  private final InMemoryUserDetailsManager userDetailsService;

  public AuthenticationResponse authenticate(AuthenticationRequest request) {
    String username = request.getUsername();
    String password = request.getPassword();

    authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));

    String jwtToken = jwtUtils.generateToken(User.builder().username(username).password(password).build());

    return AuthenticationResponse.builder()
            .accessToken(jwtToken)
            .build();
  }

  @Override
  public AuthenticationResponse register(User user) {
    userDetailsService.createUser(user);

    String jwtToken = jwtUtils.generateToken(user);

    return AuthenticationResponse.builder()
            .accessToken(jwtToken)
            .build();
  }

  @Override
  public void logout(String token) {
    return;
  }

}
