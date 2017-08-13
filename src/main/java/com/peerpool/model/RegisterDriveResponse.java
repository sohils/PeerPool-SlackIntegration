package com.peerpool.model;

import java.util.List;

public class RegisterDriveResponse {
	private String text;
	private List<Attachments> attachments;
	
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public List<Attachments> getAttachments() {
		return attachments;
	}
	public void setAttachments(List<Attachments> attachments) {
		this.attachments = attachments;
	}
}
