package com.liangzd.realHeart.VO;

/**
 * 
 * @Description: 模拟调用短信验证码接口的返回值，用于测试
 * @author liangzd
 * @date 2018年6月20日 下午4:01:47
 */
public class TestPhone {
	private Integer code;//返回值200,为正确
	private String msg;//具体消息
	private String obj;//验证码值
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
	@Override
	public String toString() {
		return "TestPhone [code=" + code + ", msg=" + msg + ", obj=" + obj + "]";
	}
}
