import java.io.*;

public class HTMLAnlisten {

    public static void main(String[] args) {
        /* Aufruf des Moduls */
        new HTMLAnlisten(
                "/home/gitRepos/TestRepository/Hilfsmodule/TestDaten/RMTvg.html", // von
                "/home/gitRepos/TestRepository/Hilfsmodule/TestDaten/RMTvg0.html"); // nach

    }

    /* Beginn desModuls */
    BufferedReader ein = null;
    PrintWriter aus = null;
    StringBuffer einZeile;
    StringBuffer ausZeile;
    String zeile;
    boolean autoFlush = true;
    int einrueck = 0;

    public HTMLAnlisten(String pfadEin, String pfadAus) {

        try {
            ein = new BufferedReader(new FileReader(pfadEin));
            aus = new PrintWriter(new FileWriter(pfadAus), autoFlush);

            einZeile = new StringBuffer();

            while ((zeile = ein.readLine()) != null) {
                einZeile.append(zeile);
            }
            aufbereiten(einZeile);
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        finally {

        }
    }

    /**
     * Aufbereiten des Eingabestrings für die Ausgabe
     */
    void aufbereiten(StringBuffer eingabe) throws Exception {
        int anf = 0;
        int end = 1;
        int start = 0;
        int stop = einZeile.length();
//        int i = 0;

        anf = einZeile.indexOf("<", start);
        end = einZeile.indexOf(">", start);
        do {
//            System.out.println("start=" + start + " anf=" + anf + " end=" + end);
//            System.out.println(einZeile.substring(start, end + 1));
            if (start < anf) {
//                System.out.println("strt+anf");
                zeile = einZeile.substring(start, anf);
//                System.out.println("#"+zeile+"#");
                einrueck = ausgeben(zeile, einrueck);
                start = anf;
            }
            zeile = einZeile.substring(anf, end + 1);
            einrueck = ausgeben(zeile, einrueck);
            start = end + 1;
//            if (i > 20)
//                break;
//            i++;
            anf = einZeile.indexOf("<", start);
            end = einZeile.indexOf(">", start);
        } while (start <= stop && anf != end);

    }

    /**
     * Ausgeben der einzelnen Zeilen
     */
    public int ausgeben(String zeile, int einrueck) throws Exception {
        int endeFeld = 0;

//        System.out.println("ausgeben:zeile ->" + zeile+"<-");
//        System.out.println("ausgeben:zeilenlänge=" + zeile.length());

        endeFeld = zeile.indexOf(" ");
//        System.out.println("ausgeben:Leerz=" + endeFeld);
        // kein Leerzeichen gefunden 
        // suchen nach Tag-Ende         
        if (endeFeld <= 0) {
            endeFeld = zeile.indexOf(">");
        }
        // Weder Leerzeichen noch Tag-Ende gefunden 
        // es liegt ein reines Textfeld vor
        if (endeFeld <= 0) {
            endeFeld = zeile.length();
        }
        // System.out.println(endeFeld);

        if (endeFeld >= 10) {
            endeFeld = 10;
        }
        // System.out.println(endeFeld);

        String testFeld = zeile.substring(0, endeFeld);
        // System.out.println(testFeld);

        switch (testFeld) {
            case "<html":
            case "<head":
            case "<title":
            case "<body":
            case "<table":
            case "<theader":
            case "<tbody":
            case "<tr":
            case "<th":
            case "<td":
                einruecken(einrueck);
                einrueck++;
                // System.out.println("ER+=" + einrueck);
                break;

            case "</title":
            case "</head":
            case "</theader":
            case "</tr":
            case "</th":
            case "</td":
            case "</tbody":
            case "</table":
            case "</body":
            case "</html":
                einrueck--;
                einruecken(einrueck);
                // System.out.println("ER-=" + einrueck);
                break;

            default:
                einruecken(einrueck);
                // System.out.println("default");
                break;
        }
        aus.println(zeile);

        return einrueck;
    }

    /**
     * @param einrueck
     */
    public void einruecken(int einrueck) {
        for (int i = 0; i < einrueck; i++) {
            aus.print("! ");
        }
    }
}
