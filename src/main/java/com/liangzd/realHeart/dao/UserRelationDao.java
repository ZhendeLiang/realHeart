package com.liangzd.realHeart.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.liangzd.realHeart.entity.TbUserRelation;

@Repository
public interface UserRelationDao extends JpaRepository<TbUserRelation, Integer>{
	public List<TbUserRelation> findByFirstUid(Integer firstUid);
	public List<TbUserRelation> findBySecondUid(Integer secondUid);
	public Optional<TbUserRelation> findByFirstUidAndSecondUid(Integer firstUid, Integer secondUid);
}
