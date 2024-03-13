package com.awesome.bank.security.service;

import com.awesome.bank.security.api.model.AuthenticationRequest;
import com.awesome.bank.security.api.model.AuthenticationResponse;
import org.springframework.security.core.userdetails.UserDetails;

public interface AuthenticationService<T extends UserDetails> {

    /**
     * Register a new user in the user persistence (not implemented)
     */
    AuthenticationResponse register(T user);

    /**
     * Authenticate a user
     */
    AuthenticationResponse authenticate(AuthenticationRequest authenticationRequest);

    /**
     * Revoke the user tokens that would be saved in db
     */
    void logout(String token);
}
