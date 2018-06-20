package com.liangzd.realHeart;

import javax.servlet.MultipartConfigElement;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;

/**
 * 
 * @Description: Spring Boot程序入口
 * @author liangzd
 * @date 2018年6月16日 下午7:16:41
 */
@SpringBootApplication
@Configuration
public class RealHeartApplication {

	public static void main(String[] args) {
		SpringApplication.run(RealHeartApplication.class, args);
	}
	/**
	 * 
	 * @Description: 文件上传配置,设置单个文件最大值20MB，单词传送数据最大值100MB
	 * @param 
	 * @return MultipartConfigElement
	 * @author liangzd
	 * @date 2018年6月16日 下午7:18:06
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
    
    /**
     * 
     * @Description: 配置Spring Boot 使用fastjson解析JSON数据
     * @param 
     * @return HttpMessageConverters
     * @author liangzd
     * @date 2018年6月17日 下午4:17:15
     */
    @Bean
    public HttpMessageConverters fastjsonHttpMessageConverter() {
         FastJsonHttpMessageConverter fastJsonHttpMessageConverter = new FastJsonHttpMessageConverter();
         FastJsonConfig fastJsonConfig = new FastJsonConfig();
         fastJsonConfig.setSerializerFeatures(SerializerFeature.PrettyFormat);
         fastJsonHttpMessageConverter.setFastJsonConfig(fastJsonConfig);
         return new HttpMessageConverters(fastJsonHttpMessageConverter);
    }
}
