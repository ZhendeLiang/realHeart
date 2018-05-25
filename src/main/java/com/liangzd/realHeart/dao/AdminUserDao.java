package com.liangzd.realHeart.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.liangzd.realHeart.entity.AdminUser;
import com.liangzd.realHeart.entity.User;

@Repository
public interface AdminUserDao extends JpaRepository<AdminUser, Integer>{

	public Optional<AdminUser> findByAdminUsername(String adminUsername);
}
