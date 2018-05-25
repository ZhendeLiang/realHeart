package com.liangzd.realHeart.action;

public class SuccJson {
	private Integer setTime;
	private String msg;
	private String URL;
	private boolean jump;
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
	public String getURL() {
		return URL;
	}
	public void setURL(String uRL) {
		URL = uRL;
	}
	public boolean isJump() {
		return jump;
	}
	public void setJump(boolean jump) {
		this.jump = jump;
	}
	public SuccJson(Integer setTime, String msg, String uRL, boolean jump) {
		super();
		this.setTime = setTime;
		this.msg = msg;
		URL = uRL;
		this.jump = jump;
	}
	public SuccJson() {
		super();
	}
}
