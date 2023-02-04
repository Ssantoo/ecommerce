package com.example.userservice.security;

import com.example.userservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationManager;

import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;


@Configuration //다른 bean들 보다 우선순위를 앞으로
@RequiredArgsConstructor
@EnableWebSecurity //security 어노테이션
public class WebSecurity {

    private final UserService userService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final Environment env; //패스워드를 암호화할떄 jwt토큰 유효시간 다양한 yml파일 정보를 가져올때 쓰기 위해 등록


    @Bean // resource 에 대해 Spring Security FilterChain 제외
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring().antMatchers("/h2-console/**", "/favicon.ico");
    }

    //권한
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        AuthenticationManager authenticationManager = getAuthenticationFilter(http);

        http.csrf().disable();
      // http.authorizeRequests().antMatchers("/users/**").permitAll();
        http.authorizeRequests()
                .antMatchers("/actuator/**").permitAll() // actuator permitAll
                .antMatchers("/error/**").permitAll()
   //             .antMatchers("/**").hasIpAddress("112.1.1.2")
                .and()
                .authenticationManager(authenticationManager)
                .addFilter(getAuthenticationFilter(authenticationManager));

        // //h2 console error 해결을 위해
        http.headers().frameOptions().disable();

        return http.build();

        //모든 요청에대한 permitall해준거
        //http.authorizeRequests().antMatchers("/users/**").permitAll();
//        return http.csrf().disable()
//                .headers()
//                .frameOptions().disable().and()               //h2 console error 해결을 위해
//                .authenticationManager(authenticationManager)
//                .authorizeRequests()
//                .antMatchers("/**")
//                        .permitAll()
//                        .and()
//                        .addFilter(getAuthenticationFilter())
//                .build();

    }

    private AuthenticationFilter getAuthenticationFilter(AuthenticationManager authenticationManager) {
        return new AuthenticationFilter(authenticationManager, userService, env);
    }

    private AuthenticationManager getAuthenticationFilter(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder.userDetailsService(userService).passwordEncoder(bCryptPasswordEncoder);
        return authenticationManagerBuilder.build();
    }





//
//    private static AuthorizationManager<RequestAuthorizationContext> hasIpAddress(String ipAddress) {
//        IpAddressMatcher ipAddressMatcher = new IpAddressMatcher(ipAddress);
//        return (authentication, context) -> {
//            HttpServletRequest request = context.getRequest();
//            return new AuthorizationDecision(ipAddressMatcher.matches(request));
//        };
//    }



//    private AuthenticationFilter getAuthenticationFilter() throws Exception {
//        AuthenticationFilter authenticationFilter =
//                new AuthenticationFilter(userService, env);
//
//        return authenticationFilter;
//    }

    //인증 인증이 되어야 권한이 생김
//    @Bean
//    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//        auth.userDetailsService(userService).passwordEncoder(bCryptPasswordEncoder);
//        //사용자가 전달한 id와 pw를 통해 로그인 처리를 security가 해줌
//    }
}
