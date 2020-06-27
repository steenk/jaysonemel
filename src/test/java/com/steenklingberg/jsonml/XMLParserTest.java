package com.steenklingberg.jsonml;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

class XMLParserTest {
    @Test
    public void parseXml () {
        StringBuilder sb = new StringBuilder();
        sb.append("<person id=\"1000\">\n")
                .append("    <firstName>Robert</firstName>")
                .append("    <lastName>Smith</lastName>")
                .append("</person>");
    }

}