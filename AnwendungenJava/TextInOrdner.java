import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.file.FileSystems;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.NavigableSet;
import java.util.TreeSet;
import java.util.List;

/**
 * @author Karl-Heinz Gödderz Matr. 6513522
 *
 */
public class TextInOrdner implements FileVisitor<Path> {

	/**
	 * Aufruf der FileVisitor-Klasse
	 * 
	 * @param args
	 */
	public static void main(String[] args) throws IOException {
		/* Festlegen des Pfades */
		Path pfad = FileSystems.getDefault().getPath(
				// "/home/gitRepos/CAcert/cats");
				"/home/gitRepos/CAcert/CAcert-devel/"
//                "/home/gitRepos/TestRepository/Hilfsmodule/TestDaten/TestDir"
		);
		String suchbegriff = "mysql_";
		String[] ausschlussOrdner = { ".git", ".scripts" };
		String[] ausschlussDatei = { ".gitignore", "valid-xhtml11-blue" };
		String[] filter = { ".broken", ".c", ".cgi", ".css", ".html", ".js", ".php", ".pl", ".sample", ".sh", ".txt" };

		boolean anlisten = true;
		String protokoll = "/home/gukkdevel/rebased-mysql-statements";
		boolean diff = true;
		String diffFile = "/home/gukkdevel/rebased-mysql-statements-diff";

		/* Festlegen Durchsuchungroutine */
		TextInOrdner sucher = new TextInOrdner(suchbegriff, ausschlussOrdner, ausschlussDatei, filter, anlisten,
				protokoll, diff, diffFile);
		try {
			Files.walkFileTree(pfad, sucher);
		} catch (IOException e) {
			e.printStackTrace();
		}
		sucher.ausgebenHashtabelle();
		sucher.ausgebenListe();
	}

	/*
	 * Beginn des eigentlichen Programms
	 */
	int lvlDebug = 0;
	/**
	 * 
	 */
	String suchString = ""; // Suchtext
	String[] ausschlussOrdner; // Auszuschliessende Ordner
	String[] ausschlussDatei; // Auszuschliessende Datei
	String[] filter; // Zu verarbeitende Dateiendungen
	boolean anlisten; // Die Zeilen mit dem Suchbegriff sollen angelistet werden
	PrintWriter protokolldatei; // Protokolldatei
	boolean diff; //
	PrintWriter diffdatei; // Protokolldatei für automatischen Vergleich

	int einschub = 0; // Einschub
	boolean verarbeiten = true; // Es können komplette Ordner ausgeschlossen
								// werden
	int ignoreIndex = 0; // Index für Teilpfad eines ausgeschlossenen Ordners
	boolean analysieren = false;
	boolean nachDatei = false;

	Hashtable de = new Hashtable();
	TreeSet<String> schlagworte = new TreeSet<String>();

	ArrayList<Stelle> liste = new ArrayList<>();

	/**
	 * 
	 */
	public TextInOrdner(String text, String[] ausschlussOrdner, String[] ausschlussDatei, String[] filter,
			boolean anlisten, String protokoll, boolean diff, String diffFile) throws IOException {

		System.out.println("Ausgeschlossene Ordner: ");
		this.ausschlussOrdner = new String[ausschlussOrdner.length];
		for (int i = 0; i < ausschlussOrdner.length; i++) {
			this.ausschlussOrdner[i] = ausschlussOrdner[i];
			System.out.println("  " + this.ausschlussOrdner[i]);
		}
		System.out.println("\nAusgeschlossene Dateien: ");
		this.ausschlussDatei = new String[ausschlussDatei.length];
		for (int i = 0; i < ausschlussDatei.length; i++) {
			this.ausschlussDatei[i] = ausschlussDatei[i];
			System.out.println("  " + this.ausschlussDatei[i]);
		}
		System.out.println("\nUntersuchte Dateien:");
		this.filter = new String[filter.length];
		for (int i = 0; i < filter.length; i++) {
			this.filter[i] = filter[i];
			System.out.println("  " + this.filter[i]);
		}

		System.out.print("\nSuchbegriff: " + text);
		text = text.toUpperCase();
		System.out.println(" --->" + text + "<");
		this.suchString = text;
		System.out.println("Folgende Dateien enthalten den Suchbegriff:\n");

		this.anlisten = anlisten;
		if (anlisten) {
			System.out.println("alle Zeilen mit dem Suchbegriff werden angelistet");

			OutputStream os = new FileOutputStream(protokoll);
			protokolldatei = new PrintWriter(new OutputStreamWriter(os, "UTF-8"));

			System.out.println("Protokolldatei:  " + protokoll + "\n\n");
		}
		this.diff = diff;
		if (diff) {
			System.out.println("alle Zeilen mit dem Suchbegriff werden maschinenvergleichbar angelistet");

			OutputStream os2 = new FileOutputStream(diffFile);
			diffdatei = new PrintWriter(new OutputStreamWriter(os2, "UTF-8"));

			System.out.println("DIFF-datei:  " + diffFile + "\n\n");
		}
	}

