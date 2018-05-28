package com.liangzd.realHeart.service.impl;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.druid.util.StringUtils;
import com.liangzd.realHeart.dao.AdminUserDao;
import com.liangzd.realHeart.dao.TrUserAddressDao;
import com.liangzd.realHeart.dao.TrUserViprankDao;
import com.liangzd.realHeart.dao.UserDao;
import com.liangzd.realHeart.entity.TrUserAddress;
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
	
	@Autowired
	private TrUserAddressDao trUserAddressDao;
	
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
	
	public TrUserAddressDao getTrUserAddressDao() {
		return trUserAddressDao;
	}

	public void setTrUserAddressDao(TrUserAddressDao trUserAddressDao) {
		this.trUserAddressDao = trUserAddressDao;
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

	@Override
	public void deleteUser(User user) {
		//删除用户会员外键信息
		TrUserViprank trUserViprank = null; 
		Optional<TrUserViprank> viprankResult = trUserViprankDao.findByUserId(user.getUid());
		if(viprankResult.isPresent()) {
			trUserViprank = viprankResult.get();
			trUserViprankDao.delete(trUserViprank);
		}
		
		//删除地址外键信息
		List<TrUserAddress> addresss = trUserAddressDao.findByUserId(user.getUid());
		if(addresss.size() != 0) {
			for(TrUserAddress trUserAddress : addresss) {
				trUserAddressDao.delete(trUserAddress);
			}
		}
		
		userDao.delete(user);
	}

	@Override
	public List<User> findAllUserWithSearch(User user) {
		List<User> users = null;
		List<User> finallyUsers = new ArrayList<User>();
		String username = StringUtils.isEmpty(user.getUsername()) ? "%" : "%"+user.getUsername()+"%";
		String email = StringUtils.isEmpty(user.getEmail()) ? "%" : "%"+user.getEmail()+"%";
		String phoneNumber = StringUtils.isEmpty(user.getPhoneNumber()) ? "%" : "%"+user.getPhoneNumber()+"%";
		String gender = "-1".equals(user.getGender()) ? "%" : user.getGender();
		if("-1".equals(String.valueOf(user.getState()))) {
			users = userDao.findUserByUsernameLikeAndEmailLikeAndPhoneNumberLikeAndGenderLike(
					username, email, phoneNumber, gender);
		}else {
			users = userDao.findUserByUsernameLikeAndEmailLikeAndPhoneNumberLikeAndGenderLikeAndStateLike(
					username, email, phoneNumber, gender, user.getState());
		}
		if(!"-1".equals(user.getViprankName())) {
			List<TrUserViprank> trUserVipranks = trUserViprankDao.findByViprankId(Integer.parseInt(user.getViprankName()));
			for(User queryUser : users) {
				for(TrUserViprank trUserViprank : trUserVipranks) {
					if(trUserViprank.getUserId().equals(queryUser.getUid())) {
						finallyUsers.add(queryUser);
					}
				}
			}
		}else {
			finallyUsers = users;
		}
		return finallyUsers;
	}
	
}
