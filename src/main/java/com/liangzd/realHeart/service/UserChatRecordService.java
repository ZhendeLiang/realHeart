package com.liangzd.realHeart.service;

import java.util.List;
import java.util.Optional;

import com.liangzd.realHeart.entity.TbAddress;
import com.liangzd.realHeart.entity.TbNationalProvinceCityTown;
import com.liangzd.realHeart.entity.TbUserChatRecord;

public interface UserChatRecordService {
	public List<TbUserChatRecord> findAllByFromUidAndToUid(TbUserChatRecord userChatRecord);
	public void saveUserChat(TbUserChatRecord userChatRecord);
	public TbUserChatRecord findLatestByFromUidAndToUid(TbUserChatRecord userChatRecord);
	
}
