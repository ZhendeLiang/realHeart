package com.liangzd.realHeart.service;

import java.util.List;
import java.util.Optional;

import com.liangzd.realHeart.entity.TbUserRelation;

public interface UserRelationService {
	public Optional<TbUserRelation> findByUidAndTargetUid(Integer uid, Integer targetUid);
	public List<Integer> findByUid(Integer uid);
	public void saveUserRelation(TbUserRelation tbUserRelation);
	public List<Integer> findUserByUidAndAndRelations(Integer uid, String firstRelation, String secondRelation);
}
