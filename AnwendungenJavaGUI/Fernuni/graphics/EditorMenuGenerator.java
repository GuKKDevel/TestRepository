package graphics;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;

import javax.swing.*;

import main.EditorController;

/**
 * Diese Klasse organisiert den Aufbau des Menues und der Popup-Menues
 * 
 * @author Karl-Heinz Gödderz<br>
 *         Matr. 6513522
 *
 */
public class EditorMenuGenerator {

    /**
     * Referenz auf den verwendeten Controller
     */
    private EditorController controller;
    /**
     * Referenz auf das aufrufende Fenster
     */
    private PetriNetEditor frame;

    /**
     * Felder für den MenuBar
     */
    private JMenuBar menuBar;

    private JMenu menuDatei;
    private JMenuItem itemDateiNeu;
    private JMenuItem itemDateiLaden;
    private JMenuItem itemDateiSpeichern;

    private JMenu menuElemente;
    private JMenuItem itemElementeStelle;
    private JMenuItem itemElementeTransition;

    private JMenu menuExtras;
    private JMenuItem itemExtrasOptionen;

    public JMenuBar getMenuBar(EditorController controller) {
        return createMenuBar(controller);
    }

    /**
     * Aufbereiten der Menüleiste
     */
    private JMenuBar createMenuBar(EditorController controller) {

        menuBar = new JMenuBar();

        menuBar.add(createMenuDatei(controller));
        menuBar.add(createMenuElemente(controller));
        menuBar.add(createMenuExtras(controller));

        return menuBar;
    }

    /**
     * Erstellen Datei-Menü
     * 
     * @return das Dateimenü für die Menüleiste
     */
    private JMenu createMenuDatei(EditorController controller) {

        menuDatei = new JMenu("Datei");

        itemDateiNeu = new JMenuItem("Neu");
        itemDateiNeu.setActionCommand(menuDatei.getActionCommand() + "Neu");
        itemDateiNeu.addActionListener(controller);
        menuDatei.add(itemDateiNeu);

        itemDateiLaden = new JMenuItem("Laden");
        itemDateiLaden.setActionCommand(menuDatei.getActionCommand() + "Laden");
        itemDateiLaden.addActionListener(controller);
        menuDatei.add(itemDateiLaden);

        itemDateiSpeichern = new JMenuItem("Speichern");
        itemDateiSpeichern.setActionCommand(menuDatei.getActionCommand()
                + "Speichern");
        itemDateiSpeichern.addActionListener(controller);
        menuDatei.add(itemDateiSpeichern);

        return menuDatei;
    }

    /**
     * Erstellen Elemente-Menü
     * 
     * @return das Elemente-Menü für die Menüleiste
     */
    private JMenu createMenuElemente(EditorController controller) {

        menuElemente = new JMenu("Elemente");

        itemElementeStelle = new JMenuItem("Stelle");
        itemElementeStelle.setActionCommand(menuElemente.getActionCommand()
                + "Stelle");
        itemElementeStelle.addActionListener(controller);
        menuElemente.add(itemElementeStelle);

        itemElementeTransition = new JMenuItem("Transition");
        itemElementeTransition.setActionCommand(menuElemente.getActionCommand()
                + "Transition");
        itemElementeTransition.addActionListener(controller);
        menuElemente.add(itemElementeTransition);

        return menuElemente;
    }

    /**
     * @return das Extras-Menü für die Menüleiste
     */
    private JMenu createMenuExtras(EditorController controller) {

        menuExtras = new JMenu("Extras");

        itemExtrasOptionen = new JMenuItem("Optionen");
        itemExtrasOptionen.setActionCommand(menuExtras.getActionCommand()
                + "Optionen");
        itemExtrasOptionen.addActionListener(controller);
        menuExtras.add(itemExtrasOptionen);

        return menuExtras;
    }

    /**
     * Generiert das Kontext-Menü für Stellen
     * 
     * @return Kontext-Menü für Stellen
     */
    public JPopupMenu getPopupPlace() {
        JPopupMenu popup = new JPopupMenu();
        return popup;
    }

    /**
     * Generiert das Kontext-Menü für die Zeichenfläche
     * 
     * @return Kontext-Menü für Zeichenfläche
     */
    public JPopupMenu getPopupNull() {

        JPopupMenu popup = new JPopupMenu();

        popup.add(itemDateiLaden);
        popup.add(itemDateiSpeichern);
        return popup;
    }

    /**
     * 
     * @return Item Datei---Laden
     */
    public JMenuItem getItemDateiLaden() {
        return itemDateiLaden;
    }
}
