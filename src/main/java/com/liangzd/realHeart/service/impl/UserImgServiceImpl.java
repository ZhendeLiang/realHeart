package com.liangzd.realHeart.service.impl;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;

import com.liangzd.realHeart.dao.UserImgDao;
import com.liangzd.realHeart.entity.TbUserImg;
import com.liangzd.realHeart.service.UserImgService;
import com.liangzd.realHeart.util.ConstantParams;

@Service
public class UserImgServiceImpl implements UserImgService {
	@Autowired
	private UserImgDao userImgDao;
	
	public UserImgDao getUserImgDao() {
		return userImgDao;
	}

	public void setUserImgDao(UserImgDao userImgDao) {
		this.userImgDao = userImgDao;
	}

	@Override
	public List<TbUserImg> findByUid(Integer uid) {
		return userImgDao.findByUid(uid);
	}

	@Override
	public void deleteByUidAndImgType(Integer uid, String imgType,Integer increasement) {
		userImgDao.deleteByUidAndImgType(uid, imgType, increasement);
	}
	
	@Transactional
	@Override
	public void addUserImg(List<TbUserImg> tbUserImgs) {
		if(ConstantParams.HEADIMG.equals(tbUserImgs.get(0).getImgType())) {
			userImgDao.deleteByUidAndImgType(tbUserImgs.get(0).getUid(), tbUserImgs.get(0).getImgType());
		}else {
			userImgDao.deleteByUidAndImgType(tbUserImgs.get(0).getUid(), tbUserImgs.get(0).getImgType(),
					tbUserImgs.get(0).getIncreasement());
		}
		userImgDao.saveAll(tbUserImgs);
	}

	@Override
	public List<TbUserImg> findByUidAndImgType(Integer uid, String imgType) {
		return userImgDao.findByUidAndImgType(uid, imgType);
	}

}
