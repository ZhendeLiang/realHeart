package com.liangzd.realHeart.service.impl;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.liangzd.realHeart.dao.UserImgDao;
import com.liangzd.realHeart.entity.TbUserImg;
import com.liangzd.realHeart.service.UserImgService;
import com.liangzd.realHeart.util.ConstantParams;

/**
 * 
 * @Description: 处理关于用户头像与背景图片的业务处理接口的实现类
 * @author liangzd
 * @date 2018年6月20日 下午2:21:54
 */
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

	/**
	 * 
	 * @Description: 根据用户的uid查找他的所有头像与背景图片
	 * @param 
	 * @return List<TbUserImg>
	 * @author liangzd
	 * @date 2018年6月20日 下午2:29:47
	 */
	@Override
	public List<TbUserImg> findByUid(Integer uid) {
		return userImgDao.findByUid(uid);
	}

	/**
	 * 
	 * @Description: 根据用户的uid与图片类型(0为头像,1为背景图片)
	 * @param 
	 * @return List<TbUserImg>
	 * @author liangzd
	 * @date 2018年6月20日 下午2:30:47
	 */
	@Override
	public List<TbUserImg> findByUidAndImgType(Integer uid, String imgType) {
		return userImgDao.findByUidAndImgType(uid, imgType);
	}

	/**
	 * 
	 * @Description: 根据用户的uid和图片类型(0为头像,1为背景图片)删除图片,受事务管理
	 * @param 
	 * @return void
	 * @author liangzd
	 * @date 2018年6月20日 下午2:31:22
	 */
	@Transactional
	@Override
	public void deleteByUidAndImgType(Integer uid, String imgType,Integer increasement) {
		userImgDao.deleteByUidAndImgType(uid, imgType, increasement);
	}

	/**
	 * 
	 * @Description: 批量添加用户图片,受事务管理
	 * @param 
	 * @return void
	 * @author liangzd
	 * @date 2018年6月20日 下午2:31:58
	 */
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
}