	private void zeileDurchsuchen(String zeile) {
		boolean fertig = false;
		String zw;

		zw = zeile;
//        while (!fertig){
//            
//        }

	}

	@Override
	public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {

		boolean aktuellGesperrt = false;

		if (verarbeiten && nachDatei) {
			if (lvlDebug > 0) {
				System.out.println();
			}
		}
		if (einschub == 0) {
			if (lvlDebug > 0) {
				System.out.print("Beginne mit Verzeichnis\n" + dir + "\n\n");
			}
		} else {
			for (String name : ausschlussOrdner) {
				if (dir.getName(dir.getNameCount() - 1).toString().equals(name)) {
					ignoreIndex = dir.getNameCount() - 1;
					verarbeiten = false;
					aktuellGesperrt = true;
					if (lvlDebug > 0) {
						System.out.print(einschubGenerieren(einschub)

								+ "Ignoriere Unterverzeichnis Ebene: " + einschub + "\n" + einschubGenerieren(einschub)
								+ ">" + dir.getName(dir.getNameCount() - 1).toString() + "<" + "\n");
					}
				}
			}
			if (verarbeiten && !aktuellGesperrt) {
				if (lvlDebug > 0) {
					System.out.print(einschubGenerieren(einschub) + "Beginne mit Unterverzeichnis Ebene: " + einschub
							+ "\n" + einschubGenerieren(einschub + 1) + ">"
							+ dir.getName(dir.getNameCount() - 1).toString() + "<" + "\n");
				}
			}
		}

		einschub++;

		nachDatei = false;

		return FileVisitResult.CONTINUE;
	}

	@Override
	public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {

		if (verarbeiten) {
			if (lvlDebug > 0) {
				String name = file.getName(file.getNameCount() - 1).toString();
				System.out.println(name);
			}
			if (weiterbearbeiten(file)) {
				if (lvlDebug > 0) {
					System.out.println("--> weiterbearbeiten");
				}
				liste.add(new Stelle(file.toString()));
				if (suchtextGefunden(file)) {
					if (lvlDebug > 0) {
						System.out.println(file.toString() + " Suchtext gefunden");
					} else {
						System.out.println(file.toString());
					}
				}

				nachDatei = true;
			}
		}
		return FileVisitResult.CONTINUE;
	}

