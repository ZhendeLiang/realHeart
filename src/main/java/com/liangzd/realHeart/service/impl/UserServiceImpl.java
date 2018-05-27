package com.liangzd.realHeart.service.impl;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.druid.util.StringUtils;
import com.liangzd.realHeart.dao.AdminUserDao;
import com.liangzd.realHeart.dao.TrUserViprankDao;
import com.liangzd.realHeart.dao.UserDao;
import com.liangzd.realHeart.entity.TrUserViprank;
import com.liangzd.realHeart.entity.User;
import com.liangzd.realHeart.service.UserService;

@Service
public class UserServiceImpl implements UserService{
	@Autowired
	private UserDao userDao;
	
	@Autowired
	private AdminUserDao adminUserDao;
	
	@Autowired
	private TrUserViprankDao trUserViprankDao;
	
	public UserDao getUserDao() {
		return userDao;
	}

	public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
	}
	
	public AdminUserDao getAdminUserDao() {
		return adminUserDao;
	}

	public void setAdminUserDao(AdminUserDao adminUserDao) {
		this.adminUserDao = adminUserDao;
	}

	public List<User> findAllUsers() {
		List<User> users = userDao.findAll();
		return users;
	}
	
	public TrUserViprankDao getTrUserViprankDao() {
		return trUserViprankDao;
	}

	public void setTrUserViprankDao(TrUserViprankDao trUserViprankDao) {
		this.trUserViprankDao = trUserViprankDao;
	}

	public List<User> findAllUsersWithViprankName() {
		List<Map<String,String>> maps= userDao.findAllWithViprankName();
		List<User> users = new ArrayList<User>();
		for(Map<String,String> map : maps) {
			User user = new User();
			user.setUid(Integer.parseInt(map.get("uid")));
			user.setUsername(map.get("username"));
			user.setGender(map.get("gender"));
			user.setEmail(map.get("email"));
			user.setPhoneNumber(map.get("phone_number"));
			user.setViprankName(map.get("viprank_name"));
			user.setState((byte)Integer.parseInt(map.get("state")));
			users.add(user);
		}
		return users;
	}

	@Transactional
	@Override
	public void addUser(User user) {
		User updateUser = userDao.saveAndFlush(user);
		if(!StringUtils.isEmpty(user.getViprankName())) {
			TrUserViprank trUserViprank = new TrUserViprank();
			int viprankId = Integer.parseInt(user.getViprankName());
			Optional<TrUserViprank> result = trUserViprankDao.findByUserId(updateUser.getUid());
			if(!result.isPresent()) {
				trUserViprank.setUserId(updateUser.getUid());
				trUserViprank.setViprankId(viprankId);
			}else {
				trUserViprank = result.get();
				trUserViprank.setViprankId(viprankId);
			}
			trUserViprankDao.saveAndFlush(trUserViprank);
		}
	}

	@Override
	public Optional<User> findById(int id) {
		Optional<User> findById = userDao.findById(id);
		return findById;
	}

	@Override
	public User findByIdWithViprankName(int id) {
		Map<String,String> map= userDao.findUserByIdWithViprankName(id);
		User user = new User();
		user.setUid(Integer.parseInt(map.get("uid")));
		user.setUsername(map.get("username"));
		user.setGender(map.get("gender"));
		user.setEmail(map.get("email"));
		user.setPhoneNumber(map.get("phone_number"));
		user.setViprankName(map.get("viprank_id"));
		user.setState((byte)Integer.parseInt(map.get("state")));
		user.setSelfIntroduction(map.get("self_introduction"));
		return user;
	}
}
