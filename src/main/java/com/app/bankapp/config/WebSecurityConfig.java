package com.app.bankapp.config;


import com.app.bankapp.filter.JwtAuthenticationTokenFilter;
import com.app.bankapp.handler.AjaxAuthenticationFailureHandler;
import com.app.bankapp.handler.AjaxAuthenticationSuccessHandler;
import com.app.bankapp.handler.AjaxLogoutSuccessHandler;
import com.app.bankapp.handler.Http401UnauthorizedEntryPoint;
import com.app.bankapp.security.AuthProviderService;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {


    AjaxAuthenticationSuccessHandler ajaxAuthenticationSuccessHandler;
    AjaxAuthenticationFailureHandler ajaxAuthenticationFailureHandler;
    AjaxLogoutSuccessHandler ajaxLogoutSuccessHandler;
    Http401UnauthorizedEntryPoint authenticationEntryPoint;
    AuthProviderService authProvider;
    SecurityProperties security;
    JwtAuthenticationTokenFilter jwtAuthenticationTokenFilter;


    public WebSecurityConfig(AjaxAuthenticationSuccessHandler ajaxAuthenticationSuccessHandler, AjaxAuthenticationFailureHandler ajaxAuthenticationFailureHandler, AjaxLogoutSuccessHandler ajaxLogoutSuccessHandler, Http401UnauthorizedEntryPoint authenticationEntryPoint, AuthProviderService authProvider, SecurityProperties security, JwtAuthenticationTokenFilter jwtAuthenticationTokenFilter) {
        this.ajaxAuthenticationSuccessHandler = ajaxAuthenticationSuccessHandler;
        this.ajaxAuthenticationFailureHandler = ajaxAuthenticationFailureHandler;
        this.ajaxLogoutSuccessHandler = ajaxLogoutSuccessHandler;
        this.authenticationEntryPoint = authenticationEntryPoint;
        this.authProvider = authProvider;
        this.security = security;
        this.jwtAuthenticationTokenFilter = jwtAuthenticationTokenFilter;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) {
        auth.authenticationProvider(authProvider);
    }


    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http
                .csrf().disable()
                .exceptionHandling().authenticationEntryPoint(authenticationEntryPoint).and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                .authorizeRequests()
                .antMatchers("/api/authentication").permitAll()
                .anyRequest().permitAll()
                .and()
                .formLogin()
                .loginProcessingUrl("/api/authentication")
                .successHandler(ajaxAuthenticationSuccessHandler)
                .failureHandler(ajaxAuthenticationFailureHandler)
                .usernameParameter("email")
                .passwordParameter("password")
                .and()
                .logout()
                .logoutUrl("/api/logout")
                .logoutSuccessHandler(ajaxLogoutSuccessHandler)
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID");

        http.addFilterBefore(jwtAuthenticationTokenFilter, UsernamePasswordAuthenticationFilter.class);
        http.headers().cacheControl();

    }


}
