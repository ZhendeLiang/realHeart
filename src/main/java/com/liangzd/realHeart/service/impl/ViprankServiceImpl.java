package com.liangzd.realHeart.service.impl;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.liangzd.realHeart.dao.TbViprankDao;
import com.liangzd.realHeart.entity.TbViprank;
import com.liangzd.realHeart.service.ViprankService;

/**
 * 
 * @Description: 处理用户会员等级的业务处理接口实现类
 * @author liangzd
 * @date 2018年6月20日 下午2:21:54
 */
@Service
public class ViprankServiceImpl implements ViprankService {
	@Autowired
	private TbViprankDao tbViprankDao;
	
	public TbViprankDao getTbViprankDao() {
		return tbViprankDao;
	}

	public void setTbViprankDao(TbViprankDao tbViprankDao) {
		this.tbViprankDao = tbViprankDao;
	}

	/**
	 * 
	 * @Description: 根据用户会员等级id查找会员登记表对应的持久化类
	 * @param 
	 * @return TbViprank
	 * @author liangzd
	 * @date 2018年6月20日 下午3:04:32
	 */
	@Override
	public TbViprank findById(Integer id) {
		return tbViprankDao.findById(id).get();
	}
}
