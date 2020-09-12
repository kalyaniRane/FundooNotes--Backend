package com.bridgelabz.fundoonotes.repository;


import com.bridgelabz.fundoonotes.model.RedisUserModel;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class RedisUserRepository {

    private HashOperations hashOperations;

    private RedisTemplate redisTemplate;

    public RedisUserRepository(RedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
        this.hashOperations = this.redisTemplate.opsForHash();
    }

    public void save(RedisUserModel user) {
        hashOperations.put("USER", user.getToken(), user);
    }

    public RedisUserModel findByToken(String token) {
        return (RedisUserModel) hashOperations.get("USER", token);
    }
/*
    public void update(RedisUserModel user) {
        save(user);
    }

    public void delete(String id) {
        hashOperations.delete("USER", id);
    }

    public void multiGetUsers(List<String> userIds) {
        hashOperations.multiGet("USER", userIds);
    }*/
}