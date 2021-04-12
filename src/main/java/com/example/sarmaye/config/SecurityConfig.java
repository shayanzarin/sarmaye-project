package com.example.sarmaye.config;

import com.example.sarmaye.models.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Qualifier("userService")
    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
//        http
//                .authorizeRequests()
//                .antMatchers("/home").permitAll()
//                .antMatchers("/user/**").hasRole("USER")
//                .anyRequest()
//                .authenticated()
//                .and()
//                .httpBasic();

//  http
//          .csrf().disable()
//            .authorizeRequests()
//            .antMatchers("/home")
//            .permitAll()
//            .antMatchers("/user/**")
//            .hasRole("USER")
//            .anyRequest()
//            .authenticated()
//            .and()
//            .formLogin()
//            .loginPage("/login").permitAll()
//            .defaultSuccessUrl("/show");
        http
                .csrf().disable()
                .authorizeRequests()
                .antMatchers("/home","/signup","/register", "/css/**", "/login/**", "/js/**", "/image/**").permitAll()
//                .antMatchers("/","/temp/**","/register*","main/**").permitAll()
                .antMatchers("/user/**").hasAnyRole(Role.ADMIN.name(), Role.USER.name())
                .antMatchers("/assistant/**").hasAnyRole(Role.ASSISTANT.name(), Role.ADMIN.name())
                .antMatchers("/admin/**").hasRole(Role.ADMIN.name())
//                .antMatchers("/admin/**").hasRole(Role.ADMIN.name())
//                .antMatchers("/responsible/**").hasRole(Role.RESPONSIBLE.name())
                .anyRequest()
                .authenticated()
                .and()
                .formLogin()
                .loginPage("/login")
                .defaultSuccessUrl("/success").permitAll()
//                .loginProcessingUrl("/login")
                .failureUrl("/fail")
//                    .usernameParameter("username")
//                    .passwordParameter("password")
                .and()
                .logout()
                .logoutSuccessUrl("/home");
//                .and()
//                .rememberMe()
//                .rememberMeParameter("remember-me")
//                .tokenValiditySeconds((int)TimeUnit.DAYS.toSeconds(20))
//                .and()
//                .logout()
//                .logoutUrl("/logout")
//                .clearAuthentication(true)
//                .invalidateHttpSession(true)
//                .deleteCookies("JSESSIONID","remember-me")
//                .logoutSuccessUrl("/home")
//                .and()
//                .exceptionHandling()
//                .accessDeniedPage("/403");
//                .usernameParameter("username")
//                .passwordParameter("password")
//                .loginProcessingUrl("/home/login-form");
//                // force to go this page after login, even ex:  you request to localhost:3306/user/hello after authenticate redirect to /say-hello
//                .defaultSuccessUrl("/say-hello",true);
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider(){

        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(passwordEncoder);
        provider.setUserDetailsService(userDetailsService);
        return provider;
    }

    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {


        auth.authenticationProvider(authenticationProvider());
    }

}

