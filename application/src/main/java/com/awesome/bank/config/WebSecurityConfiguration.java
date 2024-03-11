package com.awesome.bank.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.builders.WebSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
//import org.springframework.security.core.userdetails.User;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.provisioning.InMemoryUserDetailsManager;
//import org.springframework.security.web.SecurityFilterChain;
//
//import static org.springframework.security.config.Customizer.withDefaults;


/**
 * todo: add configuration to enable spring security using JWT filter
 */
@Configuration
//@EnableWebSecurity
public class WebSecurityConfiguration {

//    private static final String[] AUTH_WHITELIST = {
//            // -- Swagger UI v2
//            "/v2/api-docs",
//            "/swagger-resources",
//            "/swagger-resources/**",
//            "/configuration/ui",
//            "/configuration/security",
//            "/swagger-ui.html",
//            "/webjars/**",
//            // -- Swagger UI v3 (OpenAPI)
//            "/v3/api-docs/**",
//            "/swagger-ui/**"
//            // other public endpoints of your API may be appended to this array
//    };
//
//
//    @Override
//    protected void configure(HttpSecurity http) throws Exception {
//        http.
//                // ... here goes your custom security configuration
//                        authorizeRequests().
//                antMatchers(AUTH_WHITELIST).permitAll().  // whitelist Swagger UI resources
//                // ... here goes your custom security configuration
//                        antMatchers("**/accounts", "**/account/**/).hasRole("ADMIN").authenticated()  // require authentication for any endpoint that's not whitelisted
//                        antMatchers("**/withdraw", "**/deposit).hasAnyRole("ADMIN", "USER").authenticated();
//    }

//    /**
//     * If we decide to switch to a user from database we could implement the user details service and
//     * load our user from the database. For this purpose we will also need an encrypter to not store the passwords
//     * in the clear and to pass to the authentication provider.
//     *
//     * @return
//     */
//    @Bean
//    public UserDetailsService userDetailsService() {
//        // Unfortunately for this guy, he will have to deposit money in order to increase his balance,
//        // the account api will not be accessible for him
//        UserDetails user =
//                User.withDefaultPasswordEncoder()
//                        .username("user")
//                        .password("userPass")
//                        .roles("USER")
//                        .build();
//        // We will give this guy the right to create accounts and see them as well as
//        UserDetails admin =
//                User.withDefaultPasswordEncoder()
//                        .username("admin")
//                        .password("adminPass")
//                        .roles("ADMIN")
//                        .build();
//
//        return new InMemoryUserDetailsManager(user, admin);
//    }
}
