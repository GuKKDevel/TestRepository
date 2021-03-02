package main;

import java.awt.Dimension;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

/**
 * In dieser Klasse werden alle Informationen für ein komplettes Netz verwaltet
 * inklusive der Markierungen
 * 
 * @author Karl-Heinz Gödderz<br>
 *         Matr. 6513522
 *
 */
public class MarkedNet {

    /**
     * Referenz auf die ParameterKlasse
     */

    private PetriNetParameters parameters;

    /**
     * In dieser Liste werden die Referenzen für die Transaktionen des Netzes
     * gespeichert
     */
    private List<Transition> transitions = new ArrayList<Transition>();

    /**
     * In dieser Liste werden die Referenzen für die Stellen des Netzes
     * gespeichert
     */
    private List<Place> places = new ArrayList<Place>();

    /**
     * In dieser Liste werden Referenzen für die Verbindungspfeile gespeichert
     */
    private List<Arc> arcs = new ArrayList<Arc>();

    /**
     * Hier wird die nächste Nummer für einen Knoten gespeichert
     */
    private int valueNextPlaceID = 1;
    private int valueNextTransitionID = 1;
    private int valueNextArcID = 1;

    /**
     * Hier wird die minimale Größe für die Zeichenfläche gespeichert
     */
    private Dimension minPanelSize = new Dimension(1000, 1000);

    /**
     * Dieser Konstruktor speichert die Referenz der ParameterKlasse
     * 
     * @param parameters
     *            die ParameterKlasse
     */
    public MarkedNet(PetriNetParameters parameters) {
        super();
        this.parameters = parameters;
    }

    /**
     * Mit dieser Methode wird der Markierung eine neue Verbindung mit Richtung
     * Stelle -> Transition hinzugefügt und der Erfolg zurückgemeldet
     * 
     * @param place
     *            Hinzuzufügende Stelle
     * @param transition
     *            Hinzuzufügende Transaktion
     * 
     * 
     * @return <tt>true</tt> wenn Operation möglich<br>
     *         <tt>false</tt> wenn Stelle oder Transition nicht existiert
     */
    public boolean addArc(String id, Place place, Transition transition) {
        if (existsNode(place) && existsNode(transition)) {
            place.addOutTransition(transition);
            transition.addInPlace(place);
            arcs.add(new Arc(parameters, id, place, transition));
            return true;
        }
        return false;

    }

    /**
     * Mit dieser Methode wird der Markierung eine neue Verbindung mit Richtung
     * Transition -> Stelle hinzugefügt und der Erfolg zurückgemeldet
     * 
     * @param transition
     *            Hinzuzufügende Transaktion
     * @param place
     *            Hinzuzufügende Stelle
     * 
     * @return <tt>true</tt> wenn Operation möglich<br>
     *         <tt>false</tt> wenn Stelle oder Transition nicht existiert
     */
    public boolean addArc(String id, Transition transition, Place place) {
        if (existsNode(transition) && existsNode(place)) {
            place.addInTransition(transition);
            transition.addOutPlace(place);
            arcs.add(new Arc(parameters, id, transition, place));
            return true;
        }
        return false;
    }

    /**
     * Mit dieser Methode wird der Markierung eine neue Stelle hinzugefügt
     * 
     * @param place
     *            Hinzuzufügende Stelle
     * 
     * @return true wenn Stelle eingefügt wurde,<br>
     *         false wenn Stelle schon existiert
     */
    public void addPlace(Place place) {
        if (!existsPlace(place)) {
            places.add(place);
        }
    }

    /**
     * Mit dieser Methode wird der Markierung eine neue Transaktion hinzugefügt
     * 
     * @param transition
     *            Hinzuzufügende Transaktion
     * 
     * @return true wenn Transaktion eingefügt wurde,<br>
     *         false wenn Transaktion schon existiert
     */
    public void addTransition(Transition transition) {
        if (!existsTransition(transition)) {
            transitions.add(transition);
            if (transition.getNodePosition() != null) {
                calculatePanelSize(transition);
            }
        }
    }

