package com.steenklingberg.json;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class JsonParserTest {
    @Test
    public void parseJson () {
        String raw = "{\"a/1\":{\"b\":123,\"c\":[true,false,true]}}";
        JsonParser parser = new JsonParser();
        JsonValue json = parser.parse(raw);
        System.out.println(json.toString());
        assertEquals(raw, json.toString());
    }

    @Test
    public void parseJsonWithWhitespaces () {
        String raw = "{\"a/1\":{\"b\":123, \"c\": [true,false,true] } }";
        String expected = "{\"a/1\":{\"b\":123,\"c\":[true,false,true]}}";
        JsonParser parser = new JsonParser();
        JsonValue json = parser.parse(raw);
        assertEquals(expected, json.toString());
    }

    @Test
    public void jsonPointer () {
        JsonParser parser = new JsonParser();
        JsonValue json = parser.parse("{\"a/1\":{\"b\": 123,\"c\":[true,false,true]}}");
        JsonPointer jp = new JsonPointer(json);
        String path = "/a~11/c/2";
        JsonValue val = jp.point(path);
        assertEquals("true", val.getStringValue());
    }

}