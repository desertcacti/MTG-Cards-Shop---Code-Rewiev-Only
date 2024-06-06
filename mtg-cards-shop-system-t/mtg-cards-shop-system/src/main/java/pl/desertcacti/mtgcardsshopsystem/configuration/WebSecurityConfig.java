package pl.desertcacti.mtgcardsshopsystem.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import pl.desertcacti.mtgcardsshopsystem.security.JwtRequestFilter;
import pl.desertcacti.mtgcardsshopsystem.security.JwtTokenProvider;
import pl.desertcacti.mtgcardsshopsystem.service.auth.CustomUserDetailsService;

/** WebSecurityConfig class configures security settings for the application */
@Configuration
@EnableWebSecurity
public class WebSecurityConfig {
    private CustomUserDetailsService customUserDetailsService;
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    public WebSecurityConfig(CustomUserDetailsService customUserDetailsService, JwtTokenProvider jwtTokenProvider) {
        this.customUserDetailsService = customUserDetailsService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    /** filterChain()
     /*  Method road map:
     *1* Disable Cross-Site Request Forgery protection - usually necessary for web applications with stateful sessions,
     but for stateless REST APIs, it is often disabled.

     *2* Set session management to stateless (no session is maintained between requests.)
     Each request needs to be authenticated using tokens JWT.

     *3* Configure authorization rules for HTTP requests.
     Define which endpoints are accessible without authorization. In any other case authorization will be required.

     *4* Add JWT filter before the UsernamePasswordAuthenticationFilter.

     *5* Build and return the configured Security Filter Chain. */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // *1*
                .csrf(csrf -> csrf.disable())
                // *2*
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                // *3*
                .authorizeHttpRequests(auth -> auth
//                        .requestMatchers("/", "/index.html", "/public/**").permitAll()
                        .requestMatchers("/").permitAll()
                        .requestMatchers("/api/auth/login").permitAll()
                        .requestMatchers("/api/auth/confirm/**").permitAll()
                        .requestMatchers("/api/auth/resendConfirmation").permitAll()
                        .requestMatchers("/api/auth/register").permitAll()
                        .requestMatchers("/api/cart/**").permitAll()
                        .requestMatchers("/api/media/**").permitAll()
                        .requestMatchers("/api/orders/guest-checkout").permitAll()
                        .requestMatchers("/api/products/**").permitAll()
                        .anyRequest().authenticated())

                // *4*
                .addFilterBefore(jwtRequestFilter(), UsernamePasswordAuthenticationFilter.class);
        // *5*
        return http.build();
    }

    /** Creates and returns a JwtRequestFilter bean. */
    @Bean
    public JwtRequestFilter jwtRequestFilter() {
        return new JwtRequestFilter(customUserDetailsService, jwtTokenProvider);
    }

    /** Creates and returns PasswordEncoder Bean using BCrypt. */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /** Creates and returns Authentication Manager Bean. */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
}