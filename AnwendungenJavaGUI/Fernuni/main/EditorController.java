package main;

import graphics.*;

import java.awt.Point;
import java.awt.event.*;
import java.io.File;
import java.nio.file.FileSystems;
import java.nio.file.Path;

import javax.swing.*;

import pnml.PNMLParser;
import main.*;

/**
 *
 *
 * @author Karl-Heinz Gödderz<br>
 *         Matr. 6513522
 *
 */
public class EditorController
        implements ActionListener, ItemListener, MouseListener, MouseMotionListener,
        MouseWheelListener {

    /**
     * Referenz auf das aufrufende Fenster
     */
    private PetriNetEditor frame;

    /**
     * Referenz auf den Menü-Generator
     */
    private EditorMenuGenerator menuGen;

    /**
     * Referenz auf das verwendete Panel
     */
    private EditorPanel panel;

    /**
     * Referenz auf die Markierung
     */
    private MarkedNet markedNet;

    /**
     * Referenz auf die Parameterklasse;
     */
    private PetriNetParameters parameters;

    /**
     * Anlegen der Log-Elemente und des ErrorLevels sowie eines Fehlerschalters
     */
    private StringBuffer logError = new StringBuffer();
    private StringBuffer logWarnings = new StringBuffer();
    private StringBuffer logInformation = new StringBuffer();

    private EMessageLevel msglvl = EMessageLevel.LEER;
    private boolean fehler = false;

    /**
     * Zwischenspeicher für das letzte Verzeichnis
     */
    private String lastDirectory = " ";

    /**
     * Diese Konstruktor speichert die Referenz auf das aufrufende Fenster und
     * holt sich <br>
     * eine Referenz auf das verwendete Editor-Panel<br>
     * eine Referenz auf die Parameterklasse
     * 
     * @param frame
     *            das aufrufende Fenster
     */
    public EditorController(PetriNetParameters parameters, EditorPanel panel) {
        // übernehmen der Parameter in eigene Felder
        this.parameters = parameters;
        this.panel = panel;
        // this.frame = frame;

        // abholen der weiteren notwendigen Parameter
        // this.menuGen = this.frame.getMenuGenerator();
        // this.markedNet = this.frame.getMarkedNet();
        // System.out.println("panel:" + panel);
    }

    /*
     * Alle Events die durch ActionListener ausgelöst werden
     */

    /*
     * (non-Javadoc)
     * 
     * @see
     * java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        // TODO Auto-generated method stub
        // System.out.println("ActionPerformed " + e);
        switch (e.getActionCommand()) {
            case "DateiLaden":
                markedNet = panel.getMarkedNet();
//                System.out.println();
                markedNet = loadMarkedNet();
//                System.out.println(markedNet);
//                System.out.println(panel);
                panel.setMarkedNet(markedNet);
                panel.refresh();
                break;
            default:
                break;
        }
    }

    /*
     * Alle Events die durch ItemListener ausgelöst werden
     */

    /*
     * (non-Javadoc)
     * 
     * @see
     * java.awt.event.ItemListener#itemStateChanged(java.awt.event.ItemEvent)
     */
    @Override
    public void itemStateChanged(ItemEvent e) {
        // TODO Auto-generated method stub
        System.out.println("itemStateChanged " + e);

    }

    /*
     * Alle Events die durch MouseListener ausgelöst werden
     */

    /**
     * Diese Methode öffnet einen JfileDialog zum Laden aus einer Datei und ruft
     * dann den PNML-Parser auf
     */
    private MarkedNet loadMarkedNet() {
        // Anlegen Parserinstanz
        PNMLParser parser;
        /* temporär Standarddatei vorgeben */
        String tmpString = "C:\\Users\\Studium\\Documents\\"
                + "StudiumInformatik\\2014_WinterSemester\\"
                + "K01584_Programmierpraktikum\\DatenBestand\\" + "Beispiel1.pnml";
        if (lastDirectory == " ") {
            lastDirectory = tmpString;
        }
//        System.out.println(lastDirectory);

        // Anlegen file chooser
        final JFileChooser fc = new JFileChooser(lastDirectory);

        // Anzeigen Dialog
        int returnVal = fc.showOpenDialog(panel);

        // Dialog wurde normal abgeschlossen
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            // Aufruf des Parsers
            lastDirectory = fc.getSelectedFile().toString();
            parser = new PNMLParser(parameters, this, fc.getSelectedFile());
            markedNet = parser.getMarkedNet();
        }
        // Dialog wurde abgebrochen
        else {
            logInformation.append("\nOpen command cancelled by user.\n");
        }

        // showDialogLog(logString);
        // TODO Auto-generated method stub
        return markedNet;
    }

    /**
     * @param logString
     */
    public void showLogDialog(StringBuffer logString, EMessageLevel msgLvl) {

        switch (msgLvl) {
            case LEER:
                JOptionPane.showMessageDialog(frame, logString.toString(), " ",
                        JOptionPane.PLAIN_MESSAGE);
                break;
            case INFO:
                JOptionPane.showMessageDialog(frame, logString.toString(), "Information",
                        JOptionPane.INFORMATION_MESSAGE);
                break;
            case WARN:
                JOptionPane.showMessageDialog(frame, logString.toString(), "Warnung",
                        JOptionPane.WARNING_MESSAGE);
                break;
            case ERR:
                JOptionPane.showMessageDialog(frame, logString.toString(), "Schwerer Fehler",
                        JOptionPane.ERROR_MESSAGE);
                break;
            default:
                break;
        }
        // System.out.println("showDialogLog");
        logString = new StringBuffer();
    }

    /**
     * Diese Methode bereitet ein PopUpMenu auf und gibt es aus
     * 
     * @param e
     *            das auslösende MouseEvent
     */
    private void maybeShowPopup(MouseEvent e) {
        if (e.isPopupTrigger()) {
            Object fit = touchedFigure();
            if (fit instanceof Place) {
                menuGen.getPopupPlace();
            }

            // popup.show(e.getComponent(),
            // e.getX(), e.getY());
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.awt.event.MouseListener#mouseClicked(java.awt.event.MouseEvent)
     */
    @Override
    public void mouseClicked(MouseEvent e) {
        // TODO Auto-generated method stub
        System.out.println("MouseClicked " + e);

    }

    /*
     * (non-Javadoc)
     * 
     * @see java.awt.event.MouseListener#mousePressed(java.awt.event.MouseEvent)
     */
    @Override
    public void mousePressed(MouseEvent e) {
        // TODO Auto-generated method stub
        System.out.println("MousePressed " + e);
        maybeShowPopup(e);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * java.awt.event.MouseListener#mouseReleased(java.awt.event.MouseEvent)
     */
    @Override
    public void mouseReleased(MouseEvent e) {
        // TODO Auto-generated method stub
        System.out.println("MouseReleased " + e);
        maybeShowPopup(e);
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.awt.event.MouseListener#mouseEntered(java.awt.event.MouseEvent)
     */
    @Override
    public void mouseEntered(MouseEvent e) {
        // TODO Auto-generated method stub
        System.out.println("MouseEntered " + e);

    }

    /*
     * (non-Javadoc)
     * 
     * @see java.awt.event.MouseListener#mouseExited(java.awt.event.MouseEvent)
     */
    @Override
    public void mouseExited(MouseEvent e) {
        // TODO Auto-generated method stub
        System.out.println("MouseExited " + e);

    }

    /*
     * Alle Events die durch MouseMotion Listener ausgelöst werden
     */

    /*
     * (non-Javadoc)
     * 
     * @see
     * java.awt.event.MouseMotionListener#mouseDragged(java.awt.event.MouseEvent
     * )
     */
    @Override
    public void mouseDragged(MouseEvent e) {
        // TODO Auto-generated method stub
        System.out.println("MouseDragged " + e);

    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * java.awt.event.MouseMotionListener#mouseMoved(java.awt.event.MouseEvent)
     */
    @Override
    public void mouseMoved(MouseEvent e) {
        // TODO Auto-generated method stub
        // System.out.println("MouseMoved " + e);

    }

    /*
     * Alle Events die durch MouseWheelListener ausgelöst werden
     */

    /*
     * (non-Javadoc)
     * 
     * @see java.awt.event.MouseWheelListener#mouseWheelMoved(java.awt.event.
     * MouseWheelEvent)
     */
    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        // TODO Auto-generated method stub
        System.out.println("MouseWheelMoved " + e);

    }

    private PetriNetComponent touchedFigure() {
        return new Place(parameters, "x1", "x1a", new Point(50, 50), 5);
    }

}
