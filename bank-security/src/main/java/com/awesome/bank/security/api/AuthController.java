package com.awesome.bank.security.api;


import com.awesome.bank.security.api.model.AuthenticationRequest;
import com.awesome.bank.security.api.model.AuthenticationResponse;
import com.awesome.bank.security.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * To be improved:
 *  - Use swagger to generate this controller
 *  - Add the register end point
 *  - Add the log out end point mechanisms
 *
 */
@RequiredArgsConstructor
@Slf4j
@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {
    private final AuthenticationService service;

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest request
    ) {
        LOG.info("[Auth API] Authenticating | username={}", request.getUsername());
        return ResponseEntity.ok(service.authenticate(request));
    }

}
