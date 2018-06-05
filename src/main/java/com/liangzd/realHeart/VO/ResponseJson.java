package com.liangzd.realHeart.VO;

public class ResponseJson {
	private Integer code;
	private Object msg;
	private Boolean usingVerifyCode;
	private String pageNum;
	private String pageSize;
	private String totalCount;
	private String currentPageCount;

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
}
