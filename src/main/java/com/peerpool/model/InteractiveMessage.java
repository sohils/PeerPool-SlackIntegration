package com.peerpool.model;

import java.util.List;

public class InteractiveMessage {
	private String text;
	private String thread_ts;
	private String response_type;
	private boolean replace_original;
	private boolean delete_original;
	private List<InteractiveAttachment> attachments;
	
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public String getThread_ts() {
		return thread_ts;
	}
	public void setThread_ts(String thread_ts) {
		this.thread_ts = thread_ts;
	}
	public String getResponse_type() {
		return response_type;
	}
	public void setResponse_type(String response_type) {
		this.response_type = response_type;
	}
	public boolean isReplace_original() {
		return replace_original;
	}
	public void setReplace_original(boolean replace_original) {
		this.replace_original = replace_original;
	}
	public boolean isDelete_original() {
		return delete_original;
	}
	public void setDelete_original(boolean delete_original) {
		this.delete_original = delete_original;
	}
	public List<InteractiveAttachment> getAttachments() {
		return attachments;
	}
	public void setAttachments(List<InteractiveAttachment> attachments) {
		this.attachments = attachments;
	}

}
