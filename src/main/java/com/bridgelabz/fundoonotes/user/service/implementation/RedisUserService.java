package com.bridgelabz.fundoonotes.user.service.implementation;

import com.bridgelabz.fundoonotes.user.dto.RedisUserDto;
import com.bridgelabz.fundoonotes.user.model.RedisUserModel;
import com.bridgelabz.fundoonotes.user.repository.RedisUserRepository;
import com.bridgelabz.fundoonotes.user.service.IRedisUser;
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