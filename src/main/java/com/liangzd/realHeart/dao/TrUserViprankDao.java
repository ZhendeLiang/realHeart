package com.liangzd.realHeart.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.liangzd.realHeart.entity.TrUserViprank;

/**
 * 
 * @Description: 用户与会员详细信息的关系对象的持久化操作类,tr_user_viprank
 * @author liangzd
 * @date 2018年6月16日 下午8:42:15
 */
@Repository
public interface TrUserViprankDao extends JpaRepository<TrUserViprank, Integer>{
	/**
	 * 
	 * @Description: 根据用户uid查找 用户与会员详细信息的关系对象
	 * @param 
	 * @return Optional<TrUserViprank>
	 * @author liangzd
	 * @date 2018年6月16日 下午8:43:19
	 */
	public Optional<TrUserViprank> findByUserId(Integer userId);
	/**
	 * 
	 * @Description: 根据会员等级id查找所有的 用户与会员详细信息的关系对象
	 * @param 
	 * @return List<TrUserViprank>
	 * @author liangzd
	 * @date 2018年6月16日 下午8:44:10
	 */
	public List<TrUserViprank> findByViprankId(Integer viprankId);
}
