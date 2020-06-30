package com.z5n.autoexcel.config.security;

import com.z5n.autoexcel.service.impl.UserServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    public SecurityConfig() {
    }

    @Bean
    UserServiceImpl userService() {
        return new UserServiceImpl();
    }

    /**使用BCrypt加密
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userService())
                .passwordEncoder(new BCryptPasswordEncoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/admin/**").hasRole("ADMIN")
                .antMatchers("/user/**", "/submit").hasRole("USER")
                .antMatchers("/","/bus/**", "/register/**", "/forgetPassword/**", "/login").permitAll()
                .anyRequest().authenticated()
                .and()
                .formLogin()
                .loginPage("/")
                .loginProcessingUrl("/login")
                .successHandler(new MyAuthenticationSuccessHandler())
                .failureHandler(new MyAuthenticationFailureHandler())
                .permitAll()
                .and()
                .csrf().disable();
    }


    /**
     * 静态资源放行
     */
    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/js/**", "/css/**","/img/**", "/fonts/**", "/bootstrap4/**", "/rtbus/**");
    }
}
