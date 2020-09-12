package com.bridgelabz.fundoonotes.service.implementation;

import com.bridgelabz.fundoonotes.dto.RedisUserDto;
import com.bridgelabz.fundoonotes.model.RedisUserModel;
import com.bridgelabz.fundoonotes.repository.RedisUserRepository;
import com.bridgelabz.fundoonotes.service.IRedisUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RedisUserService implements IRedisUser {

    @Autowired
    private RedisUserRepository userRepository;

    @Override
    public String saveData(RedisUserDto redisUserDto) {
        RedisUserModel redisUserModel = new RedisUserModel(redisUserDto);
        userRepository.save(redisUserModel);
        return "ADDED SUCCESSFULLY";
    }

}