package graphics;

import java.awt.*;

import javax.swing.*;

import main.*;

/**
 * Klasse zur Verwaltung der grafischen Oberfläche.<br>
 * <br>
 * 
 * Instanziert gleichzeitig: <br>
 * <br>
 * die ParameterStruktur<br>
 * das markierte Petrinetz, <br>
 * den Menü-Generator und <br>
 * den Controller für den Editor
 * 
 * @author Karl-Heinz Gödderz<br>
 *         Matr. 6513522
 *
 */
public class PetriNetEditor
        extends JFrame {

    /**
     * Definitionen für BildschirmElemente
     */
    private EditorPanel editorPanel;
    private JScrollPane scrollPane;

    /**
     * Parameter für die grafischen Komponenten
     */
    private PetriNetParameters parameters;
    /**
     * Klasse für die Umsetzung der Anwenderanforderungen
     */
    private EditorController controller;

    /**
     * Generelle Klasse für Menü-Erstellung
     */
    private EditorMenuGenerator menuGen;

    /**
     * Referenz für die anzuzeigende Markierung
     */
    private MarkedNet markedNet;

    /**
     * Dieser Konstruktor erwartet eine Markierung als Übergabe
     * 
     * @param markedNet
     *            Petrinetz mit Markierungen
     */
    public PetriNetEditor(PetriNetParameters parameters, MarkedNet markedNet) {
        super();

        // übernehmen Referenz auf ParameterKlasse
        this.parameters = parameters;

        // übernehmen der Referenz auf
        this.markedNet = markedNet;

        // anlegen Menü-Generator
        this.menuGen = new EditorMenuGenerator();

        // anlegen EditorPanel
        // braucht markedNet, menuGen, controller, PetriNetParameters
        editorPanel = new EditorPanel(this.parameters, this.markedNet, this.menuGen);
        scrollPane = new JScrollPane(editorPanel);
        this.add(scrollPane, BorderLayout.CENTER);
        this.setContentPane(scrollPane);

        // controller anlegen
        // braucht panel, menuGen, parameters, markedNet
        controller = new EditorController(this.parameters, this.editorPanel);

        // jetzt Funktionen ausführen, die den controller brauchen
        editorPanel.initListeners(controller);
        this.setJMenuBar(menuGen.getMenuBar(controller));

        // abschliessende Aktionen für die Anzeige
        initFrame();

    }

    /**
     * Dieser Konstruktor arbeitet mit einer leeren Markierung
     */
    public PetriNetEditor(PetriNetParameters parameters) {
        this(parameters, new MarkedNet(parameters));
    }

    /**
     * Initialisieren des Hauptfensters
     */
    private void initFrame() {
        this.setTitle("6513522_Gödderz_Karl-Heinz");
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setBackground(Color.WHITE);
        this.setPreferredSize(new Dimension(500, 500));
        this.pack();
        this.setVisible(true);

    }

    /**
     * Stellt eine Referenz auf den verwendeten Menü-Generator zur Verfügung
     * 
     * @return verwendeter Menü-Generator
     */
    public EditorMenuGenerator getMenuGenerator() {
        return this.menuGen;
    }

    /**
     * Stellt eine Referenz auf den verwendeten Controller zur Verfügung
     * 
     * @return verwendeter Controller
     */
    public EditorController getEditorController() {
        return this.controller;
    }

    /**
     * Stellt eine Referenz auf die verwendete Grafikfläche zur Verfügung
     * 
     * @return verwendete Grafikfläche(Panel)
     */
    public EditorPanel getEditorPanel() {
        return this.editorPanel;
    }

    /**
     * Stellt eine Referenz auf die Markierung zurück
     * 
     * @return Markierung
     */
    public MarkedNet getMarkedNet() {
        return this.markedNet;
    }

    /**
     * Stellt eine Referenz auf die Parameterklasse zur Verfügung
     * 
     * @return Parameter-Klasse
     */
    public PetriNetParameters getParameters() {
        return this.parameters;
    }

    /**
     * Main-Methode sowie die Methode createAndShowGUI zum starten des Editors
     * 
     * @param args
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                createAndShowGUI();
            }
        });
    }

    private static void createAndShowGUI() {
        // System.out.println("Created GUI on EDT? " +
        SwingUtilities.isEventDispatchThread()
        // )
        ;
        JFrame f = new PetriNetEditor(new PetriNetParameters());
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setSize(250, 250);
        f.setLocationRelativeTo(null);
        f.setVisible(true);
    }
}
