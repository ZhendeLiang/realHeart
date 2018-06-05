package com.liangzd.realHeart;

import javax.servlet.MultipartConfigElement;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@SpringBootApplication
@Configuration
public class RealHeartApplication {

	public static void main(String[] args) {
		SpringApplication.run(RealHeartApplication.class, args);
	}
	/**  
     * 文件上传配置  
     * @return  
     */  
    @Bean  
    public MultipartConfigElement multipartConfigElement() {  
        MultipartConfigFactory factory = new MultipartConfigFactory();  
        //单个文件最大  
        factory.setMaxFileSize("20MB"); //KB,MB  
        /// 设置总上传数据总大小  
        factory.setMaxRequestSize("100MB");  
        return factory.createMultipartConfig();  
    } 
}
