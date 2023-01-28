package com.example.userservice.controller;


import com.example.userservice.domain.dto.UserDto;
import com.example.userservice.service.UserService;
import com.example.userservice.vo.Greeting;
import com.example.userservice.vo.RequestUser;
import com.example.userservice.vo.ResponseUser;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/")
public class UsersController {

    //application yml 파일 정보를 가져오고 싶을떄는 방법이 2가지 있다
    // 1. Enviroment 객체를 사용하거나
    // 여기서 바로 @Autowired 해도 되지만 생성자를 만들어 주는것이 좋다.
    // 2. @Value 라는 어노테이션 사용하면 된다

    private Environment env;
    private UserService userService;

    public UsersController(Environment env, UserService userService) {
        this.userService = userService;
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

    @PostMapping("/users")
    public ResponseEntity<ResponseUser> createUser(@RequestBody RequestUser user){

        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        UserDto userDto = mapper.map(user, UserDto.class);
        ResponseUser responseUser = userService.createUser(userDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(responseUser);
    }


}
