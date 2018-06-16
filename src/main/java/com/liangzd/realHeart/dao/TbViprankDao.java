package com.liangzd.realHeart.dao;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.liangzd.realHeart.entity.TbViprank;

/**
 * 
 * @Description: 会员详细等级对象的持久化操作类,tb_viprank
 * @author liangzd
 * @date 2018年6月16日 下午8:39:13
 */
@Repository
public interface TbViprankDao extends JpaRepository<TbViprank, Integer>{
}
