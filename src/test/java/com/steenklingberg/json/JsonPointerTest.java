package com.steenklingberg.json;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class JsonPointerTest {

    @Test
    public void createOrUpdateTest () {
        JsonPointer p = new JsonPointer();
        String expected = "{\"a\":{\"b\":{\"c\":\"d\"}}}";
        p.createOrUpdate("/a/b/c", new JsonString("d"));
        Assertions.assertEquals(expected, p.getRoot().toString());
    }
}
