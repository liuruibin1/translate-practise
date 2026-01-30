package com.xxx.security.configuration;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.*;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

import static org.apache.http.HttpStatus.SC_UNAUTHORIZED;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true)
public class SecurityConfiguration {

    @Value("${spring.profiles.active}")
    String springProfilesActive;

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (webSecurity) -> webSecurity.ignoring().requestMatchers("/resources/**");
    }

    public CorsConfigurationSource corsConfigurationSource() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration corsConfiguration = new CorsConfiguration();

        //TODO 非生产环境
        //if (!springProfilesActive.equals(SpringProfilesConstant.PROD)) {
        //    corsConfiguration.setAllowedOrigins(
        //            Arrays.asList("http://127.0.0.1:30021", "http://127.0.0.1:30031", "http://127.0.0.1:5173")
        //    );
        //} else {
        //    corsConfiguration.setAllowedOrigins(List.of("*"));
        //}
        corsConfiguration.setAllowedOrigins(List.of("*"));
        corsConfiguration.setAllowedMethods(List.of("*"));
        corsConfiguration.setAllowedHeaders(List.of("*"));
        corsConfiguration.setExposedHeaders(List.of("*"));
        corsConfiguration.setAllowCredentials(false);
        corsConfiguration.setMaxAge(3600L);
        source.registerCorsConfiguration("/**", corsConfiguration);
        return source;
    }


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        // CORS
        Customizer<CorsConfigurer<HttpSecurity>> corsCustomizer = corsConfigurer
                -> corsConfigurer.configurationSource(corsConfigurationSource());
        httpSecurity.cors(corsCustomizer);

        // Disable CSRF
        httpSecurity.csrf(AbstractHttpConfigurer::disable);

        // Disable FormLogin
        httpSecurity.formLogin(AbstractHttpConfigurer::disable);

        //http.formLogin().loginPage("/auth/login").and();
        //http = http.logout().logoutUrl("/auth/logout").and();

        // Disable BasicAuthenticationFilter
        Customizer<LogoutConfigurer<HttpSecurity>> logoutCustomizer = httpSecurityLogoutConfigurer
                -> httpSecurityLogoutConfigurer.logoutUrl("/auth/logout");
        httpSecurity.logout(logoutCustomizer);

        // Set session management to stateless
        Customizer<SessionManagementConfigurer<HttpSecurity>> sessionManagementCustomizer = sessionManagementConfigurer
                -> sessionManagementConfigurer
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        httpSecurity.sessionManagement(sessionManagementCustomizer);

        // Set unauthorized requests exception handler
        Customizer<ExceptionHandlingConfigurer<HttpSecurity>> ExceptionHandlingCustomizer =
                exceptionHandlingConfigurer
                        -> exceptionHandlingConfigurer.authenticationEntryPoint((request, response, ex)
                        -> response.sendError(SC_UNAUTHORIZED, ex.getMessage()));
        httpSecurity.exceptionHandling(ExceptionHandlingCustomizer);

        httpSecurity.exceptionHandling((configure) -> configure
                .accessDeniedHandler((request, response, ade)
                        -> response.sendError(HttpServletResponse.SC_FORBIDDEN, ade.getMessage())));

        httpSecurity.authorizeHttpRequests(registry -> registry
                .requestMatchers("/", "/*.ico", "/actuator/**", "/error").permitAll()
                .requestMatchers("/auth/**").permitAll()
                .requestMatchers("/graphql/**").permitAll()
                .anyRequest().authenticated());

        return httpSecurity.build();
    }

}