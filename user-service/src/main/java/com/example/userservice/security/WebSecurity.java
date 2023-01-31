package com.example.userservice.security;

import com.example.userservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;
import org.springframework.security.web.util.matcher.IpAddressMatcher;

import javax.servlet.Filter;
import javax.servlet.http.HttpServletRequest;

import static org.springframework.security.web.server.authorization.IpAddressReactiveAuthorizationManager.hasIpAddress;

@Configuration //다른 bean들 보다 우선순위를 앞으로
@RequiredArgsConstructor
@EnableWebSecurity //security 어노테이션
public class WebSecurity extends WebSecurityConfigurerAdapter {

    private final UserService userService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    private final Environment env; //패스워드를 암호화할떄 jwt토큰 유효시간 다양한 yml파일 정보를 가져올때 쓰기 위해 등록

    //권한
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();
        //모든 요청에대한 permitall해준거
        //http.authorizeRequests().antMatchers("/users/**").permitAll();

        http.authorizeRequests().antMatchers("/**")
                .permitAll()
                .and()
                .addFilter(getAuthenticationFilter());
        
        http.headers().frameOptions().disable();    //h2 console error 해결을 위해
    }
//
//    private static AuthorizationManager<RequestAuthorizationContext> hasIpAddress(String ipAddress) {
//        IpAddressMatcher ipAddressMatcher = new IpAddressMatcher(ipAddress);
//        return (authentication, context) -> {
//            HttpServletRequest request = context.getRequest();
//            return new AuthorizationDecision(ipAddressMatcher.matches(request));
//        };
//    }


    private AuthenticationFilter getAuthenticationFilter() throws Exception {
        AuthenticationFilter authenticationFilter =
                new AuthenticationFilter(userService, env);
        authenticationFilter.setAuthenticationManager(authenticationManager());
        //spring security에서 제공하는 manager 객체

        return authenticationFilter;
    }

    //인증 인증이 되어야 권한이 생김
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userService).passwordEncoder(bCryptPasswordEncoder);
        //사용자가 전달한 id와 pw를 통해 로그인 처리를 security가 해줌
    }
}
