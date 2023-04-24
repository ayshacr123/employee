package com.ust.employee.configuration;



import com.ust.employee.service.CustomUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.servlet.http.HttpServletResponse;


/**
 * Configures the security settings for the application.
 */
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfiguration {
    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    private final JwtAuthenticationFilter jwtAuthFilter;


    /**
     * Defines the password encoder used to encrypt user passwords.
     *
     * @return The password encoder instance.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Defines the security filter chain that will be used to secure the application.
     *
     * @param http The HTTP security object to configure.
     * @return The security filter chain instance.
     * @throws Exception if an error occurs while configuring the security filter chain.
     */

    //Create a bean for security filter chain to define the security configurations.
    //Permit requests with "/api/v1/user/**" URL without authentication.
    // For all other requests, authentication is required.
    // Session will be stateless, i.e., no session information will be stored on the server.
    // Add authentication provider to validate user's authentication.
    // Add JwtAuthenticationFilter before UsernamePasswordAuthenticationFilter to validate the token.
    //Handle exception when user is not authorized to access a resource.
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf()
                .disable()
                .authorizeHttpRequests().requestMatchers
                        (new AntPathRequestMatcher("/api/v1/user/**"))
                .permitAll().and().authorizeHttpRequests().anyRequest().authenticated()
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authenticationProvider(authenticationProvider())
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling().authenticationEntryPoint((request, response, authException) -> {
                    // Return 401 Unauthorized status
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                })
                .accessDeniedHandler((request, response, accessDeniedException) -> {
                    // Return 401 Unauthorized status
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                })
                .and().cors();

        return http.build();
    }

    /**
     * Defines the authentication provider used to authenticate users.
     *
     * @return The authentication provider instance.
     */
    // Create an AuthenticationProvider bean to authenticate users.
    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(customUserDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    /**
     * Defines the authentication manager used to manage authentication.
     *
     * @param config The authentication configuration object.
     * @return The authentication manager instance.
     * @throws Exception if an error occurs while configuring the authentication manager.
     */

    // Create an AuthenticationManager bean to manage authentication of users.
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}
