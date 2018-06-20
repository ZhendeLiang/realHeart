package com.liangzd.realHeart.service.impl;

import java.util.Collections;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.liangzd.realHeart.dao.TbUserChatRecordDao;
import com.liangzd.realHeart.entity.TbUserChatRecord;
import com.liangzd.realHeart.service.UserChatRecordService;

/**
 * 
 * @Description: 处理关于用户间聊天记录的业务处理接口实现类
 * @author liangzd
 * @date 2018年6月20日 下午2:21:54
 */
@Service
public class UserChatRecordServiceImpl implements UserChatRecordService {
	@Autowired
	private TbUserChatRecordDao userChatRecordDao;


	public TbUserChatRecordDao getUserChatRecordDao() {
		return userChatRecordDao;
	}

	public void setUserChatRecordDao(TbUserChatRecordDao userChatRecordDao) {
		this.userChatRecordDao = userChatRecordDao;
	}
	
	/**
	 * 
	 * @Description: 根据用户1和用户2的uid查找他们间的聊天记录
	 * @param 
	 * @return List<TbUserChatRecord>
	 * @author liangzd
	 * @date 2018年6月20日 下午2:26:06
	 */
	@Override
	public List<TbUserChatRecord> findAllByFromUidAndToUid(TbUserChatRecord userChatRecord) {
		Sort sort = new Sort(Sort.Direction.DESC,"id"); //创建时间降序排序
//		sort.and(new Sort(Sort.Direction.DESC,"id"));
		List<TbUserChatRecord> list1 = userChatRecordDao.findByFromUidAndToUid(userChatRecord.getFromUid(), userChatRecord.getToUid(),sort);
		List<TbUserChatRecord> list2 = userChatRecordDao.findByToUidAndFromUid(userChatRecord.getFromUid(), userChatRecord.getToUid(),sort);
//		List<TbUserChatRecord> list1 = userChatRecordDao.findByFromUidAndToUid(userChatRecord.getFromUid(), userChatRecord.getToUid());
//		List<TbUserChatRecord> list2 = userChatRecordDao.findByToUidAndFromUid(userChatRecord.getFromUid(), userChatRecord.getToUid());
		list1.addAll(list2);
		Collections.sort(list1);
		return list1;
	}
	

	/**
	 * 
	 * @Description: 保存用户间的聊天记录
	 * @param 
	 * @return TbUserChatRecord
	 * @author liangzd
	 * @date 2018年6月20日 下午2:26:44
	 */
	@Override
	@Transactional
	public TbUserChatRecord saveUserChat(TbUserChatRecord userChatRecord) {
		return userChatRecordDao.save(userChatRecord);
	}

	/**
	 * 
	 * @Description: 根据用户1和用户2的uid查找他们间的最后一条聊天记录
	 * @param 
	 * @return TbUserChatRecord
	 * @author liangzd
	 * @date 2018年6月20日 下午2:26:57
	 */
	@Override
	public TbUserChatRecord findLatestByFromUidAndToUid(TbUserChatRecord userChatRecord) {
		TbUserChatRecord userChat = null;
		Sort sort = new Sort(Sort.Direction.DESC,"id"); //创建时间降序排序同ID
		List<TbUserChatRecord> list1 = userChatRecordDao.findByFromUidAndToUid(userChatRecord.getFromUid(), userChatRecord.getToUid(),sort);
		List<TbUserChatRecord> list2 = userChatRecordDao.findByToUidAndFromUid(userChatRecord.getFromUid(), userChatRecord.getToUid(),sort);
		if(list1.size() == 0 && list2.size() == 0) {
			return userChat;
		}else if(list1.size() == 0 && list2.size() != 0) {
			userChat = list2.get(0);
		}else if(list2.size() == 0 && list1.size() != 0) {
			userChat = list1.get(0);
		}else{
			int lastId1 = list1.get(0).getId();
			int lastId2 = list2.get(0).getId();
			if(lastId1 > lastId2) {
				userChat = list1.get(0);
			}else {
				userChat = list2.get(0);
			}
		}
		return userChat;
	}

	/**
	 * 
	 * @Description: 根据用户1和用户2的uid查找他们间指定时候往后的新聊天记录
	 * @param 
	 * @return TbUserChatRecord
	 * @author liangzd
	 * @date 2018年6月20日 下午2:26:57
	 */
	@Override
	public List<TbUserChatRecord> findAllLatestChatByFromUidAndToUid(TbUserChatRecord userChatRecord) {
		Sort sort = new Sort(Sort.Direction.DESC,"id"); //创建时间降序排序
		List<TbUserChatRecord> list1 = userChatRecordDao.findByFromUidAndToUidAndIdGreaterThan(
				userChatRecord.getFromUid(), userChatRecord.getToUid(), userChatRecord.getId(),sort);
		List<TbUserChatRecord> list2 = userChatRecordDao.findByToUidAndFromUidAndIdGreaterThan(
				userChatRecord.getFromUid(), userChatRecord.getToUid(), userChatRecord.getId(),sort);
		list1.addAll(list2);
		Collections.sort(list1);
		return list1;
	}
}
