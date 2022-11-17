package main;

import java.awt.*;

/**
 * Dieses Interface dient dazu, den graphischen Elementen einen gemeinsamen
 * Supertyp zu schaffen.
 * 
 *
 * @author Karl-Heinz Gödderz<br>
 *         Matr. 6513522
 *
 */
public interface PetriNetComponent {

    /**
     * Mit dieser Methode wird der Knoten als grafisches Element in das
     * übergebene Grafikelemnt gezeichnet.
     * 
     * @param g2
     *            das Grafikelement auf das gezeichnet wird
     * @param parameters
     *            Parametrierung
     */
    public void paintPetriNetComponent(Graphics g);

    /**
     * Diese Methode stellt fest, ob der übergebene Punkt auf den Knoten zeigt,
     * dabei wird eine Ungenauigkeit von halo Pixel beücksichtigt
     * 
     * @param point
     *            der zu überprüfende Punkt
     * @param parameters
     *            Parametrierung
     * @return <tt>true</tt> wenn der Punkt in dem Bereich liegt <br>
     *         <tt>false</tt> sonst
     */
    public boolean touchesPetriNetComponent(Point point, PetriNetParameters parameters);
}
