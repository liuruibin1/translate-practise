package com.xxx.security.configuration;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.CorsConfigurer;
import org.springframework.security.config.annotation.web.configurers.ExceptionHandlingConfigurer;
import org.springframework.security.config.annotation.web.configurers.SessionManagementConfigurer;
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

    //@Value("${server.servlet.context-path}")
    //private String serverServletContextPath;

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (webSecurity) -> webSecurity
                .ignoring()
                .requestMatchers("/resources/**").anyRequest();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.setAllowedOrigins(List.of("*"));
        corsConfiguration.setAllowedMethods(List.of("*"));
        corsConfiguration.setAllowedHeaders(List.of("*"));
        corsConfiguration.setExposedHeaders(List.of("*"));
        corsConfiguration.setAllowCredentials(true);
        corsConfiguration.setMaxAge(3600L);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfiguration);
        return source;
    }


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        // CORS
        Customizer<CorsConfigurer<HttpSecurity>> corsCustomizer = corsConfigurer
                -> corsConfigurer.configurationSource(corsConfigurationSource());
        httpSecurity.cors(corsCustomizer);

        httpSecurity.csrf(AbstractHttpConfigurer::disable);

        httpSecurity.formLogin(AbstractHttpConfigurer::disable);

        //http.formLogin().loginPage("/auth/login").and();
        //http = http.logout().logoutUrl("/auth/logout").and();

        //Customizer<LogoutConfigurer<HttpSecurity>> logoutCustomizer = httpSecurityLogoutConfigurer
        //        -> httpSecurityLogoutConfigurer.logoutUrl("/auth/logout");
        //httpSecurity.logout(logoutCustomizer);

        Customizer<SessionManagementConfigurer<HttpSecurity>> sessionManagementCustomizer = sessionManagementConfigurer
                -> sessionManagementConfigurer
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        httpSecurity.sessionManagement(sessionManagementCustomizer);

        Customizer<ExceptionHandlingConfigurer<HttpSecurity>> ExceptionHandlingCustomizer =
                exceptionHandlingConfigurer
                        -> exceptionHandlingConfigurer.authenticationEntryPoint((request, response, ex)
                        -> response.sendError(SC_UNAUTHORIZED, ex.getMessage()));
        httpSecurity.exceptionHandling(ExceptionHandlingCustomizer);

        httpSecurity.exceptionHandling((configure) -> configure
                .accessDeniedHandler((request, response, ade)
                        -> response.sendError(HttpServletResponse.SC_FORBIDDEN, ade.getMessage())));

        // Allow OPTIONS requests (preflight requests) without security checks
        httpSecurity.authorizeHttpRequests(registry -> registry
                .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                .requestMatchers("/", "/*.ico", "/actuator/**", "/error").permitAll()
                .requestMatchers("/auth/**").permitAll()
                //.requestMatchers(serverServletContextPath + "/auth/**").permitAll()
                .requestMatchers("/graphql/**").permitAll()
                //.requestMatchers(serverServletContextPath + "/graphql/**").permitAll()
                .anyRequest().authenticated());

        return httpSecurity.build();
    }

}