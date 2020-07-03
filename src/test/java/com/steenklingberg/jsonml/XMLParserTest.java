package com.steenklingberg.jsonml;

import org.junit.jupiter.api.Test;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;

import static org.junit.jupiter.api.Assertions.assertEquals;

class XMLParserTest {

    @Test
    public void parseXmlAndJsonString () {
        try {
            String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><a a1=\"100\"><b>Alice</b><b>Martin</b><b/><c/></a>";
            String expected = "{\"a\":{\"@a1\":100,\"b\":[\"Alice\",\"Martin\",null],\"c\":null}}";
            InputStream stream = new ByteArrayInputStream(xml.getBytes(StandardCharsets.UTF_8));
            JsonMlHandler handler = XMLParser.parse(stream);

            String jsonmlStr = handler.getJsonMlString();
            JsonMl jsonml = new JsonMl((jsonmlStr));
            String json = jsonml.getJsonString();
            assertEquals(expected, json);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void parseXmlWithWhitespaces () {
        String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?> <a a1=\"100\"><b>Alice</b> <b>Martin\n</b> <b/>\t</a>";
        String expected = "[\"a\",{\"a1\":\"100\"},[\"b\",\"Alice\"],[\"b\",\"Martin\"],[\"b\"]]";
        InputStream stream = new ByteArrayInputStream(xml.getBytes(StandardCharsets.UTF_8));
        try {
            JsonMlHandler handler = XMLParser.parse(stream);
            String jsonmlStr = handler.getJsonMlString();
            assertEquals(expected, jsonmlStr);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void parseXmlAndPathFind () {
        String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><a><b><c>1</c><c>3</c></b><b lang=\"en\"><c>2</c><c>4</c></b></a>";
        InputStream stream = new ByteArrayInputStream(xml.getBytes(StandardCharsets.UTF_8));
        try {
            JsonMlHandler handler = XMLParser.parse(stream);
            String jsonmlStr = handler.getJsonMlString();
            SortedMap<String, String> set = handler.getSortedMap("a[0].b");
            Map.Entry<String,String> entry = null;
            for (Iterator<Map.Entry<String, String>> it = set.entrySet().iterator(); it.hasNext(); ) {
                entry = it.next();
                System.out.println(entry.getKey() + " => " + entry.getValue());
            }
            System.out.println(set.get("a[0].b[1].c.name()"));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}