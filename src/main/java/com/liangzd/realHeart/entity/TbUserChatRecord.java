package com.liangzd.realHeart.entity;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Transient;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * 
 * @Description: tb_user_chat_recode表的持久化对象,用户聊天数据表
 * @author liangzd
 * @date 2018年6月16日 下午9:15:32
 */
@Entity(name="tb_user_chat_recode")
public class TbUserChatRecord implements Serializable,Comparable<TbUserChatRecord>{
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue
	private Integer id;//主键
	private Integer fromUid;//用户1
	private Integer toUid;//用户2
	private String chatRecode;//聊天记录
	private Integer type;//聊天类型 0未读 1已读
	@Column(columnDefinition="timestamp")
	@JSONField(format="MM-dd hh:mm")
	private Timestamp lastChatTime;//最后聊天时间,格式化JSON数据的样式
	@Transient
	private String toUserImgPath;//非持久化属性,用户2的头像地址
	@Transient
	private String fromUserImgPath;//非持久化属性,用户1的头像地址
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getFromUid() {
		return fromUid;
	}
	public void setFromUid(Integer fromUid) {
		this.fromUid = fromUid;
	}
	public Integer getToUid() {
		return toUid;
	}
	public void setToUid(Integer toUid) {
		this.toUid = toUid;
	}
	public String getChatRecode() {
		return chatRecode;
	}
	public void setChatRecode(String chatRecode) {
		this.chatRecode = chatRecode;
	}
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	public Timestamp getLastChatTime() {
		return lastChatTime;
	}
	public void setLastChatTime(Timestamp lastChatTime) {
		this.lastChatTime = lastChatTime;
	}
	public String getToUserImgPath() {
		return toUserImgPath;
	}
	public void setToUserImgPath(String toUserImgPath) {
		this.toUserImgPath = toUserImgPath;
	}
	public String getFromUserImgPath() {
		return fromUserImgPath;
	}
	public void setFromUserImgPath(String fromUserImgPath) {
		this.fromUserImgPath = fromUserImgPath;
	}
	@Override
	public int compareTo(TbUserChatRecord o) {
		if(this.getLastChatTime().after(o.getLastChatTime())) {
			return 1;
		}else if(this.getLastChatTime().equals(o.getLastChatTime())){
			return this.getId() - o.getId();
		}else{
			return -1;
		}
	}
	@Override
	public String toString() {
		return "TbUserChatRecord [id=" + id + ", fromUid=" + fromUid + ", toUid=" + toUid + ", chatRecode=" + chatRecode
				+ ", type=" + type + ", lastChatTime=" + lastChatTime + ", toUserImgPath=" + toUserImgPath
				+ ", fromUserImgPath=" + fromUserImgPath + "]";
	}
}
