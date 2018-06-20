package com.liangzd.realHeart.service;

import java.util.List;

import com.liangzd.realHeart.entity.TbUserImg;

/**
 * 
 * @Description: 处理关于用户头像与背景图片的业务处理接口
 * @author liangzd
 * @date 2018年6月20日 下午2:21:54
 */
public interface UserImgService {
	
	/**
	 * 
	 * @Description: 根据用户的uid查找他的所有头像与背景图片
	 * @param 
	 * @return List<TbUserImg>
	 * @author liangzd
	 * @date 2018年6月20日 下午2:29:47
	 */
	public List<TbUserImg> findByUid(Integer uid);
	
	/**
	 * 
	 * @Description: 根据用户的uid与图片类型(0为头像,1为背景图片)
	 * @param 
	 * @return List<TbUserImg>
	 * @author liangzd
	 * @date 2018年6月20日 下午2:30:47
	 */
	public List<TbUserImg> findByUidAndImgType(Integer uid, String imgType);
	
	/**
	 * 
	 * @Description: 根据用户的uid和图片类型(0为头像,1为背景图片)删除图片
	 * @param 
	 * @return void
	 * @author liangzd
	 * @date 2018年6月20日 下午2:31:22
	 */
	public void deleteByUidAndImgType(Integer uid, String imgType, Integer increasement);
	
	/**
	 * 
	 * @Description: 批量添加用户图片
	 * @param 
	 * @return void
	 * @author liangzd
	 * @date 2018年6月20日 下午2:31:58
	 */
	public void addUserImg(List<TbUserImg> tbUserImgs);
}
