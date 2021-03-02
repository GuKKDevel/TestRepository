package main;

import java.awt.Point;

import javax.swing.JComponent;

/**
 * Dieses Interface fasst die Knoten in einer gemeinsamen Klasse zusammen
 * 
 * @author Karl-Heinz Gödderz Matr. 6513522
 *
 */
public abstract class PetriNetNode
        extends JComponent
        implements PetriNetComponent {

    /**
     * Referenz auf die ParameterKlasse
     */
    protected PetriNetParameters parameters;

    /**
     * Dies ist der Konstruktor, auf den alle anderen aufbauen; <br>
     * 
     * @param parameters
     *            Referenz auf die ParameterKlasse (wird zum zeichnen benötigt)
     * @param nodeID
     *            ID des Knoten
     * @param nodeName
     *            Name des Knoten
     * @param nodePosition
     *            Position des Knoten
     */
    public PetriNetNode(PetriNetParameters parameters, String nodeID, String nodeName,
            Point nodePosition) {
        super();
        this.parameters = parameters;
        this.nodeID = nodeID;
        this.nodeName = nodeName;
        this.nodePosition = nodePosition;
    }

    /**
     * Hier wird die ID des Knotens gespeichert
     */
    protected String nodeID;

    /**
     * Hier wird der Name des Knotens gespeichert
     */
    protected String nodeName;

    /**
     * Hier wird die Position abgespeichert, die die linke obere Ecke des
     * Rechtecks bezeichnet, in das der Knoten gezeichnet wird
     */
    protected Point nodePosition;

    /**
     * Die Methode gibt die ID des jeweiligen Knotens zurück
     * 
     * @return String nodeID
     */
    public String getNodeID() {
        return this.nodeID;
    };

    /**
     * Die Methode gibt den Namen des jeweiligen Knotens zurück
     * 
     * @return String nodeName
     */
    public String getNodeName() {
        return this.nodeName;
    }

    /**
     * Die Methode gibt die Position des jeweiligen Knotens in der Grafik zurück
     * 
     * @return nodePosition
     */
    public Point getNodePosition() {
        return this.nodePosition;
    }

    /**
     * Die Methode gibt an, ob der Knoten eine Stelle ist
     * 
     * @return true wenn der Knoten eine Stelle darstellt <br>
     *         false sonst
     */
    public boolean isPlace() {
        return false;
    };

    /**
     * Die Methode gibt an, ob der Knoten eine Transition ist
     * 
     * @return true wenn der Knoten eine Transition darstellt <br>
     *         false sonst
     */
    public boolean isTransition() {
        return false;
    };

    /**
     * Die Methode setzt die ID des jeweiligen Knotens
     * 
     * @param nodeID
     *            ID des Knotens
     */
    public void setNodeID(String nodeID) {
        this.nodeID = nodeID;
    }

    /**
     * Die Methode setzt den Namen des jeweiligen Knotens
     * 
     * @param nodeName
     *            Name des Knotens
     */
    public void setNodeName(String nodeName) {
        this.nodeName = nodeName;
    }

    /**
     * Die Methode setzt die Position des jeweiligen Knotens
     * 
     * @param nodePosition
     *            Position des Knotens
     */
    public void setNodePosition(Point nodePosition) {
        this.nodePosition = nodePosition;
    }
}
