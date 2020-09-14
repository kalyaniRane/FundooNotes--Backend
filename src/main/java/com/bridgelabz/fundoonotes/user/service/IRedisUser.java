package com.bridgelabz.fundoonotes.user.service;

import com.bridgelabz.fundoonotes.user.dto.RedisUserDto;

public interface IRedisUser {

    String saveData(RedisUserDto redisUserDto);

}
