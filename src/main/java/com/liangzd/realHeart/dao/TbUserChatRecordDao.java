package com.liangzd.realHeart.dao;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.liangzd.realHeart.entity.TbUserChatRecord;

/**
 * 
 * @Description: 用户聊天记录对象的持久化操作类,tb_user_chat_recode
 * @author liangzd
 * @date 2018年6月16日 下午8:40:24
 */
@Repository
public interface TbUserChatRecordDao extends JpaRepository<TbUserChatRecord, Integer>{
	/**
	 * 
	 * @Description: 根据FromUid和ToUid查找用户与用户间的聊天记录对象
	 * @param 
	 * @return List<TbUserChatRecord>
	 * @author liangzd
	 * @date 2018年6月16日 下午8:30:23
	 */
	public List<TbUserChatRecord> findByFromUidAndToUid(Integer fromUid, Integer toUid);
	/**
	 * 
	 * @Description: 根据FromUid和ToUid(但已经调换位置)查找用户与用户间的聊天记录对象
	 * @param 
	 * @return List<TbUserChatRecord>
	 * @author liangzd
	 * @date 2018年6月16日 下午8:31:04
	 */
	public List<TbUserChatRecord> findByToUidAndFromUid(Integer fromUid, Integer toUid);
	/**
	 * 
	 * @Description: 根据FromUid和ToUid查找用户与用户间的聊天记录对象,根据Sort对象进行排序
	 * @param 
	 * @return List<TbUserChatRecord>
	 * @author liangzd
	 * @date 2018年6月16日 下午8:32:07
	 */
	public List<TbUserChatRecord> findByFromUidAndToUid(Integer fromUid, Integer toUid,Sort sort);
	/**
	 * 
	 * @Description: 根据FromUid和ToUid(但已经调换位置)查找用户与用户间的聊天记录对象,根据Sort对象进行排序
	 * @param 
	 * @return List<TbUserChatRecord>
	 * @author liangzd
	 * @date 2018年6月16日 下午8:32:15
	 */
	public List<TbUserChatRecord> findByToUidAndFromUid(Integer fromUid, Integer toUid,Sort sort);

	/**
	 * 
	 * @Description: 根据FromUid和ToUid查找用户与用户间且id>指定数的聊天记录对象,根据Sort对象进行排序
	 * @param 
	 * @return List<TbUserChatRecord>
	 * @author liangzd
	 * @date 2018年6月16日 下午8:32:07
	 */
	public List<TbUserChatRecord> findByFromUidAndToUidAndIdGreaterThan(Integer fromUid, Integer toUid,Integer id, Sort sort);
	/**
	 * 
	 * @Description: 根据FromUid和ToUid(但已经调换位置)查找用户与用户间且id>指定数的聊天记录对象,根据Sort对象进行排序
	 * @param 
	 * @return List<TbUserChatRecord>
	 * @author liangzd
	 * @date 2018年6月16日 下午8:32:15
	 */
	public List<TbUserChatRecord> findByToUidAndFromUidAndIdGreaterThan(Integer fromUid, Integer toUid,Integer id, Sort sort);
}
