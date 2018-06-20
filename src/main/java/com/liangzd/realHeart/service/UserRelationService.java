package com.liangzd.realHeart.service;

import java.util.List;
import java.util.Optional;

import com.liangzd.realHeart.entity.TbUserRelation;

/**
 * 
 * @Description: 处理用户间的关系的业务处理接口
 * @author liangzd
 * @date 2018年6月20日 下午2:21:54
 */
public interface UserRelationService {
	/**
	 * 
	 * @Description: 根据用户1的uid和用户2的uid查找他们间的用户关系
	 * @param 
	 * @return Optional<TbUserRelation>
	 * @author liangzd
	 * @date 2018年6月20日 下午2:35:02
	 */
	public Optional<TbUserRelation> findByUidAndTargetUid(Integer uid, Integer targetUid);
	
	/**
	 * 
	 * @Description: 根据uid查找与其的所有用户的uid值,@TODO此处可优,与{@link UserRelationService#findUserByUidAndAndRelations}
	 * @param 
	 * @return List<Integer>
	 * @author liangzd
	 * @date 2018年6月20日 下午2:35:33
	 */
	public List<Integer> findByUid(Integer uid);
	
	/**
	 * 
	 * @Description: 保存用户间的关系
	 * @param 
	 * @return void
	 * @author liangzd
	 * @date 2018年6月20日 下午2:36:05
	 */
	public void saveUserRelation(TbUserRelation tbUserRelation);
	
	/**
	 * 
	 * @Description: 根据用户1的uid值与用户2的关系类型查找系统匹配的用户uid值
	 * @param 
	 * @return List<Integer>
	 * @author liangzd
	 * @date 2018年6月20日 下午2:36:24
	 */
	public List<Integer> findUserByUidAndAndRelations(Integer uid, String firstRelation, String secondRelation);
}