    /**
     * berechnet aus den Knotenpositionen und den Elementgrößen die minimale
     * Größe für die Zeichenfläche<br>
     * muss jedesmal aufgerufen werden, wenn ein neuer Knoten dazukommt
     * 
     * @param node
     *            der hinzugekommene Knoten
     */
    private void calculatePanelSize(PetriNetNode node) {
        int nodeW = Math.max(parameters.getPlaceSize().width, parameters.getTransitionSize().width);
        int nodeH = Math.max(parameters.getPlaceSize().height,
                parameters.getTransitionSize().height);

        int minW = node.getNodePosition().x + nodeW;
        int minH = node.getNodePosition().y + nodeH;

        minPanelSize.width = Math.max(minW, minPanelSize.width);
        minPanelSize.height = Math.max(nodeH, minPanelSize.height);
    }

    /**
     * diese Methode stellt fest, ob die ID für die Verbindung schon vergeben
     * ist
     * 
     * @param ID
     *            ID für die Verbindung
     * @return <tt>true</tt> wenn bereits eine Verbindung mit der ID existiert<br>
     *         <tt>false</tt> sonst
     */
    private boolean existsArcID(String id) {
        // TODO Auto-generated method stub
        return false;
    }

    /**
     * diese Methode stellt fest, ob die ID schon vergeben ist
     * 
     * @param ID
     *            ID für Petrinetz-Element
     * @return <tt>true</tt> wenn bereits ein Element mit der ID existiert<br>
     *         <tt>false</tt> sonst
     */
    public boolean existsID(String id) {
        return existsNodeID(id) || existsArcID(id);
    }

    /**
     * Methode gibt an, ob der Knoten (Stelle oder Transaktion) existiert
     * 
     * @param node
     *            Knoten der gesucht wird
     * @return true wenn der Knoten existiert<br>
     *         false wenn nicht
     */
    public boolean existsNode(PetriNetNode node) {
        if (node.isPlace()) {
            return existsPlace(node);
        }
        else if (node.isTransition()) {
            return existsTransition(node);
        }
        else {
            return false;
        }
    }

    /**
     * diese Methode stellt fest, ob die ID schon vergeben ist
     * 
     * @param nodeID
     *            ID für einen Knoten (Transition/Stelle)
     * @return <tt>true</tt> wenn bereits ein Knoten mit der ID existiert<br>
     *         <tt>false</tt> sonst
     */
    public boolean existsNodeID(String nodeID) {
        return existsPlaceID(nodeID) || existsTransitionID(nodeID);
    }

    /**
     * Die Methode gibt an, ob die Stelle existiert
     * 
     * @param place
     *            die zu überprüfende Stelle
     * @return <tt>true</tt> wenn die Stelle existiert<br>
     *         <tt>false</tt> wenn nicht
     */
    public boolean existsPlace(PetriNetNode place) {
        return places.contains(place);
    }

    /**
     * Die Methode gibt an, ob diese ID für eine Stelle bereits existiert
     * 
     * @param placeID
     *            der ID-String für die Stelle
     * 
     * @return <tt>true</tt> wenn StellenID schon vorhanden<br>
     *         <tt>false</tt> wenn nicht
     */
    public boolean existsPlaceID(String placeID) {
        return getPlaceByID(placeID) != null;
    }

    /**
     * Die Methode gibt an, ob die Transaktion existiert
     * 
     * @param transition
     *            die zu überprüfende Transaktion
     * @return <tt>true</tt> wenn die Transaktion existiert<br>
     *         <tt>false</tt> wenn nicht
     * 
     */
    public boolean existsTransition(PetriNetNode transition) {
        return transitions.contains(transition);
    }

