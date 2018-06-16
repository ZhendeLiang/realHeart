package com.liangzd.realHeart.service.impl;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
import com.liangzd.realHeart.util.MethodUtil;

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

	/**
	 * 
	 * @Description: 获取所有用户，并级联抓取会员等级信息
	 * @param 
	 * @return 
	 * @author liangzd
	 * @date 2018年5月31日 下午4:51:45
	 */
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

	/**
	 * 属于事务管理范畴
	 * @Description: 添加用户，同时修改会员等级关系表
	 * @param 
	 * @return 
	 * @author liangzd
	 * @date 2018年5月31日 下午4:51:06
	 */
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

	/**
	 * 
	 * @Description: 根据Id查找用户
	 * @param 
	 * @return 
	 * @author liangzd
	 * @date 2018年5月31日 下午4:50:42
	 */
	@Override
	public Optional<User> findById(int id) {
		Optional<User> findById = userDao.findById(id);
		return findById;
	}

	/**
	 * 
	 * @Description: 根据ID查找用户，级联抓取会员等级名字
	 * @param 
	 * @return 
	 * @author liangzd
	 * @date 2018年5月31日 下午4:50:14
	 */
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

	/**
	 * 
	 * @Description: 删除用户
	 * @param 
	 * @return 
	 * @author liangzd
	 * @date 2018年5月31日 下午4:50:03
	 */
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

	/**
	 * 
	 * @Description: 条件查询所有用户
	 * @param 
	 * @return 
	 * @author liangzd
	 * @date 2018年5月31日 下午4:49:47
	 */
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

	/**
	 * 
	 * @Description: 根据用户名查找用户
	 * @param 
	 * @return 
	 * @author liangzd
	 * @date 2018年6月2日 上午2:01:45
	 */
	@Override
	public Optional<User> findByUsername(String username) {
		return userDao.findByUsername(username);
	}
	
	/**
	 * 
	 * @Description: 根据手机号查找用户
	 * @param 
	 * @return 
	 * @author liangzd
	 * @date 2018年5月31日 下午4:53:14
	 */
	@Override
	public Optional<User> findByPhoneNumber(String phoneNumber) {
		return userDao.findByPhoneNumber(phoneNumber);
	}

	/**
	 * 
	 * @Description: 根据邮箱查找用户
	 * @param 
	 * @return 
	 * @author liangzd
	 * @date 2018年6月2日 上午2:01:45
	 */
	@Override
	public Optional<User> findByEmail(String email) {
		return userDao.findByEmail(email);
	}

	@Transactional
	@Override
	public void updateUserPassword(Integer uid, String password) {
		userDao.updateUserPassword(uid, password);
	}
	
	public User queryByIdentityInfo(String identityInfo) {
		Optional<User> users = null;
		if(!StringUtils.isEmpty(identityInfo)) {
			if(MethodUtil.isInteger(identityInfo)) {
				if(identityInfo.length() >= 11) {
					users = userDao.findByPhoneNumber(identityInfo);
					if(users != null && users.isPresent()) {
						return users.get();
					}
				}else {
					users = userDao.findById(Integer.parseInt(identityInfo));
					if(users != null && users.isPresent()) {
						return users.get();
					}
				}
			}else {
				users = userDao.findByUsername(identityInfo);
				if(users != null && users.isPresent()) {
					return users.get();
				}else {
					users = userDao.findByEmail(identityInfo);
					if(users != null && users.isPresent()) {
						return users.get();
					}
				}
			}
		}
		return null;
	}

	@Override
	public Page<User> findByGenderAndState(String gender, Byte state, Integer pageNum, Integer pageSize, List<Integer> uids) {
		Sort sort = new Sort(Sort.Direction.ASC,"uid"); //创建时间降序排序
		Pageable pageable = PageRequest.of(pageNum,pageSize,sort);
		if(uids.size() == 0) {
			return userDao.findByGenderAndState(gender, state, pageable);
		}else {
			return userDao.findByGenderAndStateNotInUids(gender, state, uids, pageable);
		}
	}

	@Override
	public TrUserViprank findUserViprankByUserId(Integer id) {
		TrUserViprank trUserViprank = null;
		Optional<TrUserViprank> hasUserViprank = trUserViprankDao.findByUserId(id);
		trUserViprank = hasUserViprank.isPresent() ? hasUserViprank.get() : null; 
		return trUserViprank;
	}

	@Override
	public List<User> findAllUsersByUid(List<Integer> uids) {
		return userDao.findAllById(uids);
	}
}
