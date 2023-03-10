package com.example.userservice.service;


import com.example.userservice.client.OrderServiceClient;
import com.example.userservice.dto.UserDto;
import com.example.userservice.jpa.UserEntity;
import com.example.userservice.jpa.UserRepository;
import com.example.userservice.vo.ResponseOrder;
import com.example.userservice.vo.ResponseUser;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    private final Environment env;

    private final RestTemplate restTemplate;

    private final OrderServiceClient orderServiceClient;





    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity userEntity = userRepository.findByEmail(username);

        if (userEntity == null) {
            throw new UsernameNotFoundException(username);
        }

        //System.out.println(userEntity);

        return new User(username, userEntity.getEncryptedPwd(),
                true, true, true, true,
                new ArrayList<>());


    }

    @Override
    public ResponseUser createUser(UserDto userDto) {

        userDto.setUserId(UUID.randomUUID().toString());

        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        UserEntity userEntity = mapper.map(userDto, UserEntity.class);
        userEntity.setEncryptedPwd(bCryptPasswordEncoder.encode(userDto.getPwd()));

        UserEntity save = userRepository.save(userEntity);
        ResponseUser responseUser = mapper.map(save, ResponseUser.class);

        return responseUser;
    }

    @Override
    public UserDto getUserByUserId(String userId) {
        UserEntity userEntity = userRepository.findByUserId(userId);

        if(userEntity == null)
            throw new UsernameNotFoundException("????????? ?????? ??? ????????????.");

        UserDto userDto = new ModelMapper().map(userEntity, UserDto.class);

        //List<ResponseOrder> orders = new ArrayList<>();

        /* Using as RestTemplate */
//        String orderUrl = String.format(env.getProperty("order_service.url"), userId);	//(1)
//
//
//        ResponseEntity<List<ResponseOrder>> orderListResponse =	//(2)
//                restTemplate.exchange(orderUrl, HttpMethod.GET, null, new ParameterizedTypeReference<List<ResponseOrder>>() {
//                });                   //?????????, ????????????????????? ???????????????, ????????????, ????????????????????? ??????????????????
//
//        List<ResponseOrder> ordersList = orderListResponse.getBody();	//(3)

        /* Using a feign client */

       // List<ResponseOrder> ordersList = orderServiceClient.getOrderByUserId(userId);

        /* FeignClient exception handling*/
//        List<ResponseOrder> ordersList = null;
//        try {
//            ordersList = orderServiceClient.getOrderByUserId(userId);
//        }catch (FeignException e){
//            log.error(e.getMessage());
//        }

        /* ErrorDecoder */
        List<ResponseOrder> ordersList = orderServiceClient.getOrderByUserId(userId);
        userDto.setOrders(ordersList);

        return userDto;
    }

    @Override
    public Iterable<UserEntity> getUserByAll() {
        return userRepository.findAll();
    }

    @Override
    public UserDto getUserDetailsByEmail(String email) {

        UserEntity userEntity = userRepository.findByEmail(email);

        if(userEntity == null)
            throw new UsernameNotFoundException(email);

        return new ModelMapper().map(userEntity, UserDto.class);
    }


}
