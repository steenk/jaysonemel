package com.steenklingberg.json;

import java.util.ArrayList;
import java.util.ListIterator;
import java.util.Map;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Arrays;
import java.util.Stack;
import java.util.regex.Pattern;

public class JsonParser {

	Map<String, String> map = new HashMap<String, String>() {{
        put("[", "start_list");
        put("]", "end_list");
        put("\"", "string_delimiter");
        put(",", "item_delimiter");
        put(" ", "whitespace");
        put("\t", "whitespace");
        put("\r", "whitespace");
        put("\n", "whitespace");
        put(":", "pair_delimiter");
        put("{", "start_object");
        put("}", "end_object");
    }};
	static Pattern number = Pattern.compile("^-?\\d+\\.?\\d*([eE][\\+-]?\\d+)?");

	public JsonParser () { }

	private ArrayList<Token> tokenize (String json) {
		String[] list = json.split("");
		ArrayList<Token> array = new ArrayList<Token>();
		Iterator<String> iter = Arrays.stream(list).iterator();
		String s = null;
		String p = null;
		boolean intext = false;
		StringBuilder sb = new StringBuilder();
		StringBuilder ws = new StringBuilder();
		while (iter.hasNext()) {
			s = iter.next();
			String item = null;
			if (!"\\".equals(p)) {
				item = map.get(s);
			}
			if ("string_delimiter".equals(item)) {
				intext = !intext;
			} else
			if ("pair_delimiter".equals(item) && intext) {
				item = null;
			} else
			if ("whitespace".equals(item)) {
				if (sb.length() == 0 || ws.length() > 0) {
					ws.append(s);
				}
				item = null;
			}
			if (item != null) {
				if (sb.length() > 0) {
					array.add(new Token("text", sb.toString()));
					sb = new StringBuilder();
				}
				Token token = new Token(item, null);
				array.add(token);
				if (ws.length() > 0) {
					array.add(new Token("whitespace", ws.toString()));
					ws = new StringBuilder();
				}
			} else {
				sb.append(s);
			}
			
			p = s;
		}
		return array;
	}

	private JsonValue build (ArrayList<Token> tokens) {
		JsonValue json = null;
		boolean isString = false;
		boolean isValue = false;
		String key = null;
		Stack<JsonValue> stack = new Stack<JsonValue>();
		ListIterator<Token> iter = tokens.listIterator();
		Token token;
		while (iter.hasNext()) {
			token = iter.next();
			switch (token.getType()) {
				case "start_list": JsonArray arr = new JsonArray();
					if (!stack.empty() && "array".equals(stack.peek().getType())) {
						((JsonArray)stack.peek()).add(arr);
					}
					if (!stack.empty() && isValue && "object".equals(stack.peek().getType())) {
						((JsonObject)stack.peek()).put(key, arr);
						isValue = false;
						key = null;
					}
					stack.push(arr);
					if (json == null) {
						json = stack.peek();
					}
					break;
				case "end_list": stack.pop();
					break;
				case "start_object": JsonObject obj = new JsonObject();
					if (!stack.empty() && "array".equals(stack.peek().getType())) {
						((JsonArray)stack.peek()).add(obj);
					}
					if (!stack.empty() && isValue && "object".equals(stack.peek().getType())) {
						((JsonObject)stack.peek()).put(key, obj);
						isValue = false;
						key = null;
					}
					stack.push(obj);
					if (json == null) {
						json = stack.peek();
					}
					break;
				case "end_object": stack.pop();
					break;
				case "string_delimiter": isString = !isString;
					break;
				case "whitespace": if (!isString) {
						//System.out.println("WHITESPACE");
					}
					break;
				case "pair_delimiter": isValue = true;
					break;
				case "text": if ("array".equals(stack.peek().getType())) {
						if (number.matcher(token.getValue()).matches()) {
							((JsonArray)stack.peek()).add(new JsonNumber(token.getValue()));
						} else if (token.getValue().equals("true")) {
							((JsonArray)stack.peek()).add(new JsonTrue());
						} else if (token.getValue().equals("false")) {
							((JsonArray)stack.peek()).add(new JsonFalse());
						} else if (token.getValue().equals("null")) {
							((JsonArray)stack.peek()).add(new JsonNull());
						} else if (token.getValue().trim().length() > 0) {
							((JsonArray)stack.peek()).add(new JsonString(token.getValue()));
						}
					} else if ("object".equals(stack.peek().getType())) {
						if (!isValue) {
							key = token.getValue();
						} else {
							if (number.matcher(token.getValue()).matches()) {
								((JsonObject)stack.peek()).put(key, new JsonNumber(token.getValue()));
							} else if (token.getValue().equals("true")) {
								((JsonObject)stack.peek()).put(key, new JsonTrue());
							} else if (token.getValue().equals("false")) {
								((JsonObject)stack.peek()).put(key, new JsonFalse());
							} else if (token.getValue().equals("null")) {
								((JsonObject)stack.peek()).put(key, new JsonNull());
							} else if (token.getValue().trim().length() > 0) {
								((JsonObject)stack.peek()).put(key, new JsonString(token.getValue()));
							}
							isValue = false;
							key = null;
						}
					}
					break;
			}
		}
		return json;
	}

	public JsonValue parse(String json) {
		ArrayList<Token> tokens = tokenize(json);
		JsonValue jv = build(tokens);
		return jv;
	}
}




















