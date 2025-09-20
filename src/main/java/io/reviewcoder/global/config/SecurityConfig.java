package io.reviewcoder.global.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;


//@EnableWebSecurity(debug = true)
@Configuration
public class SecurityConfig {

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(sm -> sm
                        .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                        .sessionFixation(sf -> sf.migrateSession())
                )
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/api/v1/auth/email/send",
                                "/api/v1/auth/email/verify",
                                "/api/v1/auth/signup",
                                "/login", "/logout",
                                "/error"
                        ).permitAll()
                        .anyRequest().authenticated()
                )
                // 비인증 요청은 401로
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED))
                )
                .formLogin(form -> form
                        .loginProcessingUrl("/login")
                        .successHandler((req, res, auth) -> res.setStatus(200))      // 로그인 성공 시 200
                        .failureHandler((req, res, ex) -> res.setStatus(401))        // 실패 시 401
                        .permitAll()
                )

                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .deleteCookies("JSESSIONID")
                        .invalidateHttpSession(true)
                        .clearAuthentication(true)
                        .logoutSuccessHandler((req, res, auth) -> {
                            res.setStatus(200);
                            res.setContentType("application/json;charset=UTF-8");
                            res.getWriter().write("{\"message\":\"로그아웃되었습니다.\"}");
                        })
                        .permitAll()
                );
        return http.build();
    }

    @Bean
    PasswordEncoder passwordEncoder() { return new BCryptPasswordEncoder(); }
}