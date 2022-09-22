package task;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.xml.sax.SAXException;

import java.io.*;

import org.w3c.dom.Document;

public class CustomDOMParser {
    // Global value so it can be ref'd by the tree-adapter
    static Document document;
    public static String FILE_NAME = "example.xml";
    public static void main(String argv[]) {
        if (argv.length != 1) {
            System.err.println("Usage: java DomEcho filename");
            System.exit(1);
        }

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        //factory.setValidating(true);
        //factory.setNamespaceAware(true);
        try {
            DocumentBuilder builder = factory.newDocumentBuilder();
            document = builder.parse(new File(argv[0]));
            Source source = new DOMSource(document);
            Writer writer = new StringWriter();

            Result result = new StreamResult(writer);
            Transformer xformer;

            try {
                xformer = TransformerFactory.newInstance().newTransformer();
                xformer.transform(source, result);
                System.out.println(writer.toString());
            } catch (TransformerConfigurationException e) {
                e.printStackTrace();
            } catch (TransformerException e) {
                e.printStackTrace();
            }

        } catch (SAXException sxe) {
            Exception x = sxe;
            if (sxe.getException() != null)
                x = sxe.getException();
            x.printStackTrace();
        } catch (ParserConfigurationException pce) {
            pce.printStackTrace();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

}