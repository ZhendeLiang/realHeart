package com.liangzd.realHeart.service;

import java.util.List;

import com.liangzd.realHeart.entity.TbUserChatRecord;

/**
 * 
 * @Description: 处理关于用户间聊天记录的业务处理接口
 * @author liangzd
 * @date 2018年6月20日 下午2:21:54
 */
public interface UserChatRecordService {
	
	/**
	 * 
	 * @Description: 根据用户1和用户2的uid查找他们间的聊天记录
	 * @param 
	 * @return List<TbUserChatRecord>
	 * @author liangzd
	 * @date 2018年6月20日 下午2:26:06
	 */
	public List<TbUserChatRecord> findAllByFromUidAndToUid(TbUserChatRecord userChatRecord);
	
	/**
	 * 
	 * @Description: 保存用户间的聊天记录
	 * @param 
	 * @return TbUserChatRecord
	 * @author liangzd
	 * @date 2018年6月20日 下午2:26:44
	 */
	public TbUserChatRecord saveUserChat(TbUserChatRecord userChatRecord);
	
	/**
	 * 
	 * @Description: 根据用户1和用户2的uid查找他们间的最后一条聊天记录
	 * @param 
	 * @return TbUserChatRecord
	 * @author liangzd
	 * @date 2018年6月20日 下午2:26:57
	 */
	public TbUserChatRecord findLatestByFromUidAndToUid(TbUserChatRecord userChatRecord);
	
	/**
	 * 
	 * @Description: 根据用户1和用户2的uid查找他们间指定时候往后的新聊天记录
	 * @param 
	 * @return TbUserChatRecord
	 * @author liangzd
	 * @date 2018年6月20日 下午2:26:57
	 */
	public List<TbUserChatRecord> findAllLatestChatByFromUidAndToUid(TbUserChatRecord userChatRecord);
	
}
