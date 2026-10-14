package com.base.todo.todosecurityhibernate.config;

import com.base.todo.todosecurityhibernate.security.JwtAuthenticationFilter;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final UserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;

    public SecurityConfig(UserDetailsService userDetailsService,@Qualifier("bcEncoder") PasswordEncoder passwordEncoder) {
        this.userDetailsService = userDetailsService;
        this.passwordEncoder = passwordEncoder;
    }

    // ─── DaoAuthenticationProvider (Spring Security 7 way) ───
//    @Bean
//    public DaoAuthenticationProvider authenticationProvider(@Qualifier(value="bcEncoder") PasswordEncoder passwordEncoder) {
//        // Constructor REQUIRES UserDetailsService (no-arg constructor removed)
//        DaoAuthenticationProvider provider = new DaoAuthenticationProvider(userDetailsService);
//        // PasswordEncoder is set via setter
//       // provider.setPasswordEncoder(encoder);
//        provider.setPasswordEncoder(passwordEncoder);
//        return provider;
//    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder);
        return provider;
    }

    // ─── AuthenticationManager (exposed for manual use) ───
//    @Bean
//    public AuthenticationManager authenticationManager(
//            DaoAuthenticationProvider authenticationProvider) {
//        return new org.springframework.security.authentication.ProviderManager(authenticationProvider);
//    }

    @Bean
    public AuthenticationManager authenticationManager(DaoAuthenticationProvider provider) {
        return new ProviderManager(provider);
    }


    // ─── HTTP Security Filter Chain ───
//    @Bean
//    public SecurityFilterChain filterChain(HttpSecurity http,DaoAuthenticationProvider provider) throws Exception {
//        http
//             .authenticationProvider(provider)
//            .authorizeHttpRequests(auth -> auth
//                .requestMatchers("/login", "/css/**", "/js/**").permitAll()
//                .requestMatchers("/admin/**").hasRole("ADMIN")
//                .anyRequest().authenticated()
//            )
//            .formLogin(form -> form
//                .loginPage("/login")
//                .defaultSuccessUrl("/home", true)
//                .permitAll()
//            )
//            .logout(logout -> logout.permitAll());
//
//        return http.build();
//    }


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, JwtAuthenticationFilter jwtAuthenticationFilter) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .exceptionHandling(e -> e
                        .authenticationEntryPoint((req, res, ex) ->
                                res.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized")))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/auth/**").permitAll()
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        .anyRequest().authenticated())
                .authenticationProvider(authenticationProvider())
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}   