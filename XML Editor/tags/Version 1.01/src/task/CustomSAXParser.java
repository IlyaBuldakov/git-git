package task;

import java.io.*;

import org.xml.sax.*;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.SAXParserFactory;

public class CustomSAXParser extends DefaultHandler {

    public static void main(String argv[]) {

        if (argv.length != 1) {
            System.err.println("Usage: cmd filename");
            System.exit(1);
        }
        DefaultHandler handler = new CustomSAXParser();
        SAXParserFactory factory = SAXParserFactory.newInstance();
        try {
            out = new OutputStreamWriter(System.out, "UTF8");
            javax.xml.parsers.SAXParser saxParser = factory.newSAXParser();
            saxParser.parse( new File(argv[0]), handler);
        } catch (Throwable t) {
            t.printStackTrace();
        }
        System.exit(0);
    }

    static private Writer  out;

    public void startDocument() throws SAXException {
        emit("<?xml version='1.0' encoding='UTF-8'?>");
        startNewLine();
    }

    public void endDocument() throws SAXException {
        try {
            startNewLine();
            out.flush();
        } catch (IOException e) {
            throw new SAXException("I/O error", e);
        }
    }

    public void startElement(String namespaceURI,
                             String lName,
                             String qName,
                             Attributes attrs)
    throws SAXException {
        String eName = lName;
        if ("".equals(eName)) {
            eName = qName;
        }
        emit("<" + eName);
        if (attrs != null) {
            for (int i = 0; i < attrs.getLength(); i++) {
                String aName = attrs.getLocalName(i);
                if ("".equals(aName)) {
                    aName = attrs.getQName(i);
                }
                emit(" ");
                emit(aName + "=\"" + attrs.getValue(i) + "\"");
            }
        }
        emit(">");
    }

    public void endElement(String namespaceURI,
                           String sName,
                           String qName
                          )
    throws SAXException {
        emit("</" + sName + ">");
    }

    public void characters(char buf[], int offset, int len)
    throws SAXException {
        String s = new String(buf, offset, len);
        emit(s);
    }

   private void emit(String s) throws SAXException {
        try {
            out.write(s);
            out.flush();
        } catch (IOException e) {
            throw new SAXException("I/O error", e);
        }
    }

    private void startNewLine() throws SAXException {
        String lineEnd =  System.getProperty("line.separator");
        try {
            out.write(lineEnd);
        } catch (IOException e) {
            throw new SAXException("I/O error", e);
        }
    }
}