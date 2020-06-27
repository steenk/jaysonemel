package com.steenklingberg.json;


public class Token {
	String type;
	String value;

	public Token (String type, String value) {
		this.type = type;
		this.value = value;
	}

	public String getType () {
		return type;
	}

	public String getValue () {
		return value;
	}
}