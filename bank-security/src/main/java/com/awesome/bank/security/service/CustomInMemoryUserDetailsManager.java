package com.awesome.bank.security.service;

import com.awesome.bank.security.api.model.AuthenticationRequest;
import com.awesome.bank.security.api.model.AuthenticationResponse;
import com.awesome.bank.security.filter.JwtAuthenticationFilter;
import com.awesome.bank.security.user.User;
import com.awesome.bank.security.utils.JwtUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.util.Assert;

import java.util.HashMap;
import java.util.Map;

/**
 * This class exists because Spring default CustomInMemoryUserDetailsManager doesn't return
 * the roles the way I wanted! At the end I had no choice but to override it to
 * return the instance of my user.
 *
 * @see org.springframework.security.provisioning.InMemoryUserDetailsManager
 */
@Slf4j
@RequiredArgsConstructor
public class CustomInMemoryUserDetailsManager extends InMemoryUserDetailsManager {

    private final Map<String, UserDetails> users = new HashMap<>();

    @Override
    public void createUser(UserDetails user) {
        Assert.isTrue(!userExists(user.getUsername()), "User does not have username");

        users.put(user.getUsername().toLowerCase(), (User) user);
    }

    @Override
    public void updateUser(UserDetails user) {
        Assert.isTrue(userExists(user.getUsername()), "User does not exist");

        users.put(user.getUsername().toLowerCase(), (User) user);
    }

    @Override
    public void deleteUser(String username) {
        users.remove(username.toLowerCase());
    }

    @Override
    public void changePassword(String oldPassword, String newPassword) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean userExists(String username) {
        return users.containsKey(username.toLowerCase());
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = (User) users.get(username.toLowerCase());

        if (user == null) {
            throw new UsernameNotFoundException(username);
        }

        return user;
    }
}