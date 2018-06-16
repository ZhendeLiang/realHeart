package com.liangzd.realHeart;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.authc.pam.AtLeastOneSuccessfulStrategy;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.liangzd.realHeart.realm.AdminShiroRealm;
import com.liangzd.realHeart.realm.CustomizedModularRealmAuthenticator;
import com.liangzd.realHeart.realm.UserShiroRealm;

/**
 * 
 * @Description: Shiro的配置信息
 * @author liangzd
 * @date 2018年5月31日 下午4:44:33
 */
@Configuration
public class ShiroConfig {
	@Bean
	public ShiroFilterFactoryBean shirFilter(SecurityManager securityManager) {
		System.out.println("ShiroConfiguration.shirFilter()");
		ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
		shiroFilterFactoryBean.setSecurityManager(securityManager);
		//拦截器.
		Map<String,String> filterChainDefinitionMap = new LinkedHashMap<String,String>();
		// 配置不会被拦截的链接 顺序判断
//		filterChainDefinitionMap.put("/css/**", "anon");
//		filterChainDefinitionMap.put("/js/**", "anon");
//		filterChainDefinitionMap.put("/img/**", "anon");
		//配置退出 过滤器,其中的具体的退出代码Shiro已经替我们实现了
		filterChainDefinitionMap.put("/logout", "logout");
		//<!-- 过滤链定义，从上向下顺序执行，一般将/**放在最为下边 -->:这是一个坑呢，一不小心代码就不好使了;
		//<!-- authc:所有url都必须认证通过才可以访问; anon:所有url都都可以匿名访问-->
		filterChainDefinitionMap.put("/filter/**", "authc");
		filterChainDefinitionMap.put("/adminI*/**", "authc");
		filterChainDefinitionMap.put("/**", "anon");
		// 如果不设置默认会自动寻找Web工程根目录下的"/login.jsp"页面
		shiroFilterFactoryBean.setLoginUrl("/login.html");
		// 登录成功后要跳转的链接
		shiroFilterFactoryBean.setSuccessUrl("/filter/welcome");
		//未授权界面;
		shiroFilterFactoryBean.setUnauthorizedUrl("/403");
		shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);
		return shiroFilterFactoryBean;
	}

	/**
	 * 
	 * @Description: 配置用户ShiroRealm 用户匹配tb_user表
	 * @param 
	 * @return UserShiroRealm
	 * @author liangzd
	 * @date 2018年6月16日 下午7:19:08
	 */
	@Bean
	public UserShiroRealm userShiroRealm(){
		UserShiroRealm userShiroRealm = new UserShiroRealm();
		return userShiroRealm;
	}
	
	/**
	 * 
	 * @Description: 密码匹配过程 配置使用MD5加密，加密次数1024次
	 * @param 
	 * @return HashedCredentialsMatcher
	 * @author liangzd
	 * @date 2018年6月16日 下午7:19:51
	 */
	@Bean
	public HashedCredentialsMatcher hashedCredentialsMatcher() {
		HashedCredentialsMatcher hashedCredentialsMatcher = new HashedCredentialsMatcher();
		hashedCredentialsMatcher.setHashAlgorithmName("MD5");
		hashedCredentialsMatcher.setHashIterations(1024);
		return hashedCredentialsMatcher;
	}
	
	/**
	 * 
	 * @Description: 配置管理员ShiroRealm 用户匹配tb_admin_user表
	 * @param 
	 * @return AdminShiroRealm
	 * @author liangzd
	 * @date 2018年6月16日 下午7:20:48
	 */
	@Bean
	public AdminShiroRealm adminShiroRealm(){
		AdminShiroRealm adminShiroRealm = new AdminShiroRealm();
		adminShiroRealm.setCredentialsMatcher(hashedCredentialsMatcher());
		return adminShiroRealm;
	}

	/**
	 * 
	 * @Description: 配置自定义业务继承模块化Realm认证器
	 * @param 
	 * @return CustomizedModularRealmAuthenticator
	 * @author liangzd
	 * @date 2018年6月16日 下午7:22:08
	 */
	@Bean
	public CustomizedModularRealmAuthenticator customizedModularRealmAuthenticator() {
		CustomizedModularRealmAuthenticator customizedModularRealmAuthenticator = new CustomizedModularRealmAuthenticator();
		customizedModularRealmAuthenticator.setAuthenticationStrategy(atLeastOneSuccessfulStrategy());
		return customizedModularRealmAuthenticator;
	}

	/**
	 * 
	 * @Description: 配置Realm认证方式:如果一个（或更多）Realm 验证成功，则整体的尝试被认为是成功的。如果没有一个验证成功，则整体尝试失败
	 * @param 
	 * @return AtLeastOneSuccessfulStrategy
	 * @author liangzd
	 * @date 2018年6月16日 下午7:24:33
	 */
	@Bean
	public AtLeastOneSuccessfulStrategy atLeastOneSuccessfulStrategy() {
		AtLeastOneSuccessfulStrategy atLeastOneSuccessfulStrategy = new AtLeastOneSuccessfulStrategy();
		return atLeastOneSuccessfulStrategy;
	}
	
	/**
	 * 
	 * @Description: 将自定义业务继承模块化Realm认证器、用户Realm、管理员Realm配置进安全管理器
	 * @param 
	 * @return SecurityManager
	 * @author liangzd
	 * @date 2018年6月16日 下午7:25:37
	 */
	@Bean
	public SecurityManager securityManager(){
		DefaultWebSecurityManager securityManager =  new DefaultWebSecurityManager();
		securityManager.setAuthenticator(customizedModularRealmAuthenticator());
		Collection<Realm> realms = new ArrayList<Realm>();
		realms.add(userShiroRealm());
		realms.add(adminShiroRealm());
		securityManager.setRealms(realms);
		return securityManager;
	}
}