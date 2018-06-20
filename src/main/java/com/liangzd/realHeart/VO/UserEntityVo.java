package com.liangzd.realHeart.VO;

import java.util.List;

import com.liangzd.realHeart.entity.TbUserChatRecord;
import com.liangzd.realHeart.entity.User;

/**
 * 
 * @Description: 用户聊天记录的前后端的数据交互的实体类
 * @author liangzd
 * @date 2018年6月20日 下午4:23:32
 */
public class UserEntityVo extends User {

	private static final long serialVersionUID = 1L;
	private List<TbUserChatRecord> userChatRecords;//用户聊天记录
	private Integer newsCount;//新增用户信息

	public List<TbUserChatRecord> getUserChatRecords() {
		return userChatRecords;
	}

	public void setUserChatRecords(List<TbUserChatRecord> userChatRecords) {
		this.userChatRecords = userChatRecords;
	}

	public Integer getNewsCount() {
		return newsCount;
	}

	public void setNewsCount(Integer newsCount) {
		this.newsCount = newsCount;
	}

	@Override
	public String toString() {
		return "UserEntityVo [userChatRecords=" + userChatRecords + ", newsCount=" + newsCount + "]";
	}
}
