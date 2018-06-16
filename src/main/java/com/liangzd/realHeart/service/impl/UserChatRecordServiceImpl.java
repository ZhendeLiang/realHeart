package com.liangzd.realHeart.service.impl;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.liangzd.realHeart.dao.TbAddressDao;
import com.liangzd.realHeart.dao.TbNationalProvinceCityTownDao;
import com.liangzd.realHeart.dao.TbUserChatRecordDao;
import com.liangzd.realHeart.entity.TbAddress;
import com.liangzd.realHeart.entity.TbNationalProvinceCityTown;
import com.liangzd.realHeart.entity.TbUserChatRecord;
import com.liangzd.realHeart.service.AddressService;
import com.liangzd.realHeart.service.UserChatRecordService;

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
	
	
	@Override
	@Transactional
	public void saveUserChat(TbUserChatRecord userChatRecord) {
		userChatRecordDao.save(userChatRecord);
	}

	@Override
	public TbUserChatRecord findLatestByFromUidAndToUid(TbUserChatRecord userChatRecord) {
		TbUserChatRecord userChat = null;
		Sort sort = new Sort(Sort.Direction.DESC,"id"); //创建时间降序排序同ID
//		sort.and(new Sort(Sort.Direction.DESC,"id"));
		List<TbUserChatRecord> list1 = userChatRecordDao.findByFromUidAndToUid(userChatRecord.getFromUid(), userChatRecord.getToUid(),sort);
		List<TbUserChatRecord> list2 = userChatRecordDao.findByToUidAndFromUid(userChatRecord.getFromUid(), userChatRecord.getToUid(),sort);
		if(list1.size() == 0 && list2.size() == 0) {
			return userChat;
		}else if(list1.size() == 0 && list2.size() != 0) {
			userChat = list2.get(0);
		}else if(list2.size() == 0 && list1.size() != 0) {
			userChat = list1.get(0);
		}else{
			/*Date lastChatTime1 = list1.get(0).getLastChatTime();
			Date lastChatTime2 = list2.get(0).getLastChatTime();
			if(lastChatTime1.after(lastChatTime2)) {
				userChat = list1.get(0);
			}else {
				userChat = list2.get(0);
			}*/
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
}
