package com.bridgelabz.fundoonotes.interceptor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Component
public class NoteServiceInterceptorAppConfig extends WebMvcConfigurerAdapter {

   @Autowired
   NoteServiceInterceptor noteServiceInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(noteServiceInterceptor).addPathPatterns("/note/**","/label/**");
    }
}
