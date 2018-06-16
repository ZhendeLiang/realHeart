package com.liangzd.realHeart.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.liangzd.realHeart.entity.TbUserRelation;

/**
 * 
 * @Description: 用户关系对象的持久化操作类,tb_user_relation
 * @author liangzd
 * @date 2018年6月16日 下午9:01:07
 */
@Repository
public interface UserRelationDao extends JpaRepository<TbUserRelation, Integer>{
	/**
	 * 
	 * @Description: 查找所有 等于firstUid和 不等于firstUserRelation 的用户关系对象
	 * @param 
	 * @return List<TbUserRelation>
	 * @author liangzd
	 * @date 2018年6月16日 下午9:01:35
	 */
	public List<TbUserRelation> findByFirstUidAndFirstUserRelationNot(Integer firstUid, String firstUserRelation);
	
	/**
	 * 
	 * @Description: 查找所有 等于secondUid和 不等于firstUserRelation 的用户关系对象
	 * @param 
	 * @return List<TbUserRelation>
	 * @author liangzd
	 * @date 2018年6月16日 下午9:02:40
	 */
	public List<TbUserRelation> findBySecondUidAndSecondUserRelationNot(Integer secondUid, String firstUserRelation);
	
	/**
	 * 
	 * @Description: 查找所有 等于firstUid、等于firstUserRelation、等于secondUserRelation的 用户对象
	 * @param 
	 * @return List<TbUserRelation>
	 * @author liangzd
	 * @date 2018年6月16日 下午9:03:02
	 */
	public List<TbUserRelation> findByFirstUidAndFirstUserRelationAndSecondUserRelation(Integer firstUid, String firstUserRelation, String secondUserRelation);
	
	/**
	 * 
	 * @Description: 查找所有 等于secondUid、等于firstUserRelation、等于secondUserRelation的 用户对象
	 * @param 
	 * @return List<TbUserRelation>
	 * @author liangzd
	 * @date 2018年6月16日 下午9:04:23
	 */
	public List<TbUserRelation> findBySecondUidAndFirstUserRelationAndSecondUserRelation(Integer secondUid, String firstUserRelation, String secondUserRelation);
	
	/**
	 * 
	 * @Description: 查找所有 等于firstUid、等于secondUid的 用户对象
	 * @param 
	 * @return Optional<TbUserRelation>
	 * @author liangzd
	 * @date 2018年6月16日 下午9:04:38
	 */
	public Optional<TbUserRelation> findByFirstUidAndSecondUid(Integer firstUid, Integer secondUid);
}
