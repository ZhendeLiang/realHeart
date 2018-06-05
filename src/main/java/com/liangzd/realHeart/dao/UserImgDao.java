package com.liangzd.realHeart.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.liangzd.realHeart.entity.TbUserImg;

@Repository
public interface UserImgDao extends JpaRepository<TbUserImg, Integer>{

	public List<TbUserImg> findByUid(Integer uid);
	public List<TbUserImg> findByUidAndImgType(Integer uid, String imgType);
	
	@Modifying
    @Query("delete from tb_user_img where uid = :uid and img_type = :imgType and increasement != :increasement")
	public void deleteByUidAndImgType(@Param("uid")Integer uid, @Param("imgType")String imgType, 
			@Param("increasement")Integer increasement);
	
	@Modifying
    @Query("delete from tb_user_img where uid = :uid and img_type = :imgType")
	public void deleteByUidAndImgType(@Param("uid")Integer uid, @Param("imgType")String imgType);
}
