package com.liangzd.realHeart.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.liangzd.realHeart.entity.TbUserImg;

/**
 * 
 * @Description: 用户图片对象的持久化操作类,tb_user_img
 * @author liangzd
 * @date 2018年6月16日 下午8:55:34
 */
@Repository
public interface UserImgDao extends JpaRepository<TbUserImg, Integer>{
	
	/**
	 * 
	 * @Description: 根据用户uid查找所有 用户图片对象
	 * @param 
	 * @return List<TbUserImg>
	 * @author liangzd
	 * @date 2018年6月16日 下午8:56:29
	 */
	public List<TbUserImg> findByUid(Integer uid);
	
	/**
	 * 
	 * @Description: 根据用户uid和图片类型(0为头像,1为背景图片)查找所有 用户图片对象
	 * @param 
	 * @return List<TbUserImg>
	 * @author liangzd
	 * @date 2018年6月16日 下午8:57:10
	 */
	public List<TbUserImg> findByUidAndImgType(Integer uid, String imgType);
	
	/**
	 * 
	 * @Description: 删除所有的等于用户uid、等于图片类型和不等于最新版本 用户图片对象
	 * @param 
	 * @return void
	 * @author liangzd
	 * @date 2018年6月16日 下午8:57:44
	 */
	@Modifying
    @Query("delete from tb_user_img where uid = :uid and img_type = :imgType and increasement != :increasement")
	public void deleteByUidAndImgType(@Param("uid")Integer uid, @Param("imgType")String imgType, 
			@Param("increasement")Integer increasement);
	
	/**
	 * 
	 * @Description: 删除所有的等于用户uid、等于图片类型  用户图片对象
	 * @param 
	 * @return void
	 * @author liangzd
	 * @date 2018年6月16日 下午9:00:15
	 */
	@Modifying
    @Query("delete from tb_user_img where uid = :uid and img_type = :imgType")
	public void deleteByUidAndImgType(@Param("uid")Integer uid, @Param("imgType")String imgType);
}
