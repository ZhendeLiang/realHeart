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

@Service
public class UserRelationServiceImpl implements UserRelationService{
	@Autowired
	private UserRelationDao userRelationDao; 
	
	@Override
	public Optional<TbUserRelation> findByUidAndTargetUid(Integer uid, Integer targetUid) {
		return userRelationDao.findByFirstUidAndSecondUid(uid, targetUid);
	}

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
		//保存用户关系，系统中两个用户对应只能拥有一条数据，超过则为脏数据
		userRelationDao.save(updateUserRelation);
	}

	@Override
	public List<Integer> findByUid(Integer uid) {
		List<TbUserRelation> firstUidList = userRelationDao.findByFirstUid(uid);
		List<TbUserRelation> secondUidList = userRelationDao.findBySecondUid(uid);
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
