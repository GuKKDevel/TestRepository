import javax.xml.parsers.*;

import org.xml.sax.*;
import org.xml.sax.helpers.*;

import java.util.*;
import java.io.*;

public class WordpressAuslesen
        extends DefaultHandler {

    
    public static void main (String[] args[]){
    
}
//    private Hashtable tags;
    private int zaehl = 0;
    boolean schreiben = false;

    public void startDocument() throws SAXException {
//        tags = new Hashtable();
    }

    public void startElement(String namespaceURI, String localName, String qName, Attributes atts)
            throws SAXException {
        if (localName.equals("item")) {
            schreiben = true;
        }
        if (zaehl < 100) {
            System.out.println(localName);
            zaehl++;
        }
        String key = localName;
//        Object value = tags.get(key);

//        if (value == null) {
//            tags.put(key, new Integer(1));
//        }
//        else {
//            int count = ((Integer) value).intValue();
//            count++;
//            tags.put(key, new Integer(count));
//        }
    }

    public void characters(char[] zeichenArray, int anfang, int laenge) {

        StringBuffer kette = new StringBuffer();
        for (int i = anfang; i < anfang + laenge; i++) {
            kette.append(zeichenArray[i]);
        }
        if (schreiben) {
            if (zaehl < 100) {
                System.out.println(anfang + " " + laenge + " " + kette);
            }
        }
    }
    public void endElement (String uri, String localName, String qName) {
        schreiben = false;
    }
            


//    public void endDocument() throws SAXException {
//        Enumeration e = tags.keys();
//        while (e.hasMoreElements()) {
//            String tag = (String) e.nextElement();
//            int count = ((Integer) tags.get(tag)).intValue();
//            System.out.println("Local Name \"" + tag + "\" occurs " + count + " times");
//        }
//    }

    static public void main(String[] args) throws Exception {
        // ...
        String filename = null;

        for (int i = 0; i < args.length; i++) {
            filename = args[i];
            if (i != args.length - 1) {
                usage();
            }
        }

        if (filename == null) {
            usage();
        }

        SAXParserFactory spf = SAXParserFactory.newInstance();
        spf.setNamespaceAware(true);
        SAXParser saxParser = spf.newSAXParser();

        XMLReader xmlReader = saxParser.getXMLReader();
        xmlReader.setContentHandler(new WordpressAuslesen());
        xmlReader.setErrorHandler(new MyErrorHandler(System.err));

        xmlReader.parse(convertToFileURL(filename));
    }

    /**
     * Konvertieren eines Eingabestrings in eine URL vom Typ file:
     * 
     * @param filename
     * @return Pfad
     */
    private static String convertToFileURL(String filename) {
        String path = new File(filename).getAbsolutePath();
        if (File.separatorChar != '/') {
            path = path.replace(File.separatorChar, '/');
        }

        if (!path.startsWith("/")) {
            path = "/" + path;
        }
        System.out.println("file:" + path);
        return "file:" + path;
    }

    /**
     * Fehlermeldungen ausgeben
     */
    private static void usage() {
        System.err.println("Usage: WordpressAuslesen <file.xml>");
        System.err.println("       -usage or -help = this message");
        System.exit(1);
    }

    private static class MyErrorHandler
            implements ErrorHandler {

        private PrintStream out;

        MyErrorHandler(PrintStream out) {
            this.out = out;
        }

        private String getParseExceptionInfo(SAXParseException spe) {
            String systemId = spe.getSystemId();

            if (systemId == null) {
                systemId = "null";
            }

            String info =
                    "URI=" + systemId + " Line=" + spe.getLineNumber() + ": " + spe.getMessage();

            return info;
        }

        public void warning(SAXParseException spe) throws SAXException {
            out.println("Warning: " + getParseExceptionInfo(spe));
        }

        public void error(SAXParseException spe) throws SAXException {
            String message = "Error: " + getParseExceptionInfo(spe);
            throw new SAXException(message);
        }

        public void fatalError(SAXParseException spe) throws SAXException {
            String message = "Fatal Error: " + getParseExceptionInfo(spe);
            throw new SAXException(message);
        }
    }

}
