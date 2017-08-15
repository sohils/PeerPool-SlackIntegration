package com.peerpool.model;

import java.util.List;

public class InteractiveAction {
	private String name;
	private String text;
	private String type;
	private String value;
	private List<Option> selected_options;
	private List<Option> options;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public List<Option> getSelected_options() {
		return selected_options;
	}
	public void setSelected_options(List<Option> selected_options) {
		this.selected_options = selected_options;
	}
	public List<Option> getOptions() {
		return options;
	}
	public void setOptions(List<Option> options) {
		this.options = options;
	}
	
}
