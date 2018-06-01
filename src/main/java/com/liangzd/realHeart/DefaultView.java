/*package com.liangzd.realHeart;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

@Configuration
public class DefaultView extends WebMvcConfigurationSupport {
	@Override
	public void addViewControllers(ViewControllerRegistry registry) {
		registry.addViewController("/home/").setViewName("forward:welcome.html");
		registry.setOrder(Ordered.HIGHEST_PRECEDENCE);
		super.addViewControllers(registry);
	}
}
*/