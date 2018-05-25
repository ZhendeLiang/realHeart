package com.liangzd.realHeart.service;

import java.util.Optional;

import com.liangzd.realHeart.entity.User;

public interface LoginService {
	public Optional<User> getUserByIdentity(String identityInfo);
}
