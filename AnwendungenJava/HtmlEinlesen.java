import java.io.*;

/**
 * @author Karl-Heinz Gödderz
 *
 */
public class HtmlEinlesen {

    public static void main(String[] args) throws Exception {
        new HtmlEinlesen(
                // WINDOWS
                // "C:/Users/khg/Desktop/Programmierung/Daten/Tagebuch_im_Reisemobil.html",
                // "C:/Users/khg/Desktop/Programmierung/Daten/TestDok.txt",
                // "C:/Users/khg/Desktop/Programmierung/Daten/TestDok2.txt");
                //
                // LINUX
                "/home/gitRepos/TestRepository/ReisemobilTagebuch/Daten/Tagebuch_im_Reisemobil.html",
                "/home/gitRepos/TestRepository/ReisemobilTagebuch/Daten/TiR.txt");

    }

    int debug = 0;
    int maxLen = 0;
    int minLen = 999999999;
    // Hashtable tags;

    // String ausgabeVersion = "H"; /* HTML */
    String ausgabeVersion = "S"; /* SQL */
    String fortsetzungSQL = "";
    String maskierungSQL = "";

    ReisemobilTagebuchEintrag eintrag;

    BufferedReader in = null;
   
    PrintWriter out = null;
    StringBuffer puffer;
    String zeile = null;

    int startMarkierung = 0;
    int startMarkierungPlus = 0;
    int endeMarkierung = 0;
    int endeMarkierungPlus = 0;
    boolean weiter = false;
    String kennung = null;

    boolean bearbeiteZeile = false;
    boolean bearbeiteFeld = false;
    // boolean bearbeiteMarkierung = false;
    boolean verarbeitungBegonnen = false;

    int nummerEintrag = 0;
    int nummerSpalte = 0;
    int nummerFeld = 0;

    /**
     * @param pathIn
     *            Pfad Eingabedatei
     * @param pathOut
     *            Pfad Ausgabedatei
     * 
     * @throws IOException
     */
    public HtmlEinlesen(String pathIn, String pathOut)
            throws IOException {

        // tags = new Hashtable<String, Integer>();

        try {

            OutputStream os = new FileOutputStream(pathOut);
            out = new PrintWriter(new OutputStreamWriter(os, "UTF-8"));

            switch (ausgabeVersion) {
                case "H":
                    out.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
                    out.println("  <ReisemobilTagebuch>");
                    break;

                case "S":
                    out.println("drop table if exists `EintragTagebuchOFF`;");
                    out.println("CREATE TABLE IF NOT EXISTS `EintragTagebuchOFF` (");
                    out.println("  `T_id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT 'Identifikation',");
                    out.println("  `T_Km` varchar(10) NOT NULL DEFAULT '00.000' COMMENT 'Km-Stand',");
                    out.println("  `T_Land` varchar(4) NOT NULL DEFAULT 'D' COMMENT 'Land für den Eintrag (aus Tabelle EL)',");
                    out.println("  `T_Ort` varchar(255) NOT NULL DEFAULT 'Köln-Dünnwald' COMMENT 'Ort ',");
                    out.println("  `T_Datum` date NOT NULL DEFAULT '0000-00-00' COMMENT 'Eintragsdatum',");
                    out.println("  `T_Zeit` time NOT NULL DEFAULT '00:00:00' COMMENT 'Eintragszeit',");
                    out.println("  `T_Autor` varchar(50) NOT NULL COMMENT 'Kürzel für  Autor(en)',");
                    out.println("  `T_Eintrag` text NOT NULL COMMENT 'Tagebucheintrag',");
                    out.println("  `T_Zeitmarke` varchar(50) NOT NULL DEFAULT '0000-00-00 00:00 00.000' COMMENT 'Zusammengesetzer Zweitschlüssel',");
                    out.println("  `T_Eingabe` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00' COMMENT 'Eingabedatum',");
                    out.println("  `T_Aenderung` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00' ON UPDATE CURRENT_TIMESTAMP COMMENT 'Änderungsdatum',");
                    out.println("  `T_BildVorhanden` tinyint(1) NOT NULL DEFAULT '0' COMMENT 'Kennzeichen, ob Bilder vorhanden',");
                    out.println("  PRIMARY KEY (`T_id`),");
                    out.println("  KEY `T_Km` (`T_Km`),");
                    out.println("  KEY `T_Ort` (`T_Ort`),");
                    out.println("  KEY `T_Land` (`T_Land`) COMMENT 'Femdschlüssel aus Landestabelle',");
                    out.println("  KEY `T_Zeitmarke` (`T_Zeitmarke`)");
                    out.println(") ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=0;");
                    out.println(" ");
                    out.println("--");
                    out.println("-- Daten für Tabelle `EintragTagebuchOFF`");
                    out.println("--");
                    out.println("");
                    out.println("INSERT INTO `EintragTagebuchOFF` (`T_id`, `T_Km`, `T_Land`, `T_Ort`,  `T_Datum`, `T_Zeit`, `T_Autor`, `T_Eintrag`, `T_Zeitmarke`, `T_Eingabe`, `T_Aenderung`, `T_BildVorhanden`) VALUES");
                    maskierungSQL = "\\";
                    break;
                default:
                    break;
            }

            InputStream is = new FileInputStream(pathIn);
            in = new BufferedReader(new InputStreamReader(is, "UTF-8"));

            puffer = new StringBuffer();
            zeile = null;

            while ((zeile = in.readLine()) != null) {
                protokollieren("v:"+puffer.toString());
                if (puffer.length() > 0 /*&& !puffer.substring(puffer.length()-1).equals(" ")*/)
                    puffer.append(" ");
                puffer.append(normierenZeile(zeile.trim()));
                protokollieren("n:"+puffer.toString());
                auswertenPuffer(puffer);
            }
            switch (ausgabeVersion) {
                case "H":
                    out.println("  </ReisemobilTagebuch>");
                    break;
                case "S":
                    out.println(";");
                    break;
                default:
                    break;
            }

        }
        catch (Exception e) {
            e.printStackTrace();
        }

        finally {
            if (in != null) {
                in.close();
            }
            if (out != null) {
                out.close();
            }
        }
        System.out.println("fertig");
    }

    /**
     * Analysieren des Puffers (gesammelte Eingabezeilen) nach HTML-Tags
     * 
     * @param puffer
     */
    private void auswertenPuffer(StringBuffer puffer) {
        weiter = false;
        while (!weiter) {
            startMarkierung = puffer.indexOf("<");
            if (startMarkierung >= 0) {
                if (startMarkierung < puffer.length()) {
                    startMarkierungPlus = startMarkierung + 1;
                }
                else {
                    startMarkierungPlus = startMarkierung;
                }

                endeMarkierung = puffer.indexOf(">", startMarkierungPlus);
                endeMarkierungPlus = endeMarkierung + 1;

                if (endeMarkierung > 0) {
                    verarbeitenKennung();
                    if (endeMarkierungPlus > puffer.length())
                        endeMarkierungPlus = puffer.length();

                }
                else {
                    weiter = true;
                }
            }
            else {
                weiter = true;

            }
        }
    }

    /**
     * Es wird die erkannte Kennung verarbeitet und die entsprechenden Aktionen
     * veranlasst
     */
    private void verarbeitenKennung() {

        // Ermitteln des HTML-Tags

        kennung = puffer.substring(startMarkierung, endeMarkierungPlus);
        String auswahl = kennung.substring(1, kennung.length() - 1);
        if (auswahl.indexOf(" ") >= 0) {
            auswahl = auswahl.substring(0, auswahl.indexOf(" "));
        }

        protokollieren("\n" + eintrag + "\n" + puffer + "\n" + auswahl + "<->"
                + kennung + "\n" + "\n");

        // Verarbeiten des HTML-Tags
        switch (auswahl) {
            case "p":
                bearbeiteFeld = true;
                puffer.delete(startMarkierung, endeMarkierungPlus);
                break;

            case "/p":
                eintragenFeld(puffer.substring(0, startMarkierung));
                puffer.delete(startMarkierung, endeMarkierungPlus);
                bearbeiteFeld = false;
                puffer.delete(0, startMarkierung);
                break;

            case "tbody":
                verarbeitungBegonnen = true;
                puffer.delete(startMarkierung, endeMarkierungPlus);
                break;

            case "td":
                nummerSpalte = nummerSpalte + 1;
                puffer.delete(startMarkierung, endeMarkierungPlus);
                break;

            case "tr":
                bearbeiteZeile = true;
                bearbeiteFeld = false;
                nummerSpalte = 0;
                nummerFeld = 0;

                neuesElementAnlegen();
                puffer.delete(startMarkierung, endeMarkierungPlus);
                break;

            case "/tr":
                if (verarbeitungBegonnen & eintrag != null) {
                    schreibenEintrag();
                }
                puffer.delete(startMarkierung, endeMarkierungPlus);
                break;

            default:
                puffer.delete(startMarkierung, endeMarkierungPlus);
                if (!verarbeitungBegonnen) {
                    puffer.delete(0, startMarkierung);
                }
                break;
        }
    }

    /**
     * Debug-Hilfe
     * 
     * @param protokollEintrag
     */
    private void protokollieren(String protokollEintrag) {
        if (debug > 0) {
            if (nummerEintrag > 277 & nummerEintrag < 279) {
                System.out.println(protokollEintrag);
            }
        }
    }

    /**
     * Schreiben des Eintrags
     */
    private void schreibenEintrag() {
        if ((eintrag.kmStand == null || eintrag.kmStand.equals(""))
                && (eintrag.ort == null || eintrag.ort.equals(""))
                && (eintrag.datum == null || eintrag.datum.equals(""))
                && (eintrag.uhrzeit == null || eintrag.uhrzeit.equals(""))
                && (eintrag.autor == null || eintrag.autor.equals(""))
                && (eintrag.text == null || eintrag.text.equals(""))) {
            nummerEintrag--;
        }
        else {
            if (eintrag.datum != null && !eintrag.datum.equals("")) {
                if (eintrag.uhrzeit == null || eintrag.uhrzeit.equals("")) {
                    eintrag.uhrzeit = "12:00";
                }
                else {
                }
                eintrag.zeitmarke = eintrag.datum + "-" + eintrag.uhrzeit;
                if (eintrag.kmStand != null && !eintrag.kmStand.equals("")) {
                    eintrag.zeitmarke += "-" + eintrag.kmStand;
                }
            }
            if (out != null) {

                switch (ausgabeVersion) {
                    case "H":
                        out.println("    <Eintrag kennung=\"" + eintrag.kennung
                                + "\">");

                        out.println("      <kmStand>");
                        out.println("        " + eintrag.kmStand);
                        out.println("      </kmStand>");

                        out.println("      <ort>");
                        out.println("        " + eintrag.ort);
                        out.println("      </ort>");

                        out.println("      <datum>");
                        out.println("        " + eintrag.datum);
                        out.println("      </datum>");

                        out.println("      <uhrzeit>");
                        out.println("        " + eintrag.uhrzeit);
                        out.println("      </uhrzeit>");

                        out.println("      <zeitmarke>");
                        out.println("        " + eintrag.zeitmarke);
                        out.println("      </zeitmarke>");

                        out.println("      <autor>");
                        out.println("        " + eintrag.autor);
                        out.println("      </autor>");

                        out.println("      <text>");
                        out.println("        " + eintrag.text);
                        out.println("      </text>");
                        //
                        // writer.writeCharacters("    ");
                        // writer.writeStartElement("", "Text", "");
                        // writer.writeCharacters("      " + eintrag.text);
                        // writer.writeCharacters("    ");
                        // writer.writeEndElement();// Text
                        //
                        // writer.writeCharacters("  ");

                        out.println("    </Eintrag>");

                        break;

                    case "S":
                        String zwZeichen;
                        StringBuffer zwText = new StringBuffer();
                        for (int i = 0; i < eintrag.text.length(); i++) {
                            zwZeichen = eintrag.text.substring(i, i + 1);
                            if (zwZeichen.equals("'"))
                                zwText.append("\\");
                            zwText.append(zwZeichen);
                        }
                        eintrag.text = zwText.toString();
                        out.println(fortsetzungSQL);
                        out.print("(" + eintrag.kennung + ", '"
                                + eintrag.kmStand + "', '" + eintrag.land
                                + "', '" + eintrag.ort + "', '" + eintrag.datum
                                + "', '" + eintrag.uhrzeit + "', '"
                                + eintrag.autor + "', '" + eintrag.text
                                + "', '" + eintrag.zeitmarke + "', "
                                + "now(), " + "now(), " + "0)");
                        fortsetzungSQL = ",";
                        break;
                    default:
                        break;
                }

            }
        }
    }

    /**
     * Anlegen eines neuen Elementes
     */
    private void neuesElementAnlegen() {
        if (!verarbeitungBegonnen)
            return;
        eintrag = new ReisemobilTagebuchEintrag();
        nummerEintrag++;
        eintrag.kennung = nummerEintrag;
        eintrag.kmStand = "";
        eintrag.land = "";
        eintrag.ort = "";
        eintrag.datum = "";
        eintrag.uhrzeit = "";
        eintrag.autor = "";
        eintrag.text = "";
        eintrag.zeitmarke = "";
    }

    /**
     * Der uebergebene String wird in das Feld uebertragen, dem er zugeordnet
     * ist
     * 
     * @param substring
     */
    private void eintragenFeld(String substring) {
        if (nummerSpalte == 1) {
            if (nummerFeld <= 1) {
                // erste Spalte erstes Feld: Km-Stand
                if (!substring.equals("")) {
                    eintrag.kmStand = textAufbereiten(substring);

                    if (eintrag.kmStand.contains("K�ln")) {
                        eintrag.kmStand = "";
                        eintrag.ort = "K�ln";
                        eintrag.land = bestimmenLand(eintrag.ort);
                    }
                    else
                        switch (eintrag.kmStand) {
                            case "43760":
                                eintrag.kmStand = "43.760";
                                break;

                            case "44789":
                                eintrag.kmStand = "44.789";
                                break;

                            case "44799":
                                eintrag.kmStand = "44.799";
                                break;

                            case "44801":
                                eintrag.kmStand = "44.801";
                                break;

                            case "44802":
                                eintrag.kmStand = "44.802";
                                break;

                            case "44813":
                                eintrag.kmStand = "44.813";
                                break;

                            default:
                                break;
                        }
                }
                nummerFeld = 2;
            }
            else {
                // erste Spalte zweites Feld: Ort
                eintrag.ort = textAufbereiten(substring);
                eintrag.land = bestimmenLand(eintrag.ort);
            }
        }

        else
            if (nummerSpalte == 2) {
                if (nummerFeld < 3) {
                    // zweite Spalte erstes Feld: Datum
                    if (!substring.equals("")) {
                        eintrag.datum = "20" + substring.substring(6, 8) + "-"
                                + substring.substring(3, 5) + "-"
                                + substring.substring(0, 2);
                        // das Datum enth�lt auch die Uhrzeit
                        if (substring.length() == 14) {
                            eintrag.uhrzeit = substring.substring(9, 14);
                        }
                    }
                    nummerFeld = 3;
                }
                else {
                    if (nummerFeld == 3) {
                        if (eintrag.uhrzeit.equals("")) {
                            eintrag.uhrzeit = "12:00";
                        }

                        // zweite Spalte zweites Feld: Uhrzeit oder Autor
                        switch (substring) {
                            case "khg":
                                // Uhrzeit fehlt, stattdessen Autor
                                eintrag.autor = "khg";
                                break;

                            case "udk":
                                // Uhrzeit fehlt, stattdessen Autor
                                eintrag.autor = "uk";
                                break;

                            case "uk":
                                // Uhrzeit fehlt, stattdessen Autor
                                eintrag.autor = "uk";
                                break;

                            case "beide":
                                // Uhrzeit fehlt, stattdessen Autor
                                eintrag.autor = "khg,uk";
                                break;

                            case "khg/udk":
                                // Uhrzeit fehlt, stattdessen Autor
                                eintrag.autor = "khg,uk";
                                break;

                            case "khg/uk":
                                // Uhrzeit fehlt, stattdessen Autor
                                eintrag.autor = "khg,uk";
                                break;

                            case "khg/uk*":
                                // Uhrzeit fehlt, stattdessen Autor
                                eintrag.autor = "khg,uk";
                                break;

                            case "khg-udk":
                                // Uhrzeit fehlt, stattdessen Autor
                                eintrag.autor = "khg,uk";
                                break;

                            case "?":
                                break;

                            default:
                                if (substring.length() == 4) {
                                    eintrag.uhrzeit = "0" + substring;
                                }
                                else {
                                    eintrag.uhrzeit = substring;
                                }
                                break;
                        }

                    }
                    else {
                        // zweite Spalte drittes Feld: Autor oder Uhrzeit
                        if (!substring.equals(""))
                            eintrag.autor = substring;
                    }
                }
            }
            else
                if (nummerSpalte == 3) {
                    // dritte Spalte erstes Feld: Beschreibung
                    if (eintrag.text.equals("")) {
                        eintrag.text = textAufbereiten(substring);
                    }
                    else {
                        eintrag.text += " \n" + textAufbereiten(substring);
                    }
                }
    }

