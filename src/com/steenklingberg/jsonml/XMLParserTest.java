package com.steenklingberg.jsonml;

import com.steenklingberg.json.JsonParser;
import com.steenklingberg.json.JsonPointer;
import com.steenklingberg.json.JsonValue;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

class XMLParserTest {
    @Test
    public void parseJson () {
        String raw = "{\"a/1\":{\"b\":123,\"c\":[true,false,true]}}";
        JsonParser parser = new JsonParser();
        JsonValue json = parser.parse(raw);
        assertEquals(raw, json.toString());
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