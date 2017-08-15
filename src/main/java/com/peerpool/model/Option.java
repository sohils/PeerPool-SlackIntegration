package com.peerpool.model;

public class Option {
	private String text;
	private String value;
	
	public Option(String name, String value){
		this.text=name;
		this.value=value;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getText() {
		return text;
	}

	public void setText(String name) {
		this.text = name;
	}
}
