package com.liangzd.realHeart.VO;

public class ResponseJson {
	private Integer code;
	private Object msg;
	private Boolean usingVerifyCode;

	public Integer getCode() {
		return code;
	}
	public void setCode(Integer code) {
		this.code = code;
	}
	public Object getMsg() {
		return msg;
	}
	public void setMsg(Object msg) {
		this.msg = msg;
	}
	public Boolean getUsingVerifyCode() {
		return usingVerifyCode;
	}
	public void setUsingVerifyCode(Boolean usingVerifyCode) {
		this.usingVerifyCode = usingVerifyCode;
	}
}
