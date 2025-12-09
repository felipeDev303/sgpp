package ipss.cl.sgpp.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity // Habilita la seguridad de Spring
public class SecurityConfig {

    // 1. Define cómo se deben codificar las contraseñas
    @Bean
    public PasswordEncoder passwordEncoder() {
        // BCrypt es el estándar de la industria para hashear contraseñas.
        return new BCryptPasswordEncoder();
    }

    // 2. Define las Reglas de Acceso (Autorización)
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable()) // Deshabilitamos CSRF (típico en APIs REST)
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // API REST no usa sesiones
            .authorizeHttpRequests(authorize -> authorize
                // Permisos para Prácticas (Indicador 1: Diferenciación de Perfiles)
                .requestMatchers("/api/v1/practicas").hasAnyAuthority("PROFESOR", "ESTUDIANTE")
                // Permisos para 'Crear' (POST) son para ambos, pero la URL es la misma. 
                // La diferencia de rol la aplicaremos después con el ID de usuario.
                
                // Los Profesores tienen permisos de Actualizar y Eliminar (U, D)
                .requestMatchers("/api/v1/practicas/**").hasAuthority("PROFESOR")
                
                .requestMatchers("/api/v1/auth/**").permitAll() 
                
                // Permite acceder a Swagger UI (Documentación)
                .requestMatchers("/v3/api-docs/**", "/swagger-ui/**").permitAll()
                
                // Cualquier otra solicitud requiere autenticación (login)
                .anyRequest().authenticated()
            );

        
        
        return http.build();
    }
}