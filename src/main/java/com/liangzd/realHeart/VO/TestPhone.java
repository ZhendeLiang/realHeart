package com.liangzd.realHeart.VO;

public class TestPhone {
	private Integer code;
	private String msg;
	private String obj;
	public Integer getCode() {
		return code;
	}
	public void setCode(Integer code) {
		this.code = code;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public String getObj() {
		return obj;
	}
	public void setObj(String obj) {
		this.obj = obj;
	}
	public TestPhone() {
		super();
	}
	public TestPhone(Integer code, String msg, String obj) {
		super();
		this.code = code;
		this.msg = msg;
		this.obj = obj;
	}
}
