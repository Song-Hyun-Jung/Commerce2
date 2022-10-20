package com.digital2.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.digital.interceptor.AuthInterceptor;

@ComponentScan(basePackages = {"com.digital.interceptor"}) 
@Configuration
public class WebMvcConfig implements WebMvcConfigurer{

	@Autowired
	AuthInterceptor authInterceptor; 
	
	@Override
    public void addInterceptors(InterceptorRegistry registry) {	

		// 인증 관련 인터셉터
        registry.addInterceptor(authInterceptor)
                .addPathPatterns("/rest/**")
                .excludePathPatterns("/rest/person/signUp", "/rest/person/logIn", "/rest/person/inquiry2/{keyword}"); //authorize 제외(로그인, 회원가입, 회원검색)
    }
}
