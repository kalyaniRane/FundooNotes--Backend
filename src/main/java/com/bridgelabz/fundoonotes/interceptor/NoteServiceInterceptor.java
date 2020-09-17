package com.bridgelabz.fundoonotes.interceptor;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.bridgelabz.fundoonotes.exceptions.UserServiceException;
import com.bridgelabz.fundoonotes.user.model.RedisUserModel;
import com.bridgelabz.fundoonotes.user.model.UserDetails;
import com.bridgelabz.fundoonotes.user.repository.IUserRepository;
import com.bridgelabz.fundoonotes.user.repository.RedisUserRepository;
import com.bridgelabz.fundoonotes.utils.IToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class NoteServiceInterceptor implements HandlerInterceptor {

    @Autowired
    IToken iToken;

    @Autowired
    IUserRepository userRepository;

    @Autowired
    RedisUserRepository redisUserRepository;

    @Autowired
    HandlerExceptionResolver handlerExceptionResolver;

    @Override
    public boolean preHandle
            (HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {

        String token = request.getHeader("token");

        if(token != null){
            try {
                RedisUserModel byToken = redisUserRepository.findByToken(token);

                if(!byToken.getToken().isEmpty()){
                    int userID = iToken.decodeJWT(token);
                    UserDetails user = userRepository.findById(userID).get();
                    request.setAttribute("user",user);
                    return true;
                }

            }catch (Exception e){
                throw new UserServiceException("User Not Found");
            }
        }
        return false;
    }

}
