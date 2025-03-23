package io.credable.loanmanagement.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Value("${security.api.username}")
    private String apiUsername;

    @Value("${security.api.password}")
    private String apiPassword;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    @Override
    public UserDetailsService userDetailsService() {
        UserDetails apiUser = User.builder()
                .username(apiUsername)
                .password(passwordEncoder().encode(apiPassword))
                .roles("API")
                .build();

        return new InMemoryUserDetailsManager(apiUser);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .csrf().disable()
            .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
            .authorizeRequests()
                // Public endpoints
                .antMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html").permitAll()
                .antMatchers("/h2-console/**").permitAll()
                // Secured endpoints
                .antMatchers("/api/v1/transactions/**").hasRole("API")
                .antMatchers("/api/v1/customers/**", "/api/v1/loans/**").authenticated()
                .anyRequest().authenticated()
                .and()
            .httpBasic()
                .and()
            .headers()
                .frameOptions().sameOrigin(); // Required for H2 console

        // Add custom token filter for scoring engine
        http.addFilterBefore(scoringEngineTokenFilter(), UsernamePasswordAuthenticationFilter.class);
    }

    @Bean
    public ScoringEngineTokenFilter scoringEngineTokenFilter() {
        return new ScoringEngineTokenFilter();
    }
} 