package com.liangzd.realHeart.service;

import java.util.List;
import java.util.Optional;

import com.liangzd.realHeart.entity.User;

public interface UserService {
	public List<User> findAllUsers();
	public List<User> findAllUsersWithViprankName();
	public void addUser(User user);
}