	@Override
	public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
		System.out.println("visitfilefailed "+file);
		// TODO Auto-generated method stub
		return FileVisitResult.CONTINUE;
	}

	@Override
	public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
		if (dir.getName(dir.getNameCount() - 1).toString().equals(".git")) {
			verarbeiten = true;
		}
		if (verarbeiten) {
			if (lvlDebug > 0) {
				System.out.print(einschubGenerieren(einschub) + "Beende Verzeichnis: >"
						+ dir.getName(dir.getNameCount() - 1).toString() + "<\n");
			}
		}
		einschub--;

		nachDatei = false;

		return FileVisitResult.CONTINUE;
	}

	/**
	 * Untersucht, ob die aktuelle Datei weiter untersucht werden soll
	 */
	protected boolean weiterbearbeiten(Path file) {
		String endung = "";
		String name = file.getName(file.getNameCount() - 1).toString();
		boolean abgelehnt = true;

		/* Ablehnen direkt */
		for (String nameDatei : ausschlussDatei) {
			if (nameDatei.equalsIgnoreCase(name)) {
				if (lvlDebug > 0) {
					System.out.println("ABLEHNEN");
				}
				return false;
			}
		}
		/*
		 * Bestimmen der Endung
		 */

		int positionPunkt = name.lastIndexOf(".");
		if (positionPunkt > 0) {
			endung = name.substring(positionPunkt);
			for (String filterElement : filter) {
				if (filterElement.equals(endung)) {
					zaehlenEndung(endung);
					abgelehnt = false;
					if (lvlDebug > 0) {
						System.out.println("+++++++++++++++++++");
					}
				}
			}
		} else {
			endung = name;
			abgelehnt = false;
			if (lvlDebug > 0) {
				System.out.println("----------------------");
			}
		}

		return !abgelehnt;
	}

	protected boolean suchtextGefunden(Path file) {
		String zeile;
		BufferedReader datei;
		boolean gefunden = false;
		Integer zeilenr = 0;
		String schlagwort = "";
		int anfang, laenge = 0;
		String zw;

		if (lvlDebug > 0)
			System.out.println("Start suchtextGefunden");

		try {
			datei = new BufferedReader(new FileReader(file.toString()));
			while ((zeile = datei.readLine()) != null) {

				zeilenr++;

				if (zeile.toUpperCase().contains(suchString)) {
					if (!gefunden) {
						gefunden = true;
						if (anlisten) {

							protokolldatei.println("\n\n" + file.toString() + "\n");
						}
					}
					if (gefunden) {
						if (anlisten) {
							protokolldatei.println(zeilenr + "->" + zeile);
						}
						if (diff) {
							liste.add(new Stelle(file.toString(),
									new Integer((zeilenr + 1000000)).toString().substring(1, 7), zeile));
						}
						zeileDurchsuchen(zeile);
					}
				}
			}

			datei.close();
			if (anlisten) {
				protokolldatei.flush();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (lvlDebug > 0)
			System.out.println("Ende suchtextGefunden, Returns:" + gefunden);
		return gefunden;
	}

	/**
	 * @param endung
	 */
	protected void zaehlenEndung(String endung) {
		/* Zählen der verarbeiteten Endungen */
		if (lvlDebug > 0) {
			Integer zw = (Integer) de.get(endung);
			if (zw != null) {
				de.put(endung, zw + 1);
			} else {
				de.put(endung, 1);
			}
		}
	}

	protected void ausgebenHashtabelle() {
		if (lvlDebug > 0) {
			System.out.println("\nAnzahl der einzelnen Endungen");
			Enumeration e = de.keys();
			while (e.hasMoreElements()) {
				String alias = (String) e.nextElement();
				System.out.println(alias + " Anzahl " + de.get(alias));
			}
		}
		if (anlisten) {
			System.out.println("\n");
			for (String schlagwort : schlagworte) {
				System.out.println(schlagwort);
			}
		}
	}

	/**
	 * 
	 */
	protected String einschubGenerieren(int einschub) {
		// String leerz = new Integer(einschub-1).toString();
		String leerz = "";
		for (int i = 1; i <= einschub; i++) {
			leerz += "  ";
		}
		return leerz;
	}

	protected void ausgebenListe() {
		if (diff) {
			String kopf = "";

//		System.out.println("Start Ausgabe Liste");
			Collections.sort(liste);

			for (Stelle stelle : liste) {
				if (!stelle.name.equals(kopf)) {
					diffdatei.println("\n" + stelle.name + "\n");
					kopf = stelle.name;
				} else {
					diffdatei.println(stelle.zeile + " -> " + stelle.inhalt);
				}
			}
			diffdatei.flush();
//			System.out.println("Ende Ausgabe Liste");
		}
	}

	private class Stelle implements Comparable<Stelle> {
		String sortierung;
		String name;
		String zeile;
		String inhalt;

		Stelle(String name) {
			this.name = name;
			this.zeile = "";
			this.inhalt = "";
			this.sortierung = name;
		}

		Stelle(String name, String zeile, String inhalt) {
			this.name = name;
			this.zeile = zeile;
			this.inhalt = inhalt;
			this.sortierung = name.concat(zeile);
		}

		@Override
		public int compareTo(Stelle argument) {
			return this.sortierung.compareTo(argument.sortierung);
		}
	}
}
