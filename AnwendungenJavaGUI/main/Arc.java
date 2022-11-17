package main;

import java.awt.*;
import java.awt.geom.Point2D;

/**
 * In dieser Klasse werden alle Informationen für einen Verbindungspfeil
 * verwaltet
 * 
 * @author Karl-Heinz Gödderz<br>
 *         Matr. 6513522
 *
 */
public class Arc
        implements PetriNetComponent {

    /**
     * Referenz auf die ParameterKlasse
     */
    private PetriNetParameters parameters;

    /**
     * ID für die Verbindung
     */
    private String arcID;

    /**
     * Referenz auf den Startknoten der Verbindung, entweder eine Stelle oder
     * eine Transaktion
     */
    private PetriNetNode startNode;

    /**
     * Referenz auf den Endknoten der Verbindung, entweder eine Stelle oder eine
     * Transaktion
     */
    private PetriNetNode endNode;

    /**
     * Startpunkt des Pfeiles (Berechnet aus Knotenart und Zentrum)
     */
    private Point startingPoint;

    /**
     * Endpunkt des Pfeiles (Berechnet aus Knotenart und Zentrum)
     */
    private Point endingPoint;

    /**
     * Diesem Konstruktor werden ID, eine Stelle als Startknoten und eine
     * Transition als Endknoten übergeben.
     * 
     * @param id
     *            ID der Verbindung
     * @param start
     *            Stelle, an der der Pfeil beginnt
     * @param end
     *            Transition, an der der Pfeil endet
     * 
     * 
     */
    public Arc(PetriNetParameters parameters, String id, Place start, Transition end) {
        // Übertragen der Parameter
        this.parameters = parameters;
        this.arcID = id;
        this.startNode = start;
        this.endNode = end;

        initArc(startNode, endNode);
    }

    /**
     * Diesem Konstruktor werden ID, eine Transition als Startknoten und eine
     * Stelle als Endknoten übergeben.
     * 
     * @param id
     *            ID der Verbindung
     * @param start
     *            Transition, an der der Pfeil beginnt
     * @param end
     *            Stelle, an der der Pfeil endet
     * 
     * 
     */
    public Arc(PetriNetParameters parameters, String id, Transition start, Place end) {
        // Übertragen der Parameter
        this.parameters = parameters;
        this.arcID = id;
        this.startNode = start;
        this.endNode = end;
        
        initArc(startNode, endNode);
    }

    /**
     * Diese Methode verarbeitet die Informationen des Start- und des Endknoten
     * 
     * @param start
     *            Startknoten (Stelle oder Transition)
     * @param end
     *            Endknoten (Transition oder Stelle)
     */
    private void initArc(PetriNetNode start, PetriNetNode end) {
        /* Übertragen der Grundpositionen für Start- und Endpunkt */
        this.startingPoint = start.getNodePosition();
        // System.out.println("start:" + startingPoint);
        this.endingPoint = end.getNodePosition();
        // System.out.println(" end :" + endingPoint);
        int centerStartNodeX = startingPoint.x;
        double centerStartNodeY = startingPoint.y;
        double centerEndNodeX = endingPoint.x;
        double centerEndNodeY = endingPoint.y;

        double diffX = startingPoint.x - endingPoint.x;
        double diffY = startingPoint.y - endingPoint.y;
        // System.out.println(endingPoint.distance(startingPoint));

        /* geht davon aus, dass start als Stelle vorliegt(also Kreis) */
        // System.out.println("diffX=" + diffX);
        // System.out.println("diffY=" + diffY);
        double winkel = Math.atan(diffX / diffY);
        // System.out.println(winkel);
    }

    @Override
    public String toString() {
        return (new StringBuffer("Pfeil von " + startNode.getNodeID()) + " nach " + endNode
                .getNodeID()).toString();
    }

    /**
     * @return the arcID
     */
    public String getArcID() {
        return arcID;
    }

    /**
     * Diese Methode gibt den StartKnoten der Verbindung zurück
     * 
     * @return PetriNetNode Startknoten der Verbindung
     * 
     */
    public PetriNetNode getStartNode() {
        return this.startNode;
    }

    /**
     * Diese Methode gibt den Endknoten der Verbindung zurück
     * 
     * @return PetriNetNode Endknoten der Verbindung
     * 
     */
    public PetriNetNode getEndNode() {
        return this.endNode;
    }

    /**
     * Diese Methode zeichnet den Pfeil auf das übergebene Grafikelement
     * 
     * @param g2
     *            Zeichenfläche
     * @param parameters
     *            zu beachtende GrafikParameter
     */
    void paintArc(Graphics2D g2, PetriNetParameters parameters) {
        g2.setColor(Color.BLACK);

        // if (arc.getStartNode().isPlace()) {
        // // Point startPoint = getStartingPosition(edge);
        int centerStartNodeX = getStartNode().getNodePosition().x;
        int centerStartNodeY = getStartNode().getNodePosition().y;
        int centerEndNodeX = getEndNode().getNodePosition().x;
        int centerEndNodeY = getEndNode().getNodePosition().y;

        // g2.drawOval(10, 50, 250, 250);
        // g2.drawArc(-20, 40, 250, 250, 0, 4);
        // g2.drawArc(-20, 40, 250, 250, 7, 4);
        // g2.drawArc(-20, 40, 250, 250, 14, 4);
        // g2.drawArc(-20, 40, 250, 250, 21, 4);
        // g2.drawArc(-20, 40, 250, 250, 28, 4);
        // g2.drawArc(-20, 40, 250, 250, 35, 4);
        // g2.drawArc(-20, 40, 250, 250, 42, 4);
        // g2.drawArc(-20, 40, 250, 250, 47, 4);
        // g2.drawArc(20, 40, 250, 250, 56, 4);
        // g2.drawArc(20, 40, 250, 250, 63, 4);
        // g2.drawArc(20, 40, 250, 250, 70, 4);
        // g2.drawArc(20, 40, 250, 250, 77, 4);
        // g2.drawArc(20, 40, 250, 250, 84, 4);
        //

        // System.out
        // .println("####################################################");
        // double diffX = centerStartNodeX - centerEndNodeX;
        // System.out.println("diffX=" + diffX);
        // double diffY = centerStartNodeY - centerEndNodeY;
        // System.out.println("diffY=" + diffY);
        // double tan = diffX / diffY;
        // System.out.println("tan=" + tan);
        // double atan = Math.atan(tan);
        // System.out.println("atan=" + atan);
        // System.out.println(centerStartNodeX);
        // centerStartNodeX = centerStartNodeX + Math.cos(atan) * ELEMWIDTH
        // / 2;
        // System.out.println("Diff=" + Math.cos(atan) * ELEMWIDTH / 2);
        // System.out.println(centerStartNodeX);
        // centerStartNodeY -= Math.sin(atan) * ELEMHEIGHT / 2;
        //
        // int startPositionX = new Double(centerStartNodeX).intValue();
        // int startPositionY = new Double(centerStartNodeY).intValue();
        // int endPositionX = new Double(centerEndNodeX).intValue();
        // int endPositionY = new Double(centerEndNodeY).intValue();
        //
        // g2.drawLine(startPositionX, startPositionY, endPositionX,
        // endPositionY);
        //
        // }
        // else if (arc.getStartNode().isTransition()) {
        //
        // }
        // else {
        //
        // }
        // if (arc.getEndNode().isPlace()) {
        // // Point endPoint = getEndingPosition(edge);
        // }
        // else {
        // if (arc.getEndNode().isTransition()) {
        //
        // }
        // else {
        //
        // }
        // }

    }

    /**
     * Diese Methode meldet zurück, ob der übergebene Punkt im Bereich des
     * Pfeiles liegt (mit einer Unschärfe von halo)
     * 
     * @param point
     *            zu überprüfender Punkt
     * @param parameters
     *            zu beachtende Grafikparameter
     * 
     */
    boolean touchesArc(Point2D point, PetriNetParameters parameters) {
        return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see main.PetriNetComponent#paintNode(java.awt.Graphics2D)
     */
    @Override
    public void paintPetriNetComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        paintArc(g2, parameters);
    }

    /*
     * (non-Javadoc)
     * 
     * @see main.PetriNetComponent#touchesNode(java.awt.Point, int)
     */
    @Override
    public boolean touchesPetriNetComponent(Point point, PetriNetParameters parameters) {
        return touchesArc(point, parameters);
    }
}
