package com.example.core.config.security;

import com.example.services.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(final AuthenticationManagerBuilder auth) throws Exception {
        // Используем базу данных
        auth.userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder());

        // Дополнительно создаем inMemory пользователей для тестирования
        auth.inMemoryAuthentication()
                .withUser("admin")
                .password(passwordEncoder().encode("qwerty"))
                .roles("ADMIN")
                .and()
                .withUser("user")
                .password(passwordEncoder().encode("password"))
                .roles("USER");
        auth.userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder());
    }

    @Override
    protected void configure(final HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .authorizeRequests()
                // GET запросы доступны всем (необязательная аутентификация)
                .antMatchers(HttpMethod.GET, "/api/bicycles/**").permitAll()

                // Все остальные методы (POST, PUT, DELETE) требуют роли ADMIN
                .antMatchers(HttpMethod.POST, "/api/bicycles/**").hasRole("ADMIN")
                .antMatchers(HttpMethod.PUT, "/api/bicycles/**").hasRole("ADMIN")
                .antMatchers(HttpMethod.DELETE, "/api/bicycles/**").hasRole("ADMIN")

                // Web интерфейс
                .antMatchers("/admin/**").hasRole("ADMIN")
                .antMatchers("/bicycles/add", "/bicycles/delete/**", "/bicycles/update/**").hasRole("ADMIN")
                .antMatchers("/bicycles/**").permitAll()
                .antMatchers("/register").permitAll()
                .antMatchers("/", "/home", "/about", "/services", "/resources/**", "/login").permitAll()
                .anyRequest().authenticated()
                .and()
                .httpBasic() // Basic Auth для API
                .and()
                .formLogin() // Форма логина для web
                .loginPage("/login")
                .loginProcessingUrl("/login")
                .defaultSuccessUrl("/", true)
                .failureUrl("/login?error=true")
                .permitAll()
                .and()
                .logout()
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login?logout=true")
                .permitAll();
    }
}