package FinanceDashboard.ByFaizan.Security;
import jakarta.servlet.http.HttpServletResponse;
import FinanceDashboard.ByFaizan.Repositary.UserRepositary;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;


@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@Slf4j
public class ConfigSecurity {
        @Autowired
        private final JwtFilter jwtFilter;
        @Autowired
        private final OAuth2SuccessHandler oAuth2SuccessHandler;
        private final UserRepositary userRepositary;

        @Bean
        public AuthenticationManager  authenticationManager(AuthenticationConfiguration configuration) throws Exception{
                 return configuration.getAuthenticationManager();
        }


        @Bean
        public PasswordEncoder passwordEncoder() {
            return new BCryptPasswordEncoder();
        }

        @Bean
        public UserDetailsService userDetailsService() {
            return username -> userRepositary.findByEmail(username)
                    .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
        }

        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

            http.cors(cros -> {})
                    .csrf(csrf -> csrf.disable())
                    .sessionManagement(sessionConfig ->
                            sessionConfig.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                    .authorizeHttpRequests(auth -> auth
                          .requestMatchers(
                    "/api/auth/**",
                    "/api/auth/login",
                    "/api/login",
                    "/oauth2/**",
                    "/api/dashboard/**",
                    "/api/transaction/**",
                    "/api/budget/**"
                ).permitAll().anyRequest().permitAll()
                    )
                    .exceptionHandling(ex -> ex
                            .authenticationEntryPoint((request, response, authException) -> {
                                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                                response.getWriter().write("Unauthorized: " + authException.getMessage());
                            })
                    )
                    .oauth2Login(oauth -> oauth
                            .successHandler(oAuth2SuccessHandler)
                            .failureHandler((request, response, exception) -> {
                                log.error("OAuth2 error: {} ", exception.getMessage());
                                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                                response.getWriter().write("OAuth2 Login Failed");
                            })
                    )
                    .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

            return http.build();
        }


    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("https://finance-dashboard-frontend-x7l4.vercel.app"));
        configuration.setAllowedMethods(List.of("GET","POST","PUT","DELETE","OPTIONS"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    }

