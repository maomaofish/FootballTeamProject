package com.company.footballteam.config;

import com.company.footballteam.filter.JWTTokenValidatorFilter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    //@Autowired
    //UserDetailsService userServiceImpl;

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                // .authorizeRequests()
                // .antMatchers("/api/signup").permitAll()
                // .anyRequest().authenticated()
                // .and()
                // .httpBasic();
            .csrf().disable()
            .addFilterBefore(new JWTTokenValidatorFilter(), BasicAuthenticationFilter.class)
            //.addFilterAfter(new JWTTokenGeneratorFilter(),BasicAuthenticationFilter.class)
            .authorizeRequests()
            .antMatchers("/api/market_players/*").hasRole("ADMIN")
            .antMatchers("/api/players/**").hasRole("ADMIN")
            .antMatchers("/api/user_info/**").hasRole("ADMIN")
            .antMatchers("/api/login").authenticated()
            .antMatchers("/api/signup").permitAll()
            .and().httpBasic();

        //http.csrf().ignoringAntMatchers("/api/signup").csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse());
    }

    //  @Autowired
    // public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
    //     auth.userDetailsService(userServiceImpl);
    // }

    // @Bean
    // public PasswordEncoder passwordEncoder() {
    //     return new BCryptPasswordEncoder();
    // }
}
