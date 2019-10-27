package org.csds.lab2.server.config;

import org.csds.lab2.server.auth.LoginFailureHandler;
import org.csds.lab2.server.auth.LoginSuccessHandler;
import org.csds.lab2.server.auth.LogoutSuccessHandler;
import org.csds.lab2.server.auth.SimpleCorsFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableOAuth2Client;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.access.channel.ChannelProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
@EnableOAuth2Client
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors().and().addFilterBefore(new SimpleCorsFilter(), ChannelProcessingFilter.class)
            .authorizeRequests()
            .anyRequest().authenticated()
            .and()
            .formLogin()
            .loginPage("/login")
            .permitAll()
            .successHandler(new LoginSuccessHandler())
                .failureHandler(new LoginFailureHandler())
            .usernameParameter("username").passwordParameter("password")
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
                        .username("kek")
                        .password("lol")
                        .roles("USER")
                        .build();

        return new InMemoryUserDetailsManager(user);
    }
}
