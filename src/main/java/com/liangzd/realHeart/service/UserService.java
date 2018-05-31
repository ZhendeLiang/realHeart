package com.liangzd.realHeart.service;

import java.util.List;
import java.util.Optional;

import com.liangzd.realHeart.entity.User;

public interface UserService {
	public List<User> findAllUsers();
	public List<User> findAllUsersWithViprankName();
	public List<User> findAllUserWithSearch(User user);
	public void addUser(User user);
	public Optional<User> findById(int id);
	public User findByIdWithViprankName(int id);
	public void deleteUser(User user);
	public Optional<User> findByPhoneNumber(String phoneNumber);
}
