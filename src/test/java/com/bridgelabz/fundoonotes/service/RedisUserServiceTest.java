package com.bridgelabz.fundoonotes.service;


import com.bridgelabz.fundoonotes.dto.RedisUserDto;
import com.bridgelabz.fundoonotes.model.RedisUserModel;
import com.bridgelabz.fundoonotes.properties.FileProperties;
import com.bridgelabz.fundoonotes.repository.RedisUserRepository;
import com.bridgelabz.fundoonotes.service.implementation.RedisUserService;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;


@SpringBootTest
public class RedisUserServiceTest {

    @Mock
    RedisUserRepository redisUserRepository;

    @InjectMocks
    RedisUserService userService;

    @MockBean
    FileProperties fileProperties;

    @Test
    void givenData_WhenSave_shouldReturnMessage(){

        RedisUserDto redisUserDto=new RedisUserDto("token");
        RedisUserModel userModel=new RedisUserModel(redisUserDto);
        String message="ADDED SUCCESSFULLY";

       doNothing().when(redisUserRepository).save(isA(RedisUserModel.class));
        String responseMessage = userService.saveData(redisUserDto);

        Assert.assertEquals(message,responseMessage);
    }

}
