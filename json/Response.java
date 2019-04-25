package org.hangzhou.tool.autoToJson;

public class Response {
	private String status;
	private String failReason;
	private int currentIndex;
 
	public String getStatus() {
		return status;
	}
 
	public void setStatus(String status) {
		this.status = status;
	}
 
	public String getFailReason() {
		return failReason;
	}
 
	public void setFailReason(String failReason) {
		this.failReason = failReason;
	}
 
	public int getCurrentIndex() {
		return currentIndex;
	}
 
	public void setCurrentIndex(int currentIndex) {
		this.currentIndex = currentIndex;
	}
 
	public Response(String status, String failReason, int currentIndex) {
		super();
		this.status = status;
		this.failReason = failReason;
		this.currentIndex = currentIndex;
	}
 
}
