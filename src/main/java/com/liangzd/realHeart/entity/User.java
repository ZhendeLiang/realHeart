package com.liangzd.realHeart.entity;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Transient;

import com.alibaba.fastjson.annotation.JSONField;

import javax.persistence.JoinColumn;

/**
 * 
 * @Description: tb_user表的持久化类,用户表
 * @author liangzd
 * @date 2018年6月16日 下午9:32:14
 */
@Entity(name="tb_user")
public class User implements Serializable,Comparable<User>{
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue
	private Integer uid;//主键
	@Column(unique = true,length = 11)
	private String username;//用户名,长度11
	@Column(length = 25, unique = true)
	private String nickname;//用户昵称,长度25
	@Column(length = 128)
	private String password;//用户密码,经过MD5加密,长度125
	@Column(length = 1)
	private String gender;//用户性别,长度1
	@Column(length = 11, unique = true)
	private String phoneNumber;//用户手机号,唯一值,长度11
	@Column(length = 18, unique = true)
	private String idCard;//用户身份证号,唯一值,长度18
	@Column(length = 320, unique = true)
	private String email;//用户邮箱,唯一值,长度320
	@Column(length = 1000)
	private String selfIntroduction;//用户自我介绍,长度1000
	private byte state;//用户状态,0:创建未认证（比如没有激活，没有输入验证码等等）--等待验证的用户 , 1:正常状态,2：用户被锁定.
	@Column(columnDefinition="timestamp")
	@JSONField(format="yyyy-MM-dd")
	private Timestamp createTime;//创建日期,格式化JSON数据日期类型
	@ManyToMany(fetch= FetchType.EAGER)//立即从数据库中进行加载数据;
	@JoinTable(name = "trUserRole", joinColumns = { @JoinColumn(name = "uid") }, inverseJoinColumns ={@JoinColumn(name = "roleId") })
	private List<Role> roleList;// 一个用户具有多个角色
	@Transient
	private String viprankName;//非持久化属性,用户的会员等级名称/会员等级ID
	@Transient
	private String addressId;//非持久化属性,用户的详地址id
	@Transient
	private String headImgPath;  //非持久化属性,头像存储路径
	@Transient
	private String lastUserChat;//非持久化属性,用户的最后一条聊天记录
	@JSONField(format="MM-dd HH:mm")//对应JSON数据格式化类型
	@Transient
	private Timestamp lastUserChatTime;//非持久化对象,最后聊天时间
	public Integer getUid() {
		return uid;
	}
	public void setUid(Integer uid) {
		this.uid = uid;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getNickname() {
		return nickname;
	}
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public String getPhoneNumber() {
		return phoneNumber;
	}
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	public String getIdCard() {
		return idCard;
	}
	public void setIdCard(String idCard) {
		this.idCard = idCard;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getSelfIntroduction() {
		return selfIntroduction;
	}
	public void setSelfIntroduction(String selfIntroduction) {
		this.selfIntroduction = selfIntroduction;
	}
	public byte getState() {
		return state;
	}
	public void setState(byte state) {
		this.state = state;
	}
	public Timestamp getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}
	public List<Role> getRoleList() {
		return roleList;
	}
	public void setRoleList(List<Role> roleList) {
		this.roleList = roleList;
	}
	public String getViprankName() {
		return viprankName;
	}
	public void setViprankName(String viprankName) {
		this.viprankName = viprankName;
	}
	public String getAddressId() {
		return addressId;
	}
	public void setAddressId(String addressId) {
		this.addressId = addressId;
	}
	public String getHeadImgPath() {
		return headImgPath;
	}
	public void setHeadImgPath(String headImgPath) {
		this.headImgPath = headImgPath;
	}
	public String getLastUserChat() {
		return lastUserChat;
	}
	public void setLastUserChat(String lastUserChat) {
		this.lastUserChat = lastUserChat;
	}
	public Timestamp getLastUserChatTime() {
		return lastUserChatTime;
	}
	public void setLastUserChatTime(Timestamp lastUserChatTime) {
		this.lastUserChatTime = lastUserChatTime;
	}
	@Override
	public String toString() {
		return "User [uid=" + uid + ", username=" + username + ", nickname=" + nickname + ", password=" + password
				+ ", gender=" + gender + ", phoneNumber=" + phoneNumber + ", idCard=" + idCard + ", email=" + email
				+ ", selfIntroduction=" + selfIntroduction + ", state=" + state + ", createTime=" + createTime
				+ ", roleList=" + roleList + ", viprankName=" + viprankName + ", addressId=" + addressId
				+ ", headImgPath=" + headImgPath + ", lastUserChat=" + lastUserChat + ", lastUserChatTime="
				+ lastUserChatTime + "]";
	}
	@Override
	public int compareTo(User o) {
		if(this.getLastUserChatTime().before(o.getLastUserChatTime())) {
			return 1;
		}else if(this.getLastUserChatTime().equals(o.getLastUserChatTime())){
			return this.getUid() - o.getUid();
		}else {
			return -1;
		}
	}
}
