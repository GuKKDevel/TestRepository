package main;

import java.awt.*;

import javax.swing.text.JTextComponent;

/**
 * In dieser Klasse werden die Parameter für die grafische Ausgabe gespeichert
 *
 * @author Karl-Heinz Gödderz<br>
 *         Matr. 6513522
 *
 */
public class PetriNetParameters {

    /**
     * Größe der Pfeilspitze
     */
    private int arcSize = 5;

    /**
     * @param arcSize
     *            the arcSize to set
     */
    @SuppressWarnings("hiding")
    public void setArcSize(int arcSize) {
        this.arcSize = arcSize;
    }

    /**
     * @return the arcSize
     */
    public int getArcSize() {
        return arcSize;
    }

    /**
     * Einrückung gezeichneten Elemente um einen Rand zu schaffen
     */
    private Point indentation = new Point(50, 50);

    /**
     * @return the indentation
     */
    public Point getIndentation() {
        return indentation;
    }

    /**
     * @param indentation
     *            the indentation to set
     */
    public void setIndentation(Point indentation) {
        this.indentation = indentation;
    }

    /**
     * Farbe für aktivierte Transitionen
     */
    private Color colorActive = Color.GREEN;

    /**
     * @param colorActive
     *            the colorActiv to set
     */
    public void setColorActive(Color colorActive) {
        this.colorActive = colorActive;
    }

    /**
     * @return the colorActiv
     */
    public Color getColorActive() {
        return colorActive;
    }

    /**
     * Farbe für den Umriss
     */
    private Color colorContour = Color.BLACK;

    /**
     * @param colorContour
     *            the colorContour to set
     */
    public void setColorContour(Color colorContour) {
        this.colorContour = colorContour;
    }

    /**
     * @return the colorContour
     */
    public Color getColorContour() {
        return colorContour;
    }

    /**
     * Farbe für nicht aktivierte Transitionen
     */
    private Color colorInactive = Color.WHITE;

    /**
     * @param colorInactive
     *            the colorInactiv to set
     */
    public void setColorInactive(Color colorInactive) {
        this.colorInactive = colorInactive;
    }

    /**
     * @return the colorInactiv
     */
    public Color getColorInactive() {
        return colorInactive;
    }

    /**
     * Farbe für die Stellen
     */
    private Color colorNormal = Color.WHITE;

    /**
     * @param colorNormal
     *            the colorNormal to set
     */
    public void setColorNormal(Color colorNormal) {
        this.colorNormal = colorNormal;
    }

    /**
     * @return the colorNormal
     */
    public Color getColorNormal() {
        return colorNormal;
    }

    /**
     * Farbe für selectierte Elemente
     */
    private Color colorSelected = Color.RED;

    /**
     * @param colorSelected
     *            the colorSelected to set
     */
    public void setColorSelected(Color colorSelected) {
        this.colorSelected = colorSelected;
    }

    /**
     * @return the colorSelected
     */
    public Color getColorSelected() {
        return colorSelected;
    }

    /**
     * Farbe für die Marken
     */
    private Color colorToken = Color.BLACK;

    /**
     * @param colorToken
     *            the colorToken to set
     */
    public void setColorToken(Color colorToken) {
        this.colorToken = colorToken;
    }

    /**
     * @return the colorToken
     */
    public Color getColorToken() {
        return colorToken;
    }

    /**
     * Unschärfe des Mauszeigers
     */
    private int halo = 5;

    /**
     * @param halo
     *            the halo to set
     */
    @SuppressWarnings("hiding")
    public void setHalo(int halo) {
        this.halo = halo;
    }

    /**
     * @return the halo
     */
    public int getHalo() {
        return halo;
    }

    /**
     * Größe des Textfeldes für den Namen des Knoten
     */
    private int nameSize = 11;

    /**
     * @param nameSize
     *            the nameSize to set
     */
    @SuppressWarnings("hiding")
    public void setNameSize(int nameSize) {
        this.nameSize = nameSize;
    }

    /**
     * @return the nameSize
     */
    public int getNameSize() {
        return nameSize;
    }

    /**
     * Offset, der benötigt wird, damit Figuren gefüllt werden können
     */
    private int offset = 1;

    /**
     * @param offset
     *            the offset to set
     */
    @SuppressWarnings("hiding")
    public void setOffset(int offset) {
        this.offset = offset;
    }

    /**
     * @return the offset
     */
    public int getOffset() {
        return offset;
    }

    /**
     * Größe des Kreises für die Stelle
     */
    private Dimension placeSize = new Dimension(50, 50);

    /**
     * @param dimension
     *            the placeSize to set
     */
    public void setPlaceSize(Dimension dimension) {
        this.placeSize = dimension;
    }

    /**
     * @return the placeSize
     */
    public Dimension getPlaceSize() {
        return placeSize;
    }

    /**
     * Dieses Flag zeigt an, ob der Name des Knotens mit ausgegeben werden soll
     */
    private boolean showNames = false;

    /**
     * @param showNames
     *            the showNames to set
     */
    public void setShowNames(boolean showNames) {
        this.showNames = showNames;
    }

    /**
     * @return the showNames
     */
    public boolean isShowNames() {
        return showNames;
    }

    /**
     * Größe des Markenfeldes
     */
    private Dimension tokenSize = new Dimension(10, 10);

    /**
     * @param dimension
     *            the tokenSize to set
     */
    @SuppressWarnings("hiding")
    public void setTokenSize(Dimension dimension) {
        this.tokenSize = dimension;
    }

    /**
     * @return the tokenSize
     */
    public Dimension getTokenSize() {
        return tokenSize;
    }

    /**
     * Größe des Quadrats für die Transition
     */
    private Dimension transitionSize = new Dimension(50, 50);

    /**
     * @param dimension
     *            the transitionSize to set
     */
    public void setTransitionSize(Dimension dimension) {
        this.transitionSize = dimension;
    }

    /**
     * @return the transitionSize
     */
    public Dimension getTransitionSize() {
        return transitionSize;
    }

    public PetriNetParameters() {
        initParameters();
    }

    /**
     * Initialisieren der Grafik-Parameter
     */
    protected void initParameters() {
        setArcSize(5);
        setIndentation(new Point(50, 50));
        setColorActive(Color.GREEN);
        setColorContour(Color.BLACK);
        setColorInactive(Color.WHITE);
        setColorNormal(Color.WHITE);
        setColorSelected(Color.RED);
        setColorToken(Color.BLACK);
        setHalo(5);
        setNameSize(11);
        setOffset(1);
        setPlaceSize(new Dimension(50, 50));
        setShowNames(false);
        setTokenSize(new Dimension(10, 10));
        setTransitionSize(new Dimension(50, 50));
    }
public Dimension getTextDimension(JTextComponent t) {
    return null;
}
}
