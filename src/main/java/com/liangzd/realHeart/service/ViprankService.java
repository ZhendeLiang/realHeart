package com.liangzd.realHeart.service;

import com.liangzd.realHeart.entity.TbViprank;

/**
 * 
 * @Description: 处理用户会员等级的业务处理接口
 * @author liangzd
 * @date 2018年6月20日 下午2:21:54
 */
public interface ViprankService {
	
	/**
	 * 
	 * @Description: 根据用户会员等级id查找会员登记表对应的持久化类
	 * @param 
	 * @return TbViprank
	 * @author liangzd
	 * @date 2018年6月20日 下午3:04:32
	 */
	public TbViprank findById(Integer id);
}
