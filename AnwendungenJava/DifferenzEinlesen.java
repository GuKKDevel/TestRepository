import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;

public class DifferenzEinlesen {

    public static void main(String[] args) {
        new DifferenzEinlesen(
                "/home/gitRepos/TestRepository/ReisemobilTagebuch/Daten/EintragTagebuch.xml");

    }

    int debug = 1;
    int debugCount = 0;

    BufferedReader in = null;
    StringBuffer puffer = new StringBuffer();
    String zeile = null;

    public DifferenzEinlesen(String einDateiname) {

        try {
            InputStream is = new FileInputStream(einDateiname);

            in = new BufferedReader(new InputStreamReader(is, "UTF-8"));

            while ((zeile = in.readLine()) != null) {
                puffer.append(zeile.trim());
            }
            auswertenPuffer();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            if (in != null) {
                try {
                    in.close();
                }
                catch (Exception ec) {
                    ec.printStackTrace();
                }
                finally {
                }
            }
        }
    }

    void auswertenPuffer() {

        int start = 0;
        int ende = 0;
        String etEintrag = null;
        String etoEintrag = null;
        String kmStand = null;
        protokollieren ("LängePuffer="+puffer.length());
        while (start < puffer.length() && start >=0 ){
            protokollieren ("Start="+start);
            start = puffer.indexOf("<column name=", start);
            if (start < 0) {
                break;
            }
                
            ende = puffer.indexOf("</column>", start);
            kmStand = puffer.substring(start + 20, ende);
            protokollieren(kmStand);
            start = ende + 9;

            start = puffer.indexOf("<column name=", start);
            ende = puffer.indexOf("</column>", start);
            etEintrag = puffer.substring(start + 25, ende);
            protokollieren(etEintrag);
            start = ende + 9;
            
            start = puffer.indexOf("<column name=", start);
            ende = puffer.indexOf("</column>", start);
            etoEintrag = puffer.substring(start + 26, ende);
            protokollieren(etoEintrag);
            start = ende + 9;

            vergleichenEintraege(etEintrag, etoEintrag);
        } 
    }

    void vergleichenEintraege(String etEintrag, String etoEintrag) {
        protokollieren("Länge= " + etEintrag.length() + " und "
                + etoEintrag.length());

        int iET = 0;
        etEintrag = etEintrag.replaceAll("  ", "");
        int iETl = etEintrag.length();

        int iETO = 0;
        etoEintrag = etoEintrag.replaceAll("  ", " ");
        int iETOl = etoEintrag.length();

        while (iET < iETl && iETO < iETOl) {
            if (!etEintrag.substring(iET, iET + 1).equals(
                    etoEintrag.substring(iETO, iETO + 1))) {
                char chET = etEintrag.charAt(iET);
                char chETO = etoEintrag.charAt(iETO);

                String hexET = String.format("%04x", (int) chET);
                String hexETO = String.format("%04x", (int) chETO);

                protokollieren("stelle:" + iET + " ->" + hexET + "<- " + iETO
                        + "->" + hexETO + "<-");
            }
            iET++;
            iETO++;

        }

    }

    /**
     * Debug-Hilfe
     * 
     * @param protokollEintrag
     */
    void protokollieren(String protokollEintrag) {
        if (debug > 0 && debugCount < 2000) {
            System.out.println(protokollEintrag);
        }
        debugCount++;
    }
}
