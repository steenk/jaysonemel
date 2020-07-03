package com.steenklingberg.jsonml;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class JsonMlTest {
    @Test
    public void jsonMlElementTest () {
        String expecting = "[\"root\",{\"date\":\"2020-07-01\"},\"testing\",[\"sub\",123]]";
        JsonMlElement elem = new JsonMlElement("root");
        elem.setAttribute("date", "2020-07-01");
        elem.addContent("testing");
        JsonMlElement sub = new JsonMlElement("sub");
        sub.addContent(123);
        elem.addContent(sub);
        sub.addContent(true);
        assertEquals(expecting, elem.toJsonArray().toString());
    }
}
