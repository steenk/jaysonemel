package com.steenklingberg.jsonml;

import com.steenklingberg.json.*;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.*;


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

    }

}