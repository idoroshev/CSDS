package org.csds.lab2.server.config;

import org.csds.lab2.server.auth.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableOAuth2Client;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.access.channel.ChannelProcessingFilter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import java.util.Collections;

@Configuration
@EnableWebSecurity
@EnableOAuth2Client
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors().and()
                .addFilterBefore(new SimpleAuthenticationFilter(authenticationManager()), UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(new SimpleCorsFilter(), ChannelProcessingFilter.class)
            .authorizeRequests().antMatchers("/session").permitAll()
            .anyRequest().authenticated()
            .and()
            .formLogin()
                .successHandler(new LoginSuccessHandler())
                .failureHandler(new LoginFailureHandler())
                .usernameParameter("username").passwordParameter("password")
            .loginPage("/login")
            .permitAll()
            .and()
            .logout()
            .permitAll()
            .deleteCookies("JSESSIONID")
            .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
            .logoutSuccessHandler(new LogoutSuccessHandler())
            .and()
            .csrf().disable();
    }

    @Bean
    @Override
    public UserDetailsService userDetailsService() {
        UserDetails user =
                User.withDefaultPasswordEncoder()
                        .username("antbakatovich@gmail.com")
                        .password("minsk")
                        .roles("USER")
                        .build();

        return new InMemoryUserDetailsManager(user);
    }

    @Autowired
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        //auth.authenticationProvider(new SimpleAuthenticationProvider(userDetailsService()));
        /*auth.inMemoryAuthentication()
                .withUser("ihar").password(passwordEncoder().encode("mazyr")).roles("USER")
                .and()
                .withUser("anton").password(passwordEncoder().encode("minsk")).roles("USER");*/
    }

    @Override
    protected AuthenticationManager authenticationManager() throws Exception {
        return new ProviderManager(Collections.singletonList(new SimpleAuthenticationProvider(userDetailsService())));
    }

    /*@Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }*/
}
