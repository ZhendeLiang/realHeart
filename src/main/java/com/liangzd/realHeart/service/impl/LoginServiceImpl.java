package com.liangzd.realHeart.service.impl;

import java.util.Iterator;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.druid.util.StringUtils;
import com.liangzd.realHeart.dao.UserDao;
import com.liangzd.realHeart.entity.User;
import com.liangzd.realHeart.service.LoginService;
import com.liangzd.realHeart.util.MethodUtil;

@Service
public class LoginServiceImpl implements LoginService{
	@Autowired
	private UserDao userDao;
	
	public UserDao getUserDao() {
		return userDao;
	}

	public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
	}

	/**
	 * 
	 * @Description: 获取根据UID、手机号、用户名、邮箱其一获取到的用户
	 * @param 
	 * @return 
	 * @author liangzd
	 * @date 2018年5月25日 下午10:07:10
	 */
	@Override
	public Optional<User> getUserByIdentity(String identityInfo) {
		Optional<User> users = null;
		if(!StringUtils.isEmpty(identityInfo)) {
			if(MethodUtil.isInteger(identityInfo)) {
				if(identityInfo.length() >= 11) {
					users = userDao.findByPhoneNumber(identityInfo);
					if(users != null && users.isPresent()) {
						return users.isPresent() ? users : null;
					}
				}else {
					users = userDao.findById(Integer.parseInt(identityInfo));
					if(users != null && users.isPresent()) {
						return users.isPresent() ? users : null;
					}
				}
			}else {
				users = userDao.findByUsername(identityInfo);
				if(users != null && users.isPresent()) {
					return users;
				}else {
					users = userDao.findByEmail(identityInfo);
					if(users != null && users.isPresent()) {
						return users.isPresent() ? users : null;
					}
				}
			}
		}
		return users;
	}

	
}
