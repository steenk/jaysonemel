package com.steenklingberg.json;

public class JsonNumber implements JsonValue {

	float number;
	boolean integer = false;

	public JsonNumber (float number) {
		this.number = number;
	}
	
	public JsonNumber (int number) {
		this.number = (float) number;
		this.integer = true;
	}

	public JsonNumber (String number) {
		this.number = Float.parseFloat(number);
		if (number.indexOf(".") == -1) {
			this.integer = true;
		}
	}

	public float getValue () {
		return number;
	}

	public float getFloatValue () {
		return number;
	}

	public void setValue (float number) {
		this.number = number;
	}

	public void setValue (int number) {
		this.number = (float) number;
		this.integer = true;
	}

	public void setValue (String number) {
		this.number = Float.parseFloat(number);
		if (number.indexOf(".") == -1) {
			this.integer = true;
		}
	}

	@Override
	public String getType () {
		return "number";
	}

	@Override
	public String getStringValue () {
		return toString();
	}

	public String toString () {
		if (integer) {
			return String.valueOf(Math.round(number));
		} else {
			return String.valueOf(number);
		}
	}	
}
