package com.ust.employee.configuration;



import com.ust.employee.service.JwtService;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * This class is responsible for intercepting the incoming requests and extracting the JWT token from the
 * Authorization header. It then validates the token, extracts the user information, and sets the authentication
 * in the SecurityContextHolder. This filter is called once per request by extending the OncePerRequestFilter class.
 */
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    /**
     * This method intercepts the incoming requests and extracts the JWT token from the Authorization header.
     * It then validates the token, extracts the user information, and sets the authentication in the SecurityContextHolder.
     *
     * @param request     the incoming request object
     * @param response    the response object
     * @param filterChain the filter chain object
     * @throws ServletException if there is an error in the servlet
     * @throws IOException      if there is an error with I/O
     */
    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        // Print the requested URI
        System.out.println(request.getRequestURI());

        // Extract the JWT token from the Authorization header
        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String userEmail;
        HttpServletRequest httpServletRequest = null;
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {

            // If the Authorization header is not present or the token is invalid, proceed with the request without
            // authentication
            filterChain.doFilter(request, response);
            return;
        }
        jwt = authHeader.substring(7);

        //userEmail = jwtService.extractUsername(jwt);
        try {
            userEmail = jwtService.extractUsername(jwt);
        } catch (ExpiredJwtException ex) {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.setContentType("application/json");
            response.getWriter().write("{\"message\": \"" + ex.getMessage() + "\", \"tokenStatus\": \"Jwt Token Time Expired\"}");
            return;
        }
        if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            // Load the user details from the database using the user email
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(userEmail);

            // Validate the JWT token


            if (jwtService.isTokenValid(jwt, userDetails)) {
                // If the token is valid, create an Authentication object and set it in the SecurityContextHolder
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities()
                );
                authToken.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request)
                );
                SecurityContextHolder.getContext().setAuthentication(authToken);

            }

        }



        // Proceed with the request
        filterChain.doFilter(request, response);
    }
}

