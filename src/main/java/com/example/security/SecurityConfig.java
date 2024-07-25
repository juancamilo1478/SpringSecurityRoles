package com.example.security;

import com.example.security.filters.JwtAuthenticationFilters;
import com.example.security.filters.JwtAutheritationFilter;
import com.example.security.jwt.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.EnableGlobalAuthentication;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    @Autowired
    JwtUtils jwtUtils;
    @Autowired
    UserDetailsService userDetailsService;
    @Autowired
    JwtAutheritationFilter jwtAutheritationFilter;
    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity, AuthenticationManager authenticationManager) throws Exception {

        JwtAuthenticationFilters jwtAuthenticationFilters = new JwtAuthenticationFilters(jwtUtils);
        jwtAuthenticationFilters.setAuthenticationManager(authenticationManager);
//        jwtAuthenticationFilters.setFilterProcessesUrl("/login");
        return httpSecurity
                .csrf(config -> config.disable())
                .authorizeHttpRequests(auth -> {
                    auth.requestMatchers("/hello").permitAll();
                    auth.requestMatchers("/accesAdmin").hasRole("ADMIN");
                    auth.requestMatchers("/accesAdmin").hasRole("ADMIN");
                    auth.anyRequest().authenticated();
                })
                .sessionManagement(session -> {
                    session.sessionCreationPolicy(SessionCreationPolicy.STATELESS);

                })
                .addFilter(jwtAuthenticationFilters)
                .addFilterBefore(jwtAutheritationFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

      @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
//    @Bean
//    UserDetailsService userDetailsService(){
//        InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();
//        manager.createUser(User.withUsername("camilo")
//                .password("buenas")
//                .roles()
//                .build());
//        return manager;
//    }
@Bean
public AuthenticationManager authenticationManager(HttpSecurity httpSecurity, PasswordEncoder passwordEncoder) throws Exception {
    AuthenticationManagerBuilder authenticationManagerBuilder = httpSecurity.getSharedObject(AuthenticationManagerBuilder.class);
    authenticationManagerBuilder
            .userDetailsService(userDetailsService)
            .passwordEncoder(passwordEncoder);
    return authenticationManagerBuilder.build();
}
//


}