    /**
     * Festlegen des Landes fuer die unterschiedlichen Orte
     * 
     * @param ort
     * @return
     */
    private String bestimmenLand(String ort) {

        switch (ort) {

            case "Sainte-Croix-en-Plaine":
                return "F";

            case "irgendwo in Frankreich":
                return "F";

            case "Moncel-l�s-Lun�ville":
                return "F";

            case "Lun�ville":
                return "F";

            case "Anould":
                return "F";

            case "B-Westende":
                eintrag.ort = "Westende";
                return "B";

            default:
                return "D";
        }
    }

    /**
     * Ein uebergebenes Textfeld wird auf Symbolisierungen untersucht, diese
     * werden aufgeloest und der bereinigte Text zur�ckgegeben.
     * 
     * @param substring
     * @return symbolfreier Text
     */
    private String textAufbereiten(String substring) {
        StringBuffer zwFeld = new StringBuffer(substring);
        boolean nochmal = true;
        substring =substring.replaceAll("  ", " ");
        while (nochmal) {
            nochmal = false;
            if (zwFeld.indexOf("&Auml;") >= 0) {
                zwFeld = new StringBuffer(zwFeld.substring(0,
                        zwFeld.indexOf("&Auml;"))
                        + "�" + zwFeld.substring(zwFeld.indexOf("&Auml;") + 6));
                nochmal = true;
            }
            if (zwFeld.indexOf("&auml;") >= 0) {
                zwFeld = new StringBuffer(zwFeld.substring(0,
                        zwFeld.indexOf("&auml;"))
                        + "�" + zwFeld.substring(zwFeld.indexOf("&auml;") + 6));
                nochmal = true;
            }
            if (zwFeld.indexOf("&Ouml;") >= 0) {
                zwFeld = new StringBuffer(zwFeld.substring(0,
                        zwFeld.indexOf("&Ouml;"))
                        + "�" + zwFeld.substring(zwFeld.indexOf("&Ouml;") + 6));
                nochmal = true;
            }
            if (zwFeld.indexOf("&ouml;") >= 0) {
                zwFeld = new StringBuffer(zwFeld.substring(0,
                        zwFeld.indexOf("&ouml;"))
                        + "�" + zwFeld.substring(zwFeld.indexOf("&ouml;") + 6));
                nochmal = true;
            }
            if (zwFeld.indexOf("&Uuml;") >= 0) {
                zwFeld = new StringBuffer(zwFeld.substring(0,
                        zwFeld.indexOf("&Uuml;"))
                        + "�" + zwFeld.substring(zwFeld.indexOf("&Uuml;") + 6));
                nochmal = true;
            }
            if (zwFeld.indexOf("&uuml;") >= 0) {
                zwFeld = new StringBuffer(zwFeld.substring(0,
                        zwFeld.indexOf("&uuml;"))
                        + "�" + zwFeld.substring(zwFeld.indexOf("&uuml;") + 6));
                nochmal = true;
            }

            // Sonderzeichen �
            if (zwFeld.indexOf("&szlig;") >= 0) {
                zwFeld = new StringBuffer(zwFeld.substring(0,
                        zwFeld.indexOf("&szlig;"))
                        + maskierungSQL
                        + "�"
                        + zwFeld.substring(zwFeld.indexOf("&szlig;") + 7));
                nochmal = true;
            }

            // gesch�tzter Trennungsstrich
            if (zwFeld.indexOf("&shy;") >= 0) {
                zwFeld = new StringBuffer(zwFeld.substring(0,
                        zwFeld.indexOf("&shy;"))
                        + zwFeld.substring(zwFeld.indexOf("&shy;") + 5));
                nochmal = true;
            }

            // Anf�hrungszeichen unten
            if (zwFeld.indexOf("&bdquo;") >= 0) {
                zwFeld = new StringBuffer(zwFeld.substring(0,
                        zwFeld.indexOf("&bdquo;"))
                        + maskierungSQL
                        + "�"
                        + zwFeld.substring(zwFeld.indexOf("&bdquo;") + 7));
                nochmal = true;
            }

            // Anf�hrungszeichen oben
            if (zwFeld.indexOf("&ldquo;") >= 0) {
                zwFeld = new StringBuffer(zwFeld.substring(0,
                        zwFeld.indexOf("&ldquo;"))
                        + maskierungSQL
                        + "�"
                        + zwFeld.substring(zwFeld.indexOf("&ldquo;") + 7));
                nochmal = true;
            }

            // Anf�hrungszeichen rechts
            if (zwFeld.indexOf("&rsquo;") >= 0) {
                zwFeld = new StringBuffer(zwFeld.substring(0,
                        zwFeld.indexOf("&rsquo;"))
                        + maskierungSQL
                        + "�"
                        + zwFeld.substring(zwFeld.indexOf("&rsquo;") + 7));
                nochmal = true;
            }

            // Anf�hrungszeichen rechts
            if (zwFeld.indexOf("&acute;") >= 0) {
                zwFeld = new StringBuffer(zwFeld.substring(0,
                        zwFeld.indexOf("&acute;"))
                        + maskierungSQL
                        + "�"
                        + zwFeld.substring(zwFeld.indexOf("&acute;") + 7));
                nochmal = true;
            }

            // Anf�hrungszeichen links
            if (zwFeld.indexOf("&lsquo;") >= 0) {
                zwFeld = new StringBuffer(zwFeld.substring(0,
                        zwFeld.indexOf("&lsquo;"))
                        + maskierungSQL
                        + "�"
                        + zwFeld.substring(zwFeld.indexOf("&lsquo;") + 7));
                nochmal = true;
            }

            // Gedankenstrich
            if (zwFeld.indexOf("&ndash;") >= 0) {
                zwFeld = new StringBuffer(zwFeld.substring(0,
                        zwFeld.indexOf("&ndash;"))
                        + "�" + zwFeld.substring(zwFeld.indexOf("&ndash;") + 7));
                nochmal = true;
            }

            // e-accent-grave �
            if (zwFeld.indexOf("&egrave;") >= 0) {
                zwFeld = new StringBuffer(zwFeld.substring(0,
                        zwFeld.indexOf("&egrave;"))
                        + maskierungSQL
                        + "�"
                        + zwFeld.substring(zwFeld.indexOf("&egrave;") + 8));
                nochmal = true;
            }

            // e-accent-acute �
            if (zwFeld.indexOf("&eacute;") >= 0) {
                zwFeld = new StringBuffer(zwFeld.substring(0,
                        zwFeld.indexOf("&eacute;"))
                        + maskierungSQL
                        + "�"
                        + zwFeld.substring(zwFeld.indexOf("&eacute;") + 8));
                nochmal = true;
            }

        }
        return zwFeld.toString();
    }

    /**
     * Uebergeben wird eine Eingabezeile der Datei, diese wird von den
     * fuehrenden Tabulatoren befreit.
     * 
     * @param zeile
     * @return zeile ohne Tabulatoren
     */
    private String normierenZeile(String zeile) {
        char c = zeile.toCharArray()[0];
        while ((int) c == 9) {
            zeile = zeile.substring(1, Math.max(1, zeile.length()));
            c = zeile.toCharArray()[0];
        }
        return zeile;
    }

}
