package com.liangzd.realHeart.realm;

import java.util.Optional;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.druid.util.StringUtils;
import com.liangzd.realHeart.dao.AdminUserDao;
import com.liangzd.realHeart.entity.AdminUser;
import com.liangzd.realHeart.entity.Permission;
import com.liangzd.realHeart.entity.Role;
import com.liangzd.realHeart.entity.User;

/**
 * 
 * @Description: 管理员的用户登陆验证方法，继承AuthorizingRealm类，并重写身份验证方法和授权方法
 * @author liangzd
 * @date 2018年6月16日 下午9:53:38
 */
public class AdminShiroRealm extends AuthorizingRealm{
	@Autowired
	private AdminUserDao adminUserDao;

	public AdminUserDao getAdminUserDao() {
		return adminUserDao;
	}

	public void setAdminUserDao(AdminUserDao adminUserDao) {
		this.adminUserDao = adminUserDao;
	}

	@Override
	public void setName(String name) {
		super.setName("AdminShiroRealm");
	}
	
	/**
	 * 
	 * @Description: 重写身份验证方法,管理员身份匹配tb_admin_user
	 * @param 
	 * @return 
	 * @author liangzd
	 * @date 2018年6月20日 下午2:18:46
	 */
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token)
	        throws AuthenticationException {
	    //实际项目中，这里可以根据实际情况做缓存，如果不做，Shiro自己也是有时间间隔机制，2分钟内不会重复执行该方法
//	    UserInfo userInfo = userInfoService.findByUsername(username);
		String adminUsername = (String)token.getPrincipal();
		AdminUser adminUser;
		Optional<AdminUser> adminUsers = null;
		Optional<AdminUser> realAdminUsers = null;
		if(!StringUtils.isEmpty(adminUsername)) {
			adminUsers = adminUserDao.findByAdminUsername(adminUsername);
			if(adminUsers != null && adminUsers.isPresent()) {
				realAdminUsers = adminUsers.isPresent() ? adminUsers : null;
			}
		}
		if(realAdminUsers == null || !realAdminUsers.isPresent()) {
			throw new UnknownAccountException();
		}else {
			adminUser = realAdminUsers.get();
		}
		SimpleAuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo(
				adminUser.getAdminUsername(), //用户名
				adminUser.getAdminPassword(), //密码
		//		ByteSource.Util.bytes(user.getSalt()),//salt=username+salt
		        getName()  //realm name
		);
	    return authenticationInfo;
	}

	/**
	 * 
	 * @Description: 重写获取授权的方法,此处没用上,@TODO二期开始
	 * @param 
	 * @return 
	 * @author liangzd
	 * @date 2018年6月20日 下午2:17:58
	 */
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
		System.out.println("权限配置-->MyShiroRealm.doGetAuthorizationInfo()");
	    SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
	    User userInfo  = (User)principals.getPrimaryPrincipal();
	    for(Role role:userInfo.getRoleList()){
	        authorizationInfo.addRole(role.getRole());
	        for(Permission p:role.getPermissions()){
	            authorizationInfo.addStringPermission(p.getPermission());
	        }
	    }
	    return authorizationInfo;
	}
}
