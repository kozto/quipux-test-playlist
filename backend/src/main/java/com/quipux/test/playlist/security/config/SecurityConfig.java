package com.quipux.test.playlist.security.config;

import com.quipux.test.playlist.security.jwt.JwtAuthFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * @author Oscar Chamorro
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true) // Para usar @PreAuthorize, @PostAuthorize
public class SecurityConfig {

    @Autowired
    private JwtAuthFilter jwtAuthFilter;

    @Autowired
    private UserDetailsService userDetailsService;
    
    @Autowired
    @Qualifier("customAuthEntryPoint")
    private AuthenticationEntryPoint authEntryPoint;    // AuthenticationEntryPoint personalizado

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(AbstractHttpConfigurer::disable) // Deshabilitar CSRF para APIs stateless
            .exceptionHandling(exception -> exception
                .authenticationEntryPoint(authEntryPoint) // <--- CONFIGURA TU ENTRY POINT AQUI
            )
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/auth/**", "/h2-console/**").permitAll() // Endpoints de autenticacion y H2 publicos
                .requestMatchers("/lists/**").authenticated() // Proteger /lists, requiere autenticacion
                // .requestMatchers(HttpMethod.GET, "/lists/**").hasAnyRole("USER", "ADMIN") // Autorizacion por rol
                // .requestMatchers(HttpMethod.POST, "/lists").hasRole("ADMIN")
                .anyRequest().authenticated()
            )
            .sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS) // API Stateless
            )
            .authenticationProvider(authenticationProvider())
            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class); // Añadir filtro JWT

        http.headers(headers -> headers.frameOptions(frameOptionsConfig -> frameOptionsConfig.disable()));

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        
        return config.getAuthenticationManager();
    }
}