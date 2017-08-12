package com.peerpool.model;

import java.util.List;

public class InteractiveAttachment {
	private String text;
	private String fallback;
	private String callback_id;
	private String color;
	private String attachment_type;
	private List<InteractiveAction> actions;
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public String getFallback() {
		return fallback;
	}
	public void setFallback(String fallback) {
		this.fallback = fallback;
	}
	public String getCallback_id() {
		return callback_id;
	}
	public void setCallback_id(String callback_id) {
		this.callback_id = callback_id;
	}
	public String getColor() {
		return color;
	}
	public void setColor(String color) {
		this.color = color;
	}
	public String getAttachment_type() {
		return attachment_type;
	}
	public void setAttachment_type(String attachment_type) {
		this.attachment_type = attachment_type;
	}
	public List<InteractiveAction> getActions() {
		return actions;
	}
	public void setActions(List<InteractiveAction> actions) {
		this.actions = actions;
	}
}
