package com.pucetec.roles.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter
import org.springframework.security.web.SecurityFilterChain

@Configuration
class SecurityConfig {

    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        http
            .csrf { it.disable() }
            .sessionManagement { it.sessionCreationPolicy(SessionCreationPolicy.STATELESS) }
            .authorizeHttpRequests { auth ->
                auth
                    .requestMatchers(HttpMethod.GET, "/api/events", "/api/events/**").permitAll()
                    .requestMatchers(HttpMethod.POST, "/api/events").hasRole("ADMIN")
                    .requestMatchers(HttpMethod.POST, "/api/reservations").hasRole("USER")
                    .requestMatchers(HttpMethod.PUT, "/api/reservations/**").hasRole("USER")
                    .requestMatchers(HttpMethod.POST, "/api/attendees").authenticated()
                    .anyRequest().authenticated()
            }
            .oauth2ResourceServer { oauth2 -> oauth2.jwt { it.jwtAuthenticationConverter(jwtAuthenticationConverter()) } }
        return http.build()
    }

    private fun jwtAuthenticationConverter(): JwtAuthenticationConverter {
        val converter = JwtAuthenticationConverter()
        converter.setJwtGrantedAuthoritiesConverter { jwt ->
            val groups = jwt.getClaimAsStringList("cognito:groups") ?: emptyList()
            groups.map { SimpleGrantedAuthority("ROLE_$it") }
        }
        return converter
    }
}
