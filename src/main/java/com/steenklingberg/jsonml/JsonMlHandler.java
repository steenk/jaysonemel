package com.steenklingberg.jsonml;

import java.util.*;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.regex.Pattern;

public class JsonMlHandler extends DefaultHandler {

	StringBuilder sb = new StringBuilder();
	Stack<String> stack = new Stack<String>();
	TreeMap<String, String> hash = new TreeMap<String, String>();
	static Pattern number = Pattern.compile("^-?[1-9]\\d*\\.?\\d*([eE][+-]?\\d+)?$");

	@Override
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
		String key = null;
		int size = 1;
		if (sb.length() > 0) {
			sb.append(",");
			key = stack.peek() + "." + qName;
		} else {
			key = qName;
		}
		if (hash.get(key + ".size()") != null) {
			size = Integer.parseInt(hash.get(key + ".size()")) + 1;
		}
		hash.put(key + ".size()", Integer.toString(size));
		hash.put(key + ".name()", qName);
		//stack.push(key);
		stack.push(key + "[" + (size - 1) + "]");
		sb.append("[\"");
		sb.append(qName);
		sb.append("\"");
		if (attributes.getLength() > 0) {
			sb.append(",{");
			for (int i=0; i<attributes.getLength(); i++) {
				sb.append("\"");
				sb.append(attributes.getQName(i));
				sb.append("\":\"");
				sb.append(attributes.getValue(i));	
				sb.append("\"");
				if (attributes.getLength() > i + 1) {
					sb.append(",");
				}
				//hash.put(stack.peek() + "[" + (size - 1) + "].@" + attributes.getQName(i), attributes.getValue(i));
				hash.put(stack.peek() + ".@" + attributes.getQName(i), attributes.getValue(i));
			}
			sb.append("}");
		}
	}

	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException {
		sb.append("]");
		stack.pop();
	}

	@Override
	public void characters(char ch[], int start, int length) throws SAXException {
		String str = new String(ch, start, length).trim();
		if (str.length() > 0) {
			if (number.matcher(str).matches() || str.equals("null") || str.equals("false") || str.equals("true")) {
				sb.append(",")
						.append(str.trim());
			} else {
				sb.append(",\"");
				sb.append(str.trim().replace("\"","\\\""));
				sb.append("\"");
			}
			String key = stack.peek();
			//key = key + "[" + (Integer.parseInt(hash.get(key + ".size()")) - 1) + "]";
			String content = hash.get(key);
			if (content != null) {
				hash.put(key, content + str.trim()); // space in between?
			} else {
				hash.put(key, str.trim());
			}
		}
	}

	public String getJsonMlString () {
		return sb.toString();
	}

	public Set<String> getKeySet() {
		//Set<String> set = hash.keySet();
		return hash.keySet();
	}

	public SortedMap<String, String> getSortedMap(String key) {
		//return hash.subMap(key, key.replaceFirst(".$", "^"));
		return hash.subMap(key, key + "~");
	}

	public String getPath (String path) {
		if (path.contains("@") || path.endsWith("]") || path.endsWith(".size()") || path.endsWith(".name()")) {
			return hash.get(path);
		} else {
			return hash.get(path + "[0]");
		}
	} 

}

