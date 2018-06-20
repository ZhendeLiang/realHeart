package com.liangzd.realHeart.VO;

/**
 * 
 * @Description:  用户向前端返回指定格式的JSON数据
 * @author liangzd
 * @date 2018年6月20日 下午3:57:54
 */
public class ResponseJson {
	private Integer code;//返回值,一般0为正确
	private Object msg;//放回消息,如果需要数据的时候,可将数据放置在这里
	private Boolean usingVerifyCode;//是否正在使用验证码值
	private String pageNum;//页数
	private String pageSize;//页面数据大小
	private String totalCount;//数据总数
	private String currentPageCount;//当前页面数
	private String token;//返回AndroidClient的token值

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
	public String getTotalCount() {
		return totalCount;
	}
	public void setTotalCount(String totalCount) {
		this.totalCount = totalCount;
	}
	public String getPageNum() {
		return pageNum;
	}
	public void setPageNum(String pageNum) {
		this.pageNum = pageNum;
	}
	public String getPageSize() {
		return pageSize;
	}
	public void setPageSize(String pageSize) {
		this.pageSize = pageSize;
	}
	public String getCurrentPageCount() {
		return currentPageCount;
	}
	public void setCurrentPageCount(String currentPageCount) {
		this.currentPageCount = currentPageCount;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
}
