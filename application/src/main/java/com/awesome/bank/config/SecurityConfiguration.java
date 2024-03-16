package com.awesome.bank.config;

import com.awesome.bank.security.filter.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static com.awesome.bank.security.user.Permission.ADMIN_DELETE;
import static com.awesome.bank.security.user.Role.ADMIN;
import static com.awesome.bank.security.user.Role.MANAGER;
import static com.awesome.bank.security.user.Role.USER;
import static org.springframework.http.HttpMethod.DELETE;
import static org.springframework.http.HttpMethod.POST;
import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;
import static org.springframework.security.web.util.matcher.RegexRequestMatcher.regexMatcher;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity
@ConditionalOnProperty(name="spring.security.enabled", havingValue="true", matchIfMissing=true)
public class SecurityConfiguration {

    private static final String[] WHITE_LIST_URL = {"/api/v1/auth/**",
            "/v2/api-docs",
            "/v3/api-docs",
            "/v3/api-docs/**",
            "/swagger-resources",
            "/swagger-resources/**",
            "/configuration/ui",
            "/configuration/security",
            "/swagger-ui/**",
            "/webjars/**",
            "/swagger-ui.html"};

    private final JwtAuthenticationFilter jwtAuthFilter;
    private final AuthenticationProvider authenticationProvider;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(req ->
                        req.requestMatchers(WHITE_LIST_URL)
                                .permitAll()
                                .requestMatchers(regexMatcher("^(\\/api\\/v1\\/bank\\/account\\/)[0-9]*\\/(withdraw|deposit).*$")).hasAnyRole(USER.name(), ADMIN.name())
                                // After a second thought, we definitely could have avoided this complex mapping by splitting the APIs by customer apis and admin/manager one!
                                .requestMatchers(POST, "/api/v1/bank/account/").hasAnyRole(ADMIN.name(), MANAGER.name())
                                // the order will imply that the Admin DELETE requests on /api/v1/bank/account/** will not be successful
                                // but other http method will succeed
                                .requestMatchers(DELETE, "/api/v1/bank/account/**").hasAuthority(ADMIN_DELETE.name())
                                .requestMatchers("/api/v1/bank/account/**").hasAnyRole(ADMIN.name(), MANAGER.name())
                                .requestMatchers("/api/v1/bank/accounts").hasAnyRole(ADMIN.name(), MANAGER.name())
                                .anyRequest()
                                .authenticated()
                )
                .sessionManagement(session -> session.sessionCreationPolicy(STATELESS))
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
