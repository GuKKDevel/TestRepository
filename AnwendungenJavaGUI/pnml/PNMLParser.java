package pnml;

import graphics.EMessageLevel;

import java.awt.Point;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Iterator;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.Characters;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

import main.*;

/**
 * Diese Klasse implementiert die Grundlage für einen einfachen PNML Parser.
 *
 * @author ?
 * @author Karl-Heinz Gödderz<br>
 *         Matr. 6513522
 *
 */
public class PNMLParser {

    /**
     * Referenz auf die ParameterKlasse
     */
    private PetriNetParameters parameters;

    /**
     * Dies ist eine Referenz zum Petrinetz
     */
    private MarkedNet markedNet;

    /**
     * Referenz auf den Controller
     */
    private EditorController controller;

    private StringBuffer logString = new StringBuffer();
    private EMessageLevel msglvl = EMessageLevel.LEER;

    /**
     * @return the msglvl
     */
    public EMessageLevel getMsglvl() {
        return msglvl;
    }

    /**
     * Dies ist eine Referenz zum Java Datei Objekt.
     */
    private File pnmlDatei;

    /**
     * Dies ist eine Referenz zum XML Parser. Diese Referenz wird durch die
     * Methode parse() initialisiert.
     */
    private XMLEventReader xmlParser = null;

    /**
     * Diese Variable dient als Zwischenspeicher für die ID des zuletzt
     * gefundenen Elements.
     */
    private String lastId = null;

    /**
     * Dieses Flag zeigt an, ob der Parser gerade innerhalb eines Token Elements
     * liest.
     */
    private boolean isToken = false;

    /**
     * Dieses Flag zeigt an, ob der Parser gerade innerhalb eines Name Elements
     * liest.
     */
    private boolean isName = false;

    /**
     * Dieses Flag zeigt an, ob der Parser gerade innerhalb eines Value Elements
     * liest.
     */
    private boolean isValue = false;

    /**
     * Dieser Konstruktor erstellt einen neuen Parser für PNML Dateien, dem die
     * PNML Datei als Java {@link File} übergeben wird.
     * 
     * @param pnml
     *            Java {@link File} Objekt der PNML Datei
     */
    public PNMLParser(PetriNetParameters parameters, final EditorController controller,
            final File pnml) {
        super();
        // Übernehmen der Parameter und anlegen eines neuen markierten Netzes
        this.parameters = parameters;
        this.pnmlDatei = pnml;
        this.markedNet = new MarkedNet(parameters);
        this.controller = controller;

        // Initialisieren Parser und Datei öffnen
        initParser();
        // weitermachen, wenn kein Fehler aufgetreten ist
        if (logString.length() == 0) {
            parse();
            if (logString.length() > 0) {
                controller.showLogDialog(logString, EMessageLevel.WARN);
            }
        }
    }

    /**
     * Diese Methode öffnet die PNML Datei als Eingabestrom und initialisiert
     * den XML Parser.
     */
    public final void initParser() {
        try {
            InputStream dateiEingabeStrom = new FileInputStream(pnmlDatei);
            XMLInputFactory factory = XMLInputFactory.newInstance();
            try {
                xmlParser = factory.createXMLEventReader(dateiEingabeStrom);

            }
            catch (XMLStreamException e) {
                logString.append("\nXML Verarbeitungsfehler: " + e.getMessage());
                System.err.println(logString);
                e.printStackTrace();
                controller.showLogDialog(logString, EMessageLevel.ERR);
                return;
            }
        }
        catch (FileNotFoundException e) {
            logString.append("\nDie Datei wurde nicht gefunden! " + e.getMessage());
            System.err.println(logString);
            controller.showLogDialog(logString, EMessageLevel.ERR);
            return;
        }
    }

