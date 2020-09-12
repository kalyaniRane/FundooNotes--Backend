package com.bridgelabz.fundoonotes.service;

import com.bridgelabz.fundoonotes.dto.RedisUserDto;
import com.bridgelabz.fundoonotes.model.RedisUserModel;

public interface IRedisUser {

    String saveData(RedisUserDto redisUserDto);

}
