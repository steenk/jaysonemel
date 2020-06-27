package steenklingberg.jsonml;

import java.util.ArrayList;
import java.util.List;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import java.util.Stack;
import java.util.HashMap;

public class JsonMlHandler extends DefaultHandler {

	StringBuilder sb = new StringBuilder();
	Stack<String> stack = new Stack<String>();
	HashMap<String, String> hash = new HashMap<String, String>();

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
		stack.push(key);
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
				hash.put(stack.peek() + "[" + (size - 1) + "].@" + attributes.getQName(i), attributes.getValue(i));
				if (size == 1) {
					hash.put(stack.peek() + ".@" + attributes.getQName(i), attributes.getValue(i));
				}
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
			sb.append(",\"");
			sb.append(str.trim());
			sb.append("\"");
			String key = stack.peek();
			key = key + "[" + (Integer.parseInt(hash.get(key + ".size()")) - 1) + "]";
			String content = hash.get(key);
			if (content != null) {
				hash.put(key, content + " " + str.trim());
			} else {
				hash.put(key, str.trim());
			}
		}
	}

	public String getJsonString () {
		return sb.toString();
	}

	public String getPath (String path) {
		if (path.indexOf("@") > -1 || path.endsWith("]") || path.endsWith(".size()")) {
			return hash.get(path);
		} else {
			return hash.get(path + "[0]");
		}
	} 

}

