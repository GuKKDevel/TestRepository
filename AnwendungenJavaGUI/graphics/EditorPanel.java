package graphics;

import java.awt.*;
import java.awt.event.*;

import javax.swing.JFrame;
import javax.swing.JPanel;

import main.*;

/**
 * 
 *
 * @author Karl-Heinz Gödderz<br>
 *         Matr. 6513522
 *
 */
public class EditorPanel
        extends JPanel {

    /**
     * Parameter für die grafischen Komponenten
     */
    private PetriNetParameters parameters;;

    /**
     * Die darzustellende Markierung
     */
    private MarkedNet markedNet;

    /**
     * @param markedNet
     *            the markedNet to set
     */
    public void setMarkedNet(MarkedNet markedNet) {
        this.markedNet = markedNet;
    }

    /**
     * @return the markedNet
     */
    public MarkedNet getMarkedNet() {
        return markedNet;
    }

    /**
     * Konfiguriert die Menüs
     */

    private EditorMenuGenerator menuGen;
    /**
     * Referenz auf das Frame-Object
     */
    private PetriNetEditor frame;

    /**
     * Kontrolliert die Ereignisse
     */
    private EditorController controller;

    /**
     * Der Konstruktor erwartet eine Referenz auf das übergeordnete Frame-Objekt
     * damit holt er <br>
     * eine Referenz auf die Markierung <br>
     * eine Referenz auf den MenuKonfigurator<br>
     * eine Referenz auf den EreignisController<br>
     * eine Referenz auf die Parameterklasse
     * 
     * @param frame
     *            Referenz auf den übergeordneten Frame
     */
    // public EditorPanel(PetriNetEditor frame) {
    public EditorPanel(PetriNetParameters parameters, MarkedNet markedNet,
            EditorMenuGenerator menuGen) {
        // this.frame = frame;
        this.markedNet = markedNet;
        this.menuGen = menuGen;
        this.parameters = parameters;

        this.setPreferredSize(new Dimension(1000, 1000));
    }

    /**
     * 
     */
    public void initListeners(EditorController controller) {
        this.addMouseListener(controller);
        this.addMouseMotionListener(controller);
        this.addMouseWheelListener(controller);
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.awt.Container#paintComponent(java.awt.Graphics)
     */
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        System.out.println("zeichne");
        if (markedNet == null) {
            markedNet = new MarkedNet(parameters);
        }
        /*
         * Elementweises zeichnen der Transitionen
         */
        // System.out.println("zeichne Transitionen");
        for (Transition transition : markedNet.getTransitions()) {
            transition.paintPetriNetComponent(g);
        }

        /*
         * Elementweises zeichnen der Stellen
         */
        for (Place place : markedNet.getPlaces()) {
            place.paintPetriNetComponent(g);
        }

        /*
         * Elementweises zeichnen der Verbindungspfeile
         */
        // TODO Auto-generated method stub
        for (Arc arc : markedNet.getArcs()) {
            arc.paintPetriNetComponent(g);
        }

    }

    public void refresh() {
//        System.out.println("refresh");
//        System.out.println(markedNet);
        repaint();
    }

}
