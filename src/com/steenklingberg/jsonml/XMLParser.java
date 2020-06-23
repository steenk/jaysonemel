package com.steenklingberg.jsonml;

import java.io.File;
import java.io.InputStream;
import java.io.IOException;
import java.util.List;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.SAXException;

public class XMLParser {

    public static JsonMlHandler parse (String path) throws ParserConfigurationException, SAXException, IOException {
        SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
        SAXParser saxParser = saxParserFactory.newSAXParser();
        JsonMlHandler handler = new JsonMlHandler();
        saxParser.parse(path, handler);
        return handler;
    }

    public static JsonMlHandler parse (InputStream stream) throws ParserConfigurationException, SAXException, IOException {
        SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
        SAXParser saxParser = saxParserFactory.newSAXParser();
        JsonMlHandler handler = new JsonMlHandler();
        saxParser.parse(stream, handler);
        return handler;
    }

    public static void main(String[] args) {
    SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
    try {
        SAXParser saxParser = saxParserFactory.newSAXParser();
        JsonMlHandler handler = new JsonMlHandler();
        saxParser.parse(new File(args[0]), handler);

        String json  = handler.getJsonString();
        System.out.println(json);

    } catch (ParserConfigurationException | SAXException | IOException e) {
        e.printStackTrace();
    }
    }

}