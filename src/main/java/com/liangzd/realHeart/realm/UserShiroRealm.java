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
import com.liangzd.realHeart.dao.UserDao;
import com.liangzd.realHeart.entity.Permission;
import com.liangzd.realHeart.entity.Role;
import com.liangzd.realHeart.entity.User;
import com.liangzd.realHeart.util.MethodUtil;
/**
 * 
 * @Description: 用户的用户登陆验证方法，继承AuthorizingRealm类，并重写身份验证方法和授权方法
 * @author liangzd
 * @date 2018年6月16日 下午9:53:38
 */
public class UserShiroRealm extends AuthorizingRealm{
	@Autowired
	private UserDao userDao;
	
	public UserDao getUserDao() {
		return userDao;
	}

	public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
	}
	
	@Override
	public void setName(String name) {
		super.setName("UserShiroRealm");
	}
	
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token)
	        throws AuthenticationException {
	    //实际项目中，这里可以根据实际情况做缓存，如果不做，Shiro自己也是有时间间隔机制，2分钟内不会重复执行该方法
//	    UserInfo userInfo = userInfoService.findByUsername(username);
//		User user = null;
//		Optional<User> users = LoginService.getUserByIdentity((String)token.getPrincipal());
		
		String identityInfo = (String)token.getPrincipal();
		User user;
		Optional<User> users = null;
		Optional<User> realUsers = null;
		if(!StringUtils.isEmpty(identityInfo)) {
			if(MethodUtil.isInteger(identityInfo)) {
				if(identityInfo.length() >= 11) {
					users = userDao.findByPhoneNumber(identityInfo);
					if(users != null && users.isPresent()) {
						realUsers = users.isPresent() ? users : null;
					}
				}else {
					users = userDao.findById(Integer.parseInt(identityInfo));
					if(users != null && users.isPresent()) {
						realUsers = users.isPresent() ? users : null;
					}
				}
			}else {
				users = userDao.findByUsername(identityInfo);
				if(users != null && users.isPresent()) {
					realUsers = users;
				}else {
					users = userDao.findByEmail(identityInfo);
					if(users != null && users.isPresent()) {
						realUsers = users.isPresent() ? users : null;
					}
				}
			}
		}
		if(realUsers == null || !realUsers.isPresent()) {
			throw new UnknownAccountException();
		}else {
			user = realUsers.get();
		}
	    SimpleAuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo(
	    		user.getUsername(), //用户名
	    		user.getPassword(), //密码
//	            ByteSource.Util.bytes(user.getSalt()),//salt=username+salt
	            getName()  //realm name
	    );
	    return authenticationInfo;
	}

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
