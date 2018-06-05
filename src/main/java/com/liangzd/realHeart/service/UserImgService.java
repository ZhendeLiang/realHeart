package com.liangzd.realHeart.service;

import java.util.List;

import com.liangzd.realHeart.entity.TbUserImg;

public interface UserImgService {
	public List<TbUserImg> findByUid(Integer uid);
	public List<TbUserImg> findByUidAndImgType(Integer uid, String imgType);
	public void deleteByUidAndImgType(Integer uid, String imgType, Integer increasement);
	public void addUserImg(List<TbUserImg> tbUserImgs);
}
