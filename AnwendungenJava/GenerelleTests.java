
/**
 * 
 */
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;

/**
 * @author GuKKDevel
 *
 */
public class GenerelleTests {
	/**
	 * 
	 */

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		GenerelleTests test1 = new GenerelleTests();
//		test1.ausgebenSystem();
//		System.out.println(test1.osIstLinux());
//		System.out.println(test1.bestimmenDateistamm("Pfad/zur/Datei"));
		test1.lesenDatei("iCalender/temp.ics");
	}

	/**
	 * @param string
	 */
	private void lesenDatei(String dateiName) {
		String zeile;
		File eingabeDatei = null;
		FileInputStream eingabeStrom = null;
		InputStreamReader eingabeStromLeser = null;
		BufferedReader eingabeStromPuffer = null;
		Scanner eingabeScanner = null;

		try {
			eingabeDatei = new File(bestimmenDateistamm(dateiName));
			eingabeStrom = new FileInputStream(eingabeDatei);
			eingabeStromLeser = new InputStreamReader(eingabeStrom, "UTF-8");
			eingabeStromPuffer = new BufferedReader(eingabeStromLeser);
			eingabeScanner = new Scanner(eingabeStromPuffer);

			zeile = eingabeStromPuffer.readLine();
			while (eingabeScanner.hasNextLine()) {
				zeile = eingabeScanner.nextLine();
				System.out.println(zeile);
			}
			
		} catch (FileNotFoundException e) {
			System.out.println("Datei nicht gefunden");
		} catch (IOException e) {
			e.printStackTrace();

		} finally {

		}
	}

	/**
	 * @param string
	 * @return
	 */
	private String bestimmenDateistamm(String pfad) {

		if (osIstLinux()) {
			return "/home/programmieren/TestFiles/" + pfad;
		} else {
			return "C:\\Users\\GuKKDevel\\Desktop\\Workspace\\Testdaten\\" + pfad.replace("/", "\\");
		}
	}

	/**
	 * Feststellen ob unter Linux oder Windows gearbeitet wird
	 */
	private boolean osIstLinux() {
		return System.getProperty("os.name").equals("Linux");
	}

	/**
	 * Alle Properties der System-Klasse ausgeben
	 */
	private void ausgebenSystem() {
		String txt = System.getProperties().toString();
		while (txt.indexOf(",") > 0) {
			System.out.println(txt.substring(0, txt.indexOf(",") + 1));
			txt = txt.substring(txt.indexOf(",") + 1);

		}
		System.out.println(txt);
	}
}
