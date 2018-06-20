package com.liangzd.realHeart.VO;

import java.util.List;

import com.liangzd.realHeart.entity.TbUserChatRecord;

/**
 * 
 * @Description: 用户聊天比较的前后端数据交互实体类
 * @author liangzd
 * @date 2018年6月20日 下午4:15:13
 */
public class UserComparableVo implements Comparable<UserComparableVo>{
	private Integer uid;//用户uid
	private List<TbUserChatRecord> userChatRecords;//用户的聊天记录
	private Integer newsCount;//用户新增的聊天记录数
	public Integer getUid() {
		return uid;
	}
	public void setUid(Integer uid) {
		this.uid = uid;
	}
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
	public int compareTo(UserComparableVo o) {
		Integer lastChatId1 = 0;
		Integer lastChatId2 = 0;
		if(this.getUserChatRecords().size() > 0) {
			lastChatId1 = this.getUserChatRecords().get(this.getUserChatRecords().size() - 1).getId();
		}
		if(o.getUserChatRecords().size() > 0) {
			lastChatId2 = o.getUserChatRecords().get(o.getUserChatRecords().size() - 1).getId();
		}
		if(lastChatId2 == lastChatId1) {
			return o.getNewsCount() - this.getNewsCount();
		}else {
			return lastChatId2 - lastChatId1;
		}
	}
	@Override
	public String toString() {
		return "UserComparableVo [uid=" + uid + ", userChatRecords=" + userChatRecords + ", newsCount=" + newsCount
				+ "]";
	}
}
