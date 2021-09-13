package main;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * In dieser Klasse werden alle Informationen für eine Transition verwaltet
 * 
 * @author Karl-Heinz Gödderz <br>
 *         Matr. 6513522
 *
 */
public class Transition
        extends PetriNetNode {

    /**
     * Hier wird eine Liste aller Stellen gespeichert, die der Transition
     * vorgelagert sind
     */
    private List<Place> inPlaces = new ArrayList<Place>();

    /**
     * Hier wird eine Liste aller Stellen gespeichert, die der Transition
     * nachgelagert sind.
     */
    private List<Place> outPlaces = new ArrayList<Place>();

    /**
     * Mit diesem Konstruktor werden ID, Name und Position der Transition
     * abgespeichert
     * 
     * @param id
     *            ID der Transition
     * @param name
     *            Name der Transition
     * @param position
     *            Position der Transition in der Grafik
     */
    public Transition(PetriNetParameters parameters, String id, String name, Point position) {
        /* Abspeichern der Parameter */
        super(parameters, id, name, position);
    }

    /**
     * Mit diesem Konstruktor wird nur die Transition mit der ID angelegt und
     * abgespeichert und alle Felder mit null-Werten initialisiert.
     * 
     * @param id
     *            ID der Transition
     */
    public Transition(PetriNetParameters parameters, String id) {
        /* Abspeichern der Parameter */
        this(parameters, id, " ", null);
    }

    /**
     * Mit dieser Methode wird eine neue Eingangsstelle zur Transition
     * hinzugefügt
     * 
     * @param place
     *            Neue Eingangsstelle
     */
    void addInPlace(Place place) {
        inPlaces.add(place);
    }

    /**
     * Mit dieser Methode wird eine neue Ausgangsstelle zur Transition
     * hinzugefügt
     * 
     * @param place
     *            Neue Ausgangsstelle
     */
    void addOutPlace(Place place) {
        outPlaces.add(place);
    }

    /**
     * Diese Methode zeigt an, ob die Transition aktivierbar ist, d.h. alle
     * Eingangsstellen mit mindestens einem Token belegt sind oder keine
     * Eingangsstellen existieren
     * 
     * @return false: wenn mindestens eine der Eingangsstellen keinen Token hat <br>
     *         true: wenn alle Eingangsstellen mindestens ein Token haben oder
     *         keine Eingangsstelle existiert
     */
    public boolean isEnabled() {

        if (inPlaces.size() == 0) {
            return true;
        }
        for (Place place : inPlaces) {
            if (place.getAmtOfTokens() == 0)
                return false;
        }
        return true;
    }

    /*
     * (non-Javadoc)
     * 
     * @see main.PetriNetNode#isPlace()
     */
    @Override
    public boolean isPlace() {
        return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see main.PetriNetNode#isTransaction()
     */
    @Override
    public boolean isTransition() {
        return true;
    }

    /*
     * (non-Javadoc)
     * 
     * @see main.PetriNetComponent#paintNode(java.awt.Graphics2D)
     */
    @Override
    public void paintPetriNetComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        // System.out.println("Transition "+nodeID);
        Point nodePos;
        Dimension nodeDim;
        int offset = parameters.getOffset();
        int scaleX;
        int scaleY;
        int scaleWidth;
        int scaleHeight;

        nodePos = getNodePosition();
        nodeDim = parameters.getTransitionSize();
        scaleX = nodePos.x + nodeDim.width / 2;
        scaleY = nodePos.y + nodeDim.height / 2;
        scaleWidth = nodeDim.width;
        scaleHeight = nodeDim.height;

        g2.setColor(parameters.getColorContour());
        g2.drawRect(nodePos.x, nodePos.y, nodeDim.width, nodeDim.height);
        g2.setColor(isEnabled() ? parameters.getColorActive() : parameters.getColorInactive());

        g2.fillRect(nodePos.x + offset, nodePos.y + offset, nodeDim.width - offset, nodeDim.height
                - offset);
        g2.setColor(parameters.getColorContour());

    }

    @Override
    public String toString() {
        StringBuilder ausgabe = new StringBuilder();
        ausgabe.append("Transition: " + nodeID).append(" \"").append(nodeName + "\"");
        ausgabe.append("\n").append(
                " aktivierbar=" + isEnabled() + "; Position: x=" + nodePosition.x + ", y="
                        + nodePosition.y);
        ausgabe.append("\n Eingänge: ");
        for (Place place : inPlaces) {
            ausgabe.append(place.getNodeID() + " ");
        }
        ausgabe.append("\n Ausgänge: ");
        for (Place place : outPlaces) {
            ausgabe.append(place.getNodeID() + " ");
        }

        return ausgabe.toString();
    }

    /*
     * (non-Javadoc)
     * 
     * @see main.PetriNetComponent#touchesNode(java.awt.Point, int)
     */
    @Override
    public boolean touchesPetriNetComponent(Point point, PetriNetParameters parameters) {
        return touchesTransition(point, parameters);
    }

    /**
     * Diese Methode stellt fest, ob der übergebene Punkt auf den Knoten zeigt,
     * dabei wird eine ungenauigkeit von halo Pixel beücksichtigt
     * 
     * @param point
     *            der zu überprüfende Punkt
     * @param parameters
     *            zu beachtende Grafikparameter
     * 
     * @return <tt>true</tt> wenn der Punkt in dem Bereich liegt <br>
     *         <tt>false</tt> sonst
     */
    boolean touchesTransition(Point point, PetriNetParameters parameters) {
        return false;
    }

}