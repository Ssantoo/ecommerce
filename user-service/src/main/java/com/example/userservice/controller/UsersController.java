package com.example.userservice.controller;


import com.example.userservice.vo.Greeting;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class UsersController {

    //application yml 파일 정보를 가져오고 싶을떄는 방법이 2가지 있다
    // 1. Enviroment 객체를 사용하거나
    // 여기서 바로 @Autowired 해도 되지만 생성자를 만들어 주는것이 좋다.
    // 2. @Value 라는 어노테이션 사용하면 된다

    private Environment env;

    public UsersController(Environment env) {
        this.env = env;
    }

    @Autowired
    private Greeting greeting;



    @GetMapping("/health_check")
    public String status(){
        return "잘 작동 중";
    }

    @GetMapping("/welcome")
    public String welcome(){
//        return env.getProperty("greeting.message");
        return greeting.getMessage();
    }
}
