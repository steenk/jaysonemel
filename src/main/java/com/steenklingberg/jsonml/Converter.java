package com.steenklingberg.jsonml;

import com.steenklingberg.json.*;

import java.util.ArrayList;
import java.util.Iterator;

public class Converter {

    public static JsonObject convertToJsonObject (JsonArray jsonml) throws JsonConvertException {

        return null;
    }

    public static JsonArray convertToJsonMl (JsonObject json) throws JsonConvertException {
        int count = 0;
        for (String key : json.keySet()) {
            if (!key.startsWith("@")) {
                count++;
            }
        }
        if (count != 1) throw new JsonConvertException("Should have only one root element.");
        return convertElement(json);
    }

    private static JsonArray convertElement (JsonObject json) {
        JsonArray root = new JsonArray();
        JsonObject attrs = new JsonObject();
        JsonValue text = null;
        ArrayList<String> list = new ArrayList<>();
        for (String key : json.keySet()) {
            if (key.startsWith("@")) {
                attrs.setValue(key.substring(1), json.getValue(key));
            } else if ("#text".equals(key)) {
                text = json.getValue(key);
            } else if (json.getValue(key) instanceof JsonObject) {
                list.add(key);
            } else if (json.getValue(key) instanceof JsonArray) {

            } else {
                root.add(new JsonString(key));
            }
        }
        if (attrs.size() > 0) {
            root.add(attrs);
        }
        if (text != null) {
            root.add(text);
        }
        for (String key: list) {
            root.add(convertElement((JsonObject) json.getValue(key)));
        }
        return root;
    }

    public static void main (String[] args) {
        JsonObject obj = new JsonObject();
        obj.setValue("name", "Steen");
        //obj.setValue("@age", 59);
        //obj.setValue("#text", "Just testing");
        System.out.println(obj.toString());
        try {
            JsonArray arr = convertToJsonMl(obj);
            System.out.println(arr.toString());
        } catch (JsonConvertException e) {
            e.printStackTrace();
        }

    }

}
