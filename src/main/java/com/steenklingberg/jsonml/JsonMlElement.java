package com.steenklingberg.jsonml;

import com.steenklingberg.json.*;

public class JsonMlElement {

    private final String name;
    private final JsonObject attributes;
    private final JsonArray content;

    public JsonMlElement (String name) {
        this.name = name;
        this.attributes = new JsonObject();
        this.content = new JsonArray();
    }

    public final String getName() {
        return this.name;
    }

    private JsonObject getAttributes() {
        return this.attributes;
    }

    private JsonArray getContent() {
        return this.content;
    }

    private JsonArray toJsonArray(JsonMlElement elem) {
        JsonArray root = new JsonArray();
        root.add(new JsonString(elem.getName()));
        if ( elem.getAttributes().size() > 0) {
            root.add(elem.getAttributes());
        }
        if (elem.getContent().size() > 0) {
            root.addAll(elem.getContent());
        }
        return root;
    }

    public JsonArray toJsonArray() {
        JsonArray root = new JsonArray();
        root.add(new JsonString(this.name));
        if (this.attributes.size() > 0) {
            root.add(this.attributes);
        }
        if (this.content.size() > 0) {
            root.addAll(this.content);
        }
        return root;
    }

    public void setAttribute(String key, String value) {
        this.attributes.setValue(key, value);
    }

    public String getAttribute(String key) {
        return this.attributes.getValue(key).getStringValue();
    }

    public void addContent(String text) {
        this.content.add(new JsonString(text));
    }

    public void addContent(int number) {
        this.content.add(new JsonNumber(number));
    }

    public void addContent(float number) {
        this.content.add(new JsonNumber(number));
    }

    public void addContent(boolean bool) {
        if (bool) {
            this.content.add(new JsonTrue());
        } else {
            this.content.add(new JsonFalse());
        }
    }

    public void addNullContent() {
        this.content.add(new JsonNull());
    }

    public void addContent(JsonMlElement elem) {
        this.content.add(toJsonArray(elem));
    }

    public String toXml() {
        return JsonMl.toXml(toJsonArray());
    }

}
