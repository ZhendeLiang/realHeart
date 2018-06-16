package com.liangzd.realHeart.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.liangzd.realHeart.entity.AdminUser;

/**
 * 
 * @Description: 管理员用户对象的持久化操作类,tb_admin_user
 * @author liangzd
 * @date 2018年6月16日 下午8:35:36
 */
@Repository
public interface AdminUserDao extends JpaRepository<AdminUser, Integer>{

	/**
	 * 
	 * @Description: 根据管理员的用户名查找对象
	 * @param 
	 * @return Optional<AdminUser>
	 * @author liangzd
	 * @date 2018年6月16日 下午8:26:55
	 */
	public Optional<AdminUser> findByAdminUsername(String adminUsername);
}