    /**
     * Die Methode gibt an, ob diese ID für eine Transaktion bereits existiert
     * 
     * @param id
     *            der ID-String für die Transaktion
     * 
     * @return <tt>true</tt> wenn TransaktionsID schon vorhanden<br>
     *         <tt>false</tt> wenn nicht
     */
    public boolean existsTransitionID(String id) {
        for (Transition transition : transitions) {
            if (transition.getNodeID().equals(id)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Ermittelt ob es eine Verbindung mit der übergebenen ID gibt und gibt dies
     * ggf. zurück
     * 
     * @param id
     *            Identifikationsstring für die Verbindung
     * 
     * @return die Referenz des Knotens oder <tt>null</tt> wenn er nicht
     *         existiert
     */
    public Arc getArcByID(String id) {
        Arc arcReturn = null;
        for (Arc arc : arcs) {
            if (arc.getArcID().equals(id)) {
                return arcReturn = arc;
            }
        }
        return arcReturn;
    }

    /**
     * @return alle Verbindungen des Petrinetzes
     */
    public Arc[] getArcs() {
        return this.arcs.toArray(new Arc[arcs.size()]);
    }

    /**
     * Ermittelt ob es einen Knoten mit der übergebenen ID gibt und gibt dies
     * ggf. zurück
     * 
     * @param nodeID
     *            Identifikationsstring für die Stelle
     * 
     * @return die Referenz des Knotens oder <tt>null</tt> wenn er nicht
     *         existiert
     */
    public PetriNetNode getNodeByID(String id) {
        PetriNetNode node = getTransitionByID(id);
        if (node == null) {
            node = getPlaceByID(id);
        }
        return node;
    }

    /**
     * Ermittelt ob es eine Stelle mit der übergebenen ID gibt und gibt dies
     * ggf. zurück
     * 
     * @param placeID
     *            Identifikationsstring für die Stelle
     * 
     * @return die Referenz der Stelle oder <tt>null</tt> wenn sie nicht
     *         existiert
     */
    public Place getPlaceByID(String nodeID) {
        Place placeReturn = null;
        for (Place place : places) {
            if (place.getNodeID().equals(nodeID)) {
                return placeReturn = place;
            }
        }
        return placeReturn;
    }

    /**
     * @return alle Stellen des Petrinetzes
     */
    public Place[] getPlaces() {
        return this.places.toArray(new Place[places.size()]);
    }

    /**
     * Ermittelt ob es eine Transition mit der übergebenen ID gibt und gibt dies
     * ggf. zurück
     * 
     * @param transitionID
     *            Identifikationsstring für die Transition
     * 
     * @return die Referenz der Transition oder <tt>null</tt> wenn sie nicht
     *         existiert
     */
    public Transition getTransitionByID(String nodeID) {
        Transition transitionReturn = null;
        for (Transition transition : transitions) {
            if (transition.getNodeID().equals(nodeID)) {
                return transitionReturn = transition;
            }
        }
        return transitionReturn;
    }

    /**
     * @return alle Transitionen des Petrinetzes
     */
    public Transition[] getTransitions() {
        return this.transitions.toArray(new Transition[transitions.size()]);
    }

    public void setNodePositionByID(String id, Point position) {
        PetriNetNode node = getNodeByID(id);
        if (node.isPlace()) {
            // place.set
        }
        // return false;
    }

    /**
     * Mit dieser Methode wird eine Referenz auf die ParameterKlasse übergeben
     * 
     * @param parameters
     *            ParameterKlasse
     */
    public void setParameters(PetriNetParameters parameters) {
        this.parameters = parameters;
    }

    /**
     * trägt die Position der Stelle ein
     * 
     * @param node
     *            Stelle
     * @param point
     *            Position
     */
    public void setPlacePosition(Place node, Point point) {
        node.setNodePosition(point);
    }

    /**
     * trägt die Position der Transition ein
     * 
     * @param node
     *            Transition
     * @param point
     *            Position
     */
    public void setTransitionPosition(Transition node, Point point) {
        node.setNodePosition(point);
    }

    @Override
    public String toString() {
        StringBuilder ausgabe = new StringBuilder();
        ausgabe.append("Transitionen:\n");
        for (Transition transition : transitions) {
            ausgabe.append(transition.getNodeID() + " ### " + transition.getNodeName() + "\n");
        }
        ausgabe.append("\nStellen:\n");

        for (Place place : places) {
            ausgabe.append(place.getNodeID() + " " + place.getAmtOfTokens() + " ### "
                    + place.getNodeName() + "\n");
        }
        ausgabe.append("\nPfeile:\n");
        for (Arc arc : arcs) {
            ausgabe.append(arc.getStartNode().getNodeID() + " " + arc.getEndNode().getNodeID()
                    + " ### " + "\n ");
        }
        return ausgabe.toString();
    }

    /**
     * Mit dieser Methode wird einem übergebenen Knoten eine neue Position
     * zugewiesen
     * 
     * @param node
     *            zu bearbeitender Knoten
     * @param point
     *            zuzuweisende Position
     */
    public void setNodePosition(PetriNetNode node, Point point) {
        node.setNodePosition(point);

    }

}
