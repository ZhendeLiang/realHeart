package com.liangzd.realHeart.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.druid.util.StringUtils;
import com.liangzd.realHeart.dao.UserRelationDao;
import com.liangzd.realHeart.entity.TbUserRelation;
import com.liangzd.realHeart.service.UserRelationService;
import com.liangzd.realHeart.util.ConstantParams;

/**
 * 
 * @Description: 处理用户间的关系的业务处理接口实现类
 * @author liangzd
 * @date 2018年6月20日 下午2:21:54
 */
@Service
public class UserRelationServiceImpl implements UserRelationService{
	@Autowired
	private UserRelationDao userRelationDao; 
	/**
	 * 
	 * @Description: 根据用户1的uid和用户2的uid查找他们间的用户关系
	 * @param 
	 * @return Optional<TbUserRelation>
	 * @author liangzd
	 * @date 2018年6月20日 下午2:35:02
	 */
	@Override
	public Optional<TbUserRelation> findByUidAndTargetUid(Integer uid, Integer targetUid) {
		if(userRelationDao.findByFirstUidAndSecondUid(uid, targetUid).isPresent()) {
			return userRelationDao.findByFirstUidAndSecondUid(uid, targetUid);
		}else {
			return userRelationDao.findByFirstUidAndSecondUid(targetUid, uid);
		}
	}

	/**
	 * 
	 * @Description: 根据uid查找与其的所有用户的uid值,@TODO此处可优,与{@link UserRelationServiceImpl#findUserByUidAndAndRelations}
	 * @param 
	 * @return List<Integer>
	 * @author liangzd
	 * @date 2018年6月20日 下午2:35:33
	 */
	@Override
	public List<Integer> findByUid(Integer uid) {
		List<TbUserRelation> firstUidList = userRelationDao.findByFirstUidAndFirstUserRelationNot(uid,ConstantParams.USER_RELATION_UNKNOW);
		List<TbUserRelation> secondUidList = userRelationDao.findBySecondUidAndSecondUserRelationNot(uid,ConstantParams.USER_RELATION_UNKNOW);
		List<Integer> uids = new ArrayList<Integer>();
		for(TbUserRelation tbUserRelation : firstUidList) {
			uids.add(tbUserRelation.getSecondUid());
		}
		for(TbUserRelation tbUserRelation : secondUidList) {
			uids.add(tbUserRelation.getFirstUid());
		}
		return uids;
	}

	/**
	 * 
	 * @Description: 保存用户间的关系,受事务管理
	 * @param 
	 * @return void
	 * @author liangzd
	 * @date 2018年6月20日 下午2:36:05
	 */
	@Override
	@Transactional
	public void saveUserRelation(TbUserRelation tbUserRelation) {
		TbUserRelation updateUserRelation = new TbUserRelation();
		//查找有误对应用户关系
		Optional<TbUserRelation> userRelation = findByUidAndTargetUid(tbUserRelation.getFirstUid(), tbUserRelation.getSecondUid());
		if(!userRelation.isPresent()) {
			//调换用户1与用户2的位置,再次查询有有用户关系信息
			userRelation = findByUidAndTargetUid(tbUserRelation.getSecondUid(),tbUserRelation.getFirstUid());
			if(!userRelation.isPresent()) {
				//判断用户2的关系为空则置为2,代表未选择，
				tbUserRelation.setSecondUserRelation(
						StringUtils.isEmpty(tbUserRelation.getSecondUserRelation()) ? 
								"2" : tbUserRelation.getSecondUserRelation());
				updateUserRelation = tbUserRelation;
			}else {
				updateUserRelation = userRelation.get();
				//将传入的用户1关系配置到对应的系统已保存的用户2位置关系中
				updateUserRelation.setSecondUserRelation(tbUserRelation.getFirstUserRelation());
			}
		}else {
			updateUserRelation = tbUserRelation;
			//查看已存在的关系，如果用户2的关系为空则置为2,代表未选择
			updateUserRelation.setSecondUserRelation(
					StringUtils.isEmpty(updateUserRelation.getSecondUserRelation()) ? 
							"2" : updateUserRelation.getSecondUserRelation());
		}
		updateUserRelation.setCreateTime(tbUserRelation.getCreateTime());
		//保存用户关系，系统中两个用户对应只能拥有一条数据，超过则为脏数据
		userRelationDao.save(updateUserRelation);
	}

	/**
	 * 
	 * @Description: 根据用户1的uid值与用户2的关系类型查找系统匹配的用户uid值
	 * @param 
	 * @return List<Integer>
	 * @author liangzd
	 * @date 2018年6月20日 下午2:36:24
	 */
	@Override
	public List<Integer> findUserByUidAndAndRelations(Integer uid, String firstRelation, String secondRelation) {
		List<TbUserRelation> firstUidList = userRelationDao.findByFirstUidAndFirstUserRelationAndSecondUserRelation(uid,
				firstRelation,secondRelation);
		List<TbUserRelation> secondUidList = userRelationDao.findBySecondUidAndFirstUserRelationAndSecondUserRelation(uid,
				firstRelation,secondRelation);
		List<Integer> uids = new ArrayList<Integer>();
		for(TbUserRelation tbUserRelation : firstUidList) {
			uids.add(tbUserRelation.getSecondUid());
		}
		for(TbUserRelation tbUserRelation : secondUidList) {
			uids.add(tbUserRelation.getFirstUid());
		}
		return uids;
	}
}
