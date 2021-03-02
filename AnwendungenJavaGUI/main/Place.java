package main;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * In dieser Klasse werden alle Informationen für eine Stelle verwaltet
 * 
 * @author Karl-Heinz Gödderz<br>
 *         Matr. 6513522
 *
 */
public class Place
        extends PetriNetNode {

    /**
     * Hier wird die Anzahl der Token abgespeichert, die dieser Stelle
     * zugeordnet sind
     */
    private int amtOfTokens;

    /**
     * Hier wird eine Liste aller Transitionen gespeichert, die der Stelle
     * vorgelagert sind
     */
    private List<Transition> inTransitions = new ArrayList<Transition>();

    /**
     * Hier wird eine Liste aller Transitionen gespeichert, die der Stelle
     * nachgelagert sind.
     */
    private List<Transition> outTransitions = new ArrayList<Transition>();

    /**
     * Mit diesem Konstruktor werden neben ID, Namen und Position auch eine
     * vorgegebene Anzahl Tokens abgespeichert
     * 
     * @param parameters
     *            Referenz auf die ParameterKlasse (wird zum zeichnen benötigt)
     * @param id
     *            ID der Stelle
     * @param name
     *            Name der Stelle
     * @param position
     *            Position der Transaktion in der Grafik
     * @param amtOfTokens
     *            Anzahl der dieser Stelle zugeordneten Token
     */
    public Place(PetriNetParameters parameters, String id, String name, Point position,
            @SuppressWarnings("hiding") int amtOfTokens) {
        super(parameters, id, name, position);
        /* Abspeichern der übergebenen Parameter */

        this.nodeID = id;
        this.nodeName = name;
        this.nodePosition = position;
        this.amtOfTokens = amtOfTokens;
    }

    /**
     * Mit diesem Konstruktor werden ID, Namen und Position abgespeichert und
     * gleichzeitig die Anzahl Tokens mit 0 initialisiert.
     * 
     * @param parameters
     *            Referenz auf die ParameterKlasse (wird zum zeichnen benötigt)
     * @param id
     *            ID der Stelle
     * @param name
     *            Name der Stelle
     * @param position
     *            Position der Stelle in der Grafik
     * 
     */
    public Place(PetriNetParameters parameters, String id, String name, Point position) {

        this(parameters, id, name, position, 0);
    }

    /**
     * Mit diesem Konstruktor wird die Stelle mit der ID angelegt und alle
     * Felder mit null-Werten initialisiert.
     *
     * @param parameters
     *            Referenz auf die ParameterKlasse (wird zum zeichnen benötigt)
     * @param id
     *            ID der Stelle
     *
     */
    public Place(PetriNetParameters parameters, String id) {

        this(parameters, id, " ", null, 0);
    }

    /**
     * Mit dieser Methode wird eine neue Eingangstransition zur Stelle
     * hinzugefügt
     * 
     * @param place
     *            Neue Eingangstransition
     */
    void addInTransition(Transition transition) {
        inTransitions.add(transition);
    }

    /**
     * Mit dieser Methode wird eine neue Ausgangstransition zur Stelle
     * hinzugefügt
     * 
     * @param Transition
     *            Neue Ausgangstransition
     */
    void addOutTransition(Transition transition) {
        outTransitions.add(transition);
    }

    /**
     * Diese Methode liefert die aktuelle Anzahl der Tokens auf der Stelle
     * 
     * @return amtOfTokens Anzahl der dieser Stelle zugeordneten Token
     */
    public int getAmtOfTokens() {
        return this.amtOfTokens;
    }

    /*
     * (non-Javadoc)
     * 
     * @see main.PetriNetNode#isPlace()
     */
    @Override
    public boolean isPlace() {
        return true;
    }

    /*
     * (non-Javadoc)
     * 
     * @see main.PetriNetNode#isTransaction()
     */
    @Override
    public boolean isTransition() {
        return false;
    }

    /**
     * Diese Methode zeichnet die Stelle auf dem Grafikelement
     * 
     * @param g2
     *            Grafikelement für die Zeichnung
     * @param parameters
     *            zu beachtende Grafikparameter
     */
    public void paintPetriNetComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        setPreferredSize(parameters.getPlaceSize());
        setMinimumSize(parameters.getPlaceSize());

        // System.out.println("Place " + nodeID);
        Point nodePos;
        Dimension nodeDim;
        int offset = parameters.getOffset();
        nodePos = getNodePosition();
        nodeDim = parameters.getPlaceSize();

        g2.setColor(parameters.getColorContour());
        g2.drawOval(nodePos.x, nodePos.y, nodeDim.width, nodeDim.height);
        g2.setColor(parameters.getColorNormal());
        g2.fillOval(nodePos.x + offset, nodePos.y + offset, nodeDim.width - offset, nodeDim.height
                - offset);

        if (amtOfTokens > 0) {
            g2.setColor(parameters.getColorToken());
            Dimension tokenDim = parameters.getTokenSize();
            if (amtOfTokens == 1) {
                g2.fillOval(nodePos.x + (nodeDim.width - tokenDim.width) / 2 + offset, nodePos.y
                        + (nodeDim.height - tokenDim.height) / 2 + offset, tokenDim.width - offset,
                        tokenDim.height - offset);
            }
            else {
                g2.drawString(String.valueOf(amtOfTokens), nodePos.x
                        + (nodeDim.width - tokenDim.width) / 2 + offset, nodePos.y
                        + (nodeDim.height - tokenDim.height) / 2 + offset);
            }

        }
    }

    /**
     * Diese Methode übernimmt die Anzahl der Token
     * 
     * @param amtOfTokens
     *            zu setzende Anzahl Token
     * 
     */
    public void setAmtOfTokens(@SuppressWarnings("hiding") int amtOfTokens) {
        this.amtOfTokens = amtOfTokens;
    }

    /**
     * Überschreibt Object.equals(), da die ID eindeutig ist
     * 
     * @param id
     *            zu prüfende ID
     * @return <tt>true</tt> wenn die IDs übereinstimmen <br>
     *         <tt>false</tt> sonst
     */
    // @Override
    // public boolean equals(Object id) {
    // return this.nodeID.equals(id);
    //
    // }

    @Override
    public String toString() {
        StringBuffer ausgabe = new StringBuffer("Stelle: " + nodeID).append(" \"").append(
                nodeName + "\"");
        ausgabe.append("\n mit " + amtOfTokens + " Token; Position: x=" + nodePosition.x + ", y="
                + nodePosition.y);
        ausgabe.append("\n Eingänge: ");
        for (Transition transition : inTransitions) {
            ausgabe.append(transition.getNodeID() + " ");
        }
        ausgabe.append("\n Ausgänge: ");
        for (Transition transition : outTransitions) {
            ausgabe.append(transition.getNodeID() + " ");
        }
        return ausgabe.toString();

    }

    /**
     * Diese Methode stellt fest, ob der übergebene Punkt innerhalb der Figur
     * liegt (mit einer Unschärfe von halo)
     * 
     * @param point
     *            der zu überprüfende Punkt
     * @param parameters
     *            zu beachtende Grafikparameter
     * @return <tt>true</tt> wenn der Punkt innerhalb des Bereiches liegt<br>
     *         <tt>false</tt> sonst
     * 
     */
    public boolean touchesPetriNetComponent(Point point, PetriNetParameters parameters) {
        // TODO Auto-generated method stub
        return false;
    }

}
