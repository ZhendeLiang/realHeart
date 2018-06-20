package com.liangzd.realHeart.VO;

/**
 * 
 * @Description: 返回正确的消息数据,用于需要页面跳转
 * @author liangzd
 * @date 2018年6月20日 下午4:00:33
 */
public class SuccJson {
	private Integer setTime;//时间
	private String msg;//返回消息
	private String url;//返回准备跳转地址
	private boolean jump;//是否跳转
	public Integer getSetTime() {
		return setTime;
	}
	public void setSetTime(Integer setTime) {
		this.setTime = setTime;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public boolean isJump() {
		return jump;
	}
	public void setJump(boolean jump) {
		this.jump = jump;
	}
	public SuccJson(Integer setTime, String msg, String url, boolean jump) {
		super();
		this.setTime = setTime;
		this.msg = msg;
		this.url = url;
		this.jump = jump;
	}
	public SuccJson() {
		super();
	}
	@Override
	public String toString() {
		return "SuccJson [setTime=" + setTime + ", msg=" + msg + ", url=" + url + ", jump=" + jump + "]";
	}
}
