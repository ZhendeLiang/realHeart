package com.liangzd.realHeart.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;

import com.liangzd.realHeart.entity.TrUserViprank;
import com.liangzd.realHeart.entity.User;

/**
 * 
 * @Description: 处理关于用户的增删查改业务处理接口
 * @author liangzd
 * @date 2018年6月20日 下午2:21:54
 */
public interface UserService {
	/**
	 * 
	 * @Description: 查找所有用户,无条件返回
	 * @param 
	 * @return List<User>
	 * @author liangzd
	 * @date 2018年6月20日 下午2:45:36
	 */
	public List<User> findAllUsers();
	
	/**
	 * 
	 * @Description: 根据用户uids,批量查找用户
	 * @param 
	 * @return List<User>
	 * @author liangzd
	 * @date 2018年6月20日 下午2:51:19
	 */
	public List<User> findAllUsersByUid(List<Integer> uids);
	
	/**
	 * 
	 * @Description: 查找所有用户,级联抓取用户的会员等级名称ViprankName
	 * @param 
	 * @return List<User>
	 * @author liangzd
	 * @date 2018年6月20日 下午2:52:40
	 */
	public List<User> findAllUsersWithViprankName();
	
	/**
	 * 
	 * @Description: 模糊查询符合调教的所有用户
	 * @param 
	 * @return List<User>
	 * @author liangzd
	 * @date 2018年6月20日 下午2:53:10
	 */
	public List<User> findAllUserWithSearch(User user);
	
	/**
	 * 
	 * @Description: 添加用户
	 * @param 
	 * @return void
	 * @author liangzd
	 * @date 2018年6月20日 下午2:53:38
	 */
	public void addUser(User user);
	
	/**
	 * 
	 * @Description: 根据用户uid查找用户
	 * @param 
	 * @return Optional<User>
	 * @author liangzd
	 * @date 2018年6月20日 下午2:54:06
	 */
	public Optional<User> findById(int id);
	
	/**
	 * 
	 * @Description: 根据用户uid查找用户,级联抓取用户的会员等级名称ViprankName
	 * @param 
	 * @return User
	 * @author liangzd
	 * @date 2018年6月20日 下午2:55:03
	 */
	public User findByIdWithViprankName(int id);
	
	/**
	 * 
	 * @Description: 删除用户
	 * @param 
	 * @return void
	 * @author liangzd
	 * @date 2018年6月20日 下午2:55:57
	 */
	public void deleteUser(User user);
	
	/**
	 * 
	 * @Description: 根据用户名查找用户
	 * @param 
	 * @return Optional<User>
	 * @author liangzd
	 * @date 2018年6月20日 下午2:56:06
	 */
	public Optional<User> findByUsername(String username);
	
	/**
	 * 
	 * @Description: 根据手机号查找用户
	 * @param 
	 * @return Optional<User>
	 * @author liangzd
	 * @date 2018年6月20日 下午2:56:19
	 */
	public Optional<User> findByPhoneNumber(String phoneNumber);
	
	/**
	 * 
	 * @Description: 根据邮箱查找用户
	 * @param 
	 * @return Optional<User>
	 * @author liangzd
	 * @date 2018年6月20日 下午2:56:34
	 */
	public Optional<User> findByEmail(String email);
	
	/**
	 * 
	 * @Description: 根据用户uid和密码,更新用户的密码
	 * @param 
	 * @return void
	 * @author liangzd
	 * @date 2018年6月20日 下午2:56:47
	 */
	public void updateUserPassword(Integer uid, String password);
	
	/**
	 * 
	 * @Description: 根据用户的唯一信息(用户uid、用户名、手机号、邮箱)查找用户
	 * @param 
	 * @return User
	 * @author liangzd
	 * @date 2018年6月20日 下午2:57:18
	 */
	public User queryByIdentityInfo(String identityInfo);
	
	/**
	 * 
	 * @Description: 根据用户的性别和用户状态(0可用,1不可用)查找用户信息
	 * @param 
	 * @return Page<User>
	 * @author liangzd
	 * @date 2018年6月20日 下午2:58:28
	 */
	public Page<User> findByGenderAndState(String gender,Byte state, Integer pageNum, Integer pageSize, List<Integer> uids);
	
	/**
	 * 
	 * @Description: 根据用户的uid查找用户的会员等级关系关系
	 * @param 
	 * @return TrUserViprank
	 * @author liangzd
	 * @date 2018年6月20日 下午2:59:01
	 */
	public TrUserViprank findUserViprankByUserId(Integer id);
}
