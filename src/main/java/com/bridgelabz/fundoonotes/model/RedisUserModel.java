package com.bridgelabz.fundoonotes.model;


import com.bridgelabz.fundoonotes.dto.RedisUserDto;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class RedisUserModel implements Serializable {
    private String token;

    public RedisUserModel(RedisUserDto redisUserDto) {
        this.token= redisUserDto.token;
    }

}