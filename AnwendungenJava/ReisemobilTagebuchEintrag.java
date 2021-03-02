import java.util.GregorianCalendar;

public class ReisemobilTagebuchEintrag {

    int kennung;

    String kmStand;
    String ort;
    String land;
    String datum;
    String uhrzeit;
    String zeitmarke;
    String autor;
    String text;

    public String toString() {
        String zwKennung;
        if (kennung < 10)
            zwKennung = "000" + kennung;
        else
            if (kennung < 100)
                zwKennung = "00" + kennung;
            else
                if (kennung < 1000)
                    zwKennung = "0" + kennung;
                else
                    zwKennung = "" + kennung;
        return "Eintrag: " + zwKennung + " km-Stand: "
                + kmStand + " Land: "+ land +" Ort: "
                + ort                 + " Datum: " + datum + " Uhrzeit: " + uhrzeit //+ " Zeitmarke: " + zeitmarke
                + " Autor: " + autor + " Text: " + text;

    }
}