    /**
     * Diese Methode liest die XML Datei und delegiert die gefundenen XML
     * Elemente an die entsprechenden Methoden.
     */
    public final void parse() {
        while (xmlParser.hasNext()) {
            try {
                XMLEvent event = xmlParser.nextEvent();
                switch (event.getEventType()) {
                    case XMLStreamConstants.START_ELEMENT:
                        handleStartEvent(event);
                        break;
                    case XMLStreamConstants.END_ELEMENT:
                        String name = event.asEndElement().getName().toString().toLowerCase();
                        if (name.equals("token")) {
                            isToken = false;
                        }
                        else if (name.equals("name")) {
                            isName = false;
                        }
                        else if (name.equals("value")) {
                            isValue = false;
                        }
                        break;
                    case XMLStreamConstants.CHARACTERS:
                        if (isValue && lastId != null) {
                            Characters ch = event.asCharacters();
                            if (!ch.isWhiteSpace()) {
                                handleValue(ch.getData());
                            }
                        }
                        break;
                    case XMLStreamConstants.END_DOCUMENT:
                        // untersuchen, ob das markierte Petrinetz konsistent
                        // ist
                        checkMarkedNet();
                        // schließe den Parser
                        xmlParser.close();
                        break;
                    default:
                }
            }
            catch (XMLStreamException e) {
                System.err.println("Fehler beim Parsen des PNML Dokuments. " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    /**
     * Überprüfen, ob Netz Konsistent
     */
    private void checkMarkedNet() {
        for (Transition transition : markedNet.getTransitions()) {
            if (transition.getNodePosition() == null) {
                // markedNet.removeTransition(transition);
                logString.append("\n" + "Transition mit ID " + transition.getNodeID()
                        + " wurde gelöscht, da keine Position vorhanden");
                msglvl = EMessageLevel.WARN;

            }
        }
    }

    /**
     * Diese Methode behandelt den Start neuer XML Elemente, in dem der Name des
     * Elements überprüft wird und dann die Behandlung an spezielle Methoden
     * delegiert wird.
     * 
     * @param event
     *            {@link XMLEvent}
     */
    private void handleStartEvent(final XMLEvent event) {
        StartElement element = event.asStartElement();
        if (element.getName().toString().toLowerCase().equals("transition")) {
            handleTransition(element);
        }
        else if (element.getName().toString().toLowerCase().equals("place")) {
            handlePlace(element);
        }
        else if (element.getName().toString().toLowerCase().equals("arc")) {
            handleArc(element);
        }
        else if (element.getName().toString().toLowerCase().equals("name")) {
            isName = true;
        }
        else if (element.getName().toString().toLowerCase().equals("position")) {
            handlePosition(element);
        }
        else if (element.getName().toString().toLowerCase().equals("token")) {
            isToken = true;
        }
        else if (element.getName().toString().toLowerCase().equals("value")) {
            isValue = true;
        }
    }

    /**
     * Diese Methode wird aufgerufen, wenn Text innerhalb eines Value Elements
     * gelesen wird.
     * 
     * @param value
     *            Der gelesene Text als String
     */
    private void handleValue(final String value) {
        if (isName) {
            setName(lastId, value);
        }
        else if (isToken) {
            setMarking(lastId, value);
        }
    }

    /**
     * Diese Methode wird aufgerufen, wenn ein Positionselement gelesen wird.
     * 
     * @param element
     *            das Positionselement
     */
    private void handlePosition(final StartElement element) {
        String x = null;
        String y = null;
        Iterator<?> attributes = element.getAttributes();
        while (attributes.hasNext()) {
            Attribute attr = (Attribute) attributes.next();
            if (attr.getName().toString().toLowerCase().equals("x")) {
                x = attr.getValue();
            }
            else if (attr.getName().toString().toLowerCase().equals("y")) {
                y = attr.getValue();
            }
        }
        if (x != null && y != null && lastId != null) {
            setPosition(lastId, x, y);
        }
        else {
            logString.append("\n" + "Unvollständige Position wurde verworfen!");
            msglvl = EMessageLevel.WARN;
        }
    }

    /**
     * Diese Methode wird aufgerufen, wenn ein Transitionselement gelesen wird.
     * 
     * @param element
     *            das Transitionselement
     */
    private void handleTransition(final StartElement element) {
        String transitionId = null;
        Iterator<?> attributes = element.getAttributes();
        while (attributes.hasNext()) {
            Attribute attr = (Attribute) attributes.next();
            if (attr.getName().toString().toLowerCase().equals("id")) {
                transitionId = attr.getValue();
                break;
            }
        }
        if (transitionId != null) {
            newTransition(transitionId);
            lastId = transitionId;
        }
        else {
            logString.append("\n" + "Transition ohne id wurde verworfen!");
            msglvl = EMessageLevel.WARN;
            lastId = null;
        }
    }

    /**
     * Diese Methode wird aufgerufen, wenn ein Stellenelement gelesen wird.
     * 
     * @param element
     *            das Stellenelement
     */
    private void handlePlace(final StartElement element) {
        String placeId = null;
        Iterator<?> attributes = element.getAttributes();
        while (attributes.hasNext()) {
            Attribute attr = (Attribute) attributes.next();
            if (attr.getName().toString().toLowerCase().equals("id")) {
                placeId = attr.getValue();
                break;
            }
        }
        if (placeId != null) {
            newPlace(placeId);
            lastId = placeId;
        }
        else {
            logString.append("\n" + "Stelle ohne id wurde verworfen!");
            msglvl = EMessageLevel.WARN;
            lastId = null;
        }
    }

    /**
     * Diese Methode wird aufgerufen, wenn ein Kantenelement gelesen wird.
     * 
     * @param element
     *            das Kantenelement
     */
    private void handleArc(final StartElement element) {
        String arcId = null;
        String source = null;
        String target = null;
        Iterator<?> attributes = element.getAttributes();
        while (attributes.hasNext()) {
            Attribute attr = (Attribute) attributes.next();
            if (attr.getName().toString().toLowerCase().equals("id")) {
                arcId = attr.getValue();
            }
            else if (attr.getName().toString().toLowerCase().equals("source")) {
                source = attr.getValue();
            }
            else if (attr.getName().toString().toLowerCase().equals("target")) {
                target = attr.getValue();
            }
        }
        if (arcId != null && source != null && target != null) {
            newArc(arcId, source, target);
        }
        else {
            logString.append("\n" + "Unvollständige Kante wurde verworfen!");
            msglvl = EMessageLevel.WARN;
        }
        // Die id von Kanten wird nicht gebraucht
        lastId = null;
    }

    /**
     * Diese Methode kann überschrieben werden, um geladene Transitionen zu
     * erstellen.
     * 
     * @param id
     *            Identifikationstext der Transition
     */
    public void newTransition(final String id) {
        // System.out.println("Transition mit id " + id + " wurde gefunden.");
        if (markedNet.existsNodeID(id) || existsArcStringID(id)) {
            logString.append("\n" + "Transition mit ID " + id
                    + " wurde verworfen, da ID bereits existiert");
        }
        else {
            markedNet.addTransition(new Transition(parameters, id));
            // System.out.println(markedNet.getTransitionByID(id));
        }
    }

    /**
     * Diese Methode kann überschrieben werden, um geladene Stellen zu
     * erstellen.
     * 
     * @param id
     *            Identifikationstext der Stelle
     */
    public void newPlace(final String id) {
        if (markedNet.existsNodeID(id) || existsArcStringID(id)) {
            logString.append("\n" + "Stelle mit ID " + id
                    + " wurde verworfen, da ID bereits existiert");
        }
        else {
            markedNet.addPlace(new Place(parameters, id));
            // System.out.println("Stelle mit id " + id + " wurde gefunden.");
        }
    }

    /**
     * Diese Methode kann überschrieben werden, um geladene Kanten zu erstellen.
     * 
     * @param id
     *            Identifikationstext der Kante
     * @param source
     *            Identifikationstext des Startelements der Kante
     * @param target
     *            Identifikationstext des Endelements der Kante
     */
    public void newArc(final String id, final String source, final String target) {
        // ID muss eindeutig sein
        if (markedNet.existsNodeID(id) || existsArcStringID(id)) {
            logString.append("\n" + "Verbindung mit ID " + id
                    + " wurde verworfen, da ID bereits existiert");
        }
        // Verbindung wird provisorisch eingefügt. Nachbearbeitung in Methode
        // checkMarkedNet
        else {
            arcStrings.add(new ArcString(id, source, target));
            // System.out.println("Kante mit id " + id + " von " + source
            // + " nach " + target + " wurde gefunden.");
        }
    }

    /**
     * Diese Methode kann überschrieben werden, um die Positionen der geladenen
     * Elemente zu aktualisieren.
     * 
     * @param id
     *            Identifikationstext des Elements
     * @param x
     *            x Position des Elements
     * @param y
     *            y Position des Elements
     */
    public void setPosition(final String id, final String x, final String y) {
        // Felder für die Konvertierung
        int px = 0;
        int py = 0;
        // Fehlerschalter, unterbinden von Folgeaktionen
        boolean fehler = false;
        // Knoten der zur ID gehört
        PetriNetNode node;

        // Konvertieren der Positionsangaben
        try {
            px = Integer.parseInt(x);
            py = Integer.parseInt(y);
        }
        catch (Exception e) {
            logString.append("\n" + "Position für Knoten mit ID " + id
                    + " wurde verworfen, da Positionswerte nicht zulässig");
            fehler = true;
        }
        if (!fehler) {
            node = markedNet.getNodeByID(id);
            if (node == null) {
                logString.append("\n" + "Position für Knoten mit ID " + id
                        + " wurde verworfen, da Knoten nicht definiert");
                fehler = true;
            }
            else {
                markedNet.setNodePosition(node, new Point(px, py));
                System.out.println("Setze die Position des Elements " + id + " auf (" + x + ", "
                        + y + ")");

            }
        }
    }

    /**
     * Diese Methode kann überschrieben werden, um den Beschriftungstext der
     * geladenen Elemente zu aktualisieren.
     * 
     * @param id
     *            Identifikationstext des Elements
     * @param name
     *            Beschriftungstext des Elements
     */
    public void setName(final String id, final String name) {
        System.out.println("Setze den Namen des Elements " + id + " auf " + name);
    }

    /**
     * Diese Methode kann überschrieben werden, um die Markierung der geladenen
     * Elemente zu aktualisieren.
     * 
     * @param id
     *            Identifikationstext des Elements
     * @param marking
     *            Markierung des Elements
     */
    public void setMarking(final String id, final String marking) {
        // Feld für die Konvertierung
        int m = 0;
        // Fehlerschalter, unterbinden von Folgeaktionen
        boolean fehler = false;
        // Knoten der zur ID gehört
        PetriNetNode node;

        // Konvertieren des Markierungswertes
        try {
            m = Integer.parseInt(marking);
        }
        catch (Exception e) {
            logString.append("\n" + "Markierung für Knoten mit ID " + id
                    + " wurde verworfen, da kein gültiger Wert");
            fehler = true;
        }
        // Fehlerfreie Konvertierung
        if (!fehler) {
            // Bestimmen Knoten für ID
            node = markedNet.getNodeByID(id);

            // Knoten nicht gefunden
            if (node == null) {
                logString.append("\n" + "Markierung für Knoten mit ID " + id
                        + " wurde verworfen, da Knoten nicht definiert");
                fehler = true;
            }
            // Knoten ist keine Stelle
            else if (!node.isPlace()) {
                logString.append("\n" + "Markierung für Stelle mit ID " + id
                        + " wurde verworfen, da Stelle nicht definiert");
                fehler = true;
            }
            // Knoten ist eine Stelle, Markierung übertragen
            else {
                ((Place) node).setAmtOfTokens(m);
                // System.out.println("Setze die Markierung des Elements " + id
                // + " auf " + marking);
            }
        }
    }

    /**
     * Diese Methode gibt ein markiertes Netz zurück
     * 
     * @return ein markiertes Netz
     */
    public MarkedNet getMarkedNet() {
        return markedNet;
    }

    /**
     * Diese Klasse wird benötigt, da nicht gewährleistet ist, dass die
     * Verbindungen erst kommen, wenn alle Knoten bereits existieren
     */
    class ArcString {

        String arcID;
        String sourceID;
        String targetID;

        ArcString(String arc, String source, String target) {
            arcID = arc;
            sourceID = source;
            targetID = target;
        }

    }

    ArrayList<ArcString> arcStrings = new ArrayList<ArcString>();

    boolean existsArcStringID(String id) {
        for (ArcString arcString : arcStrings) {
            if (arcString.arcID.equals(id)) {
                return true;
            }
        }
        return false;
    }
}
