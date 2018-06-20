package com.liangzd.realHeart.VO;

/**
 * 
 * @Description: 用户图片的前后端的数据交互的实体类
 * @author liangzd
 * @date 2018年6月20日 下午4:20:04
 */
public class UserImgVo {
	private Integer uid;//用户uid
	private String nickname;//用户昵称
	private String headImg;//用户头像
	public Integer getUid() {
		return uid;
	}
	public void setUid(Integer uid) {
		this.uid = uid;
	}
	public String getNickname() {
		return nickname;
	}
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	public String getHeadImg() {
		return headImg;
	}
	public void setHeadImg(String headImg) {
		this.headImg = headImg;
	}
	@Override
	public String toString() {
		return "UserImgVo [uid=" + uid + ", nickname=" + nickname + ", headImg=" + headImg + "]";
	}
}
