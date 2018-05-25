package com.liangzd.realHeart.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.liangzd.realHeart.entity.User;

@Repository
public interface UserDao extends JpaRepository<User, Integer>{

	public Optional<User> findByEmail(String email);
	public Optional<User> findByUsername(String username);
	public Optional<User> findByPhoneNumber(String phoneNumber);
}
