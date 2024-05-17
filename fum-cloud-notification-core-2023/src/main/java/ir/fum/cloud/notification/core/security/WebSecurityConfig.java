package ir.fum.cloud.notification.core.security;


import ir.fum.cloud.notification.core.configuration.DocsConfiguration;
import ir.fum.cloud.notification.core.configuration.ProjectConfiguration;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.annotation.Priority;

@Slf4j
@Priority(1)
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableGlobalMethodSecurity(prePostEnabled = true, order = Integer.MIN_VALUE + 1000)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private final DocsConfiguration docsConfiguration;
    private final ProjectConfiguration projectConfiguration;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .cors()
                .and()
                .csrf().disable()
                .anonymous().principal("_ANONYMOUS_")
                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .addFilter(new MyBasicAuthenticationFilter(authenticationManager(), projectConfiguration))
                .authorizeRequests()
                .regexMatchers("\\/docs/api-docs/private(&.*|$)").authenticated()
                .antMatchers("/login", "/**", "/actuator/**", "/webjars/**",
                        "/configuration/ui", "/configuration/security", "/docs").permitAll()
                .anyRequest().authenticated()
                .and()
                .httpBasic();

        http.headers().cacheControl();

    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication()
                .withUser(docsConfiguration.getDocsPrivateUsername())
                .password(bCryptPasswordEncoder().encode(docsConfiguration.getDocsPrivatePassword()))
                .roles("SWAGGER_ADMIN");
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
