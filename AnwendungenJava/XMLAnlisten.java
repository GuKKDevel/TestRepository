import java.io.BufferedReader;
import java.io.FileReader;
import java.io.PrintWriter;

public class XMLAnlisten {

    public static void main(String[] args) throws Exception {
        
        new XMLAnlisten("/home/gitRepos/TestRepository/ReisemobilTagebuch/Daten/EintragTagebuch.xml", // Tagebuch_im_Reisemobil.html",
                "/home/gitRepos/TestRepository/Hilfsmodule/TestDaten/GA.txt");
    }

    BufferedReader eingabe;
    PrintWriter ausgabe;
    StringBuffer puffer;
    String zeile;
    boolean weiter;

    public XMLAnlisten(String eingabePfad, String ausgabePfad)
            throws Exception {
        System.out.println(eingabePfad);
        System.out.println(ausgabePfad);
        try {
            eingabe = new BufferedReader(new FileReader(eingabePfad));
            ausgabe = new PrintWriter(ausgabePfad);

            puffer = new StringBuffer();
            zeile = null;
            int xcount = 0;

            while ((zeile = eingabe.readLine()) != null & xcount < 10) {
                if (puffer.length() > 0)
                    puffer.append(" ");
                puffer.append(zeile.trim());
                System.out.println("Puffer=" + puffer);
                auswertenPuffer();
                xcount++;
            }

        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            if (eingabe != null) {
                eingabe.close();
            }
            if (ausgabe != null) {
                ausgabe.close();
            }
        }

    }

    private void auswertenPuffer() {

        int start, ende;

        weiter = true;
        while (weiter) {
            start = puffer.indexOf("<");
            protokollieren("Start=" + start);
            ende = puffer.indexOf(">");
            protokollieren("Ende=" + ende);
            protokollieren("Länge=" + puffer.length());
            protokollieren("Min=" + Math.min(ende + 1, puffer.length()));
            String zw = puffer.substring(0, Math.min(ende + 1, puffer.length()));
            protokollieren("zw=" + zw);
            ausgabe.println(puffer.substring(0, Math.min(ende + 1, puffer.length())));
            puffer.delete(start, Math.min(ende + 1, puffer.length()));
            System.out.println("puffer2=" + puffer);
            if (puffer.length() > 0) {
                weiter = true;
                System.out.println("länge=" + puffer.length());
            }
            else
                weiter = false;
        }
    }

    private void protokollieren(String string) {
        System.out.println(string);
    }
}
