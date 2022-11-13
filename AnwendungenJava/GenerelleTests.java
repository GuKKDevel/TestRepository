
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
import java.net.InetAddress;
import java.util.Scanner;
import java.util.logging.ConsoleHandler;
import java.util.logging.Formatter;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

/**
 * @author GuKKDevel
 *
 */
public class GenerelleTests {
	/**
	 * 
	 */
	Logger logger = Logger.getLogger("AnwendungenJava");

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		GenerelleTests test1 = new GenerelleTests();
		test1.loggerEinrichten();
		test1.ausgebenSystem();
//		System.out.println(test1.osIstLinux());
//		System.out.println(test1.bestimmenDateistamm("/Testdaten/iCalender/temp0.ics"));
//		test1.lesenDatei("/Testdaten/iCalender/temp.ics");
	}

	/**
	 * 
	 * Einrichten des Loggingsystems
	 * 
	 * Der Logger muss in allen Klassen definiert werden,
	 * 
	 * 
	 * 
	 * 
	 */
	private void loggerEinrichten() {
		logger.setLevel(Level.INFO);
//		Handler handler = new FileHandler("/home/programmieren/TestFiles/iCalender/temp.log");
		Handler handler = new ConsoleHandler();
		handler.setLevel(Level.FINEST);
		handler.setFormatter(new Formatter() {
			@Override
			public String format(LogRecord record) {
				return record.getSourceClassName() + "." + record.getSourceMethodName() + ": " + record.getMessage()
						+ "\n";
			}
		});
		logger.addHandler(handler);
		logger.setUseParentHandlers(false);
		logger.finest("begonnen");
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
			logger.info("Datei nicht gefunden");
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
			return "/home/programmieren/" + pfad.replace("\\", "/");
		} else {
			return System.getProperty("user.home") + "\\Desktop\\Workspace\\" + pfad.replace("/", "\\");
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
		try {
			System.out.println("Rechner: " + InetAddress.getLocalHost().getHostName());
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println();
		String txt = System.getProperties().toString();
		while (txt.indexOf(",") > 0) {
			System.out.println(txt.substring(0, txt.indexOf(",") + 1));
			txt = txt.substring(txt.indexOf(",") + 2);

		}
		System.out.println(txt);
	}
}
