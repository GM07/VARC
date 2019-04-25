package dessin;

import javax.swing.border.StrokeBorder;
import java.awt.*;
import java.awt.geom.AffineTransform;

import neural.network.Layer;
import neural.network.LayerType;

/**
 * Objet dessinable pour représenter une neurone
 * @author Gaya Mehenni
 */
public class DrawableNeuron implements Dessinable {

    private Color color;
    private int posX, posY, diametre;
    private int value;
    private LayerType layerType;
    private final int base = 145;

    /**
     * Constructeur d'une neurone dessinable
     * @param diametre
     */
    public DrawableNeuron(int diametre, LayerType type) {
        this.diametre = diametre;
        this.layerType = type;

        value = (int) (Math.random() * 255);

        color = new Color(value, value, value);

    }

    /**
     * Dessin de la neurone
     * @param g2d contexte graphique
     */
    public void dessiner(Graphics2D g2d) {

        AffineTransform backup = new AffineTransform(g2d.getTransform());

        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // On dessine l'interieur de l'oval
//        if (layerType == LayerType.InputLayer) {
//            color = new Color(value, base, base);
//        } else if (layerType == LayerType.HiddenLayer) {
//            color = new Color(base, value, base);
//        } else {
//            color = new Color(base, base, value);
//
//        }

        color = new Color(value, value, value);
        g2d.setColor(color);
        g2d.fillOval(posX - diametre/2, posY - diametre/2, diametre, diametre);

        // On dessine le contour
        g2d.setColor(Color.BLACK);
        g2d.setStroke(new BasicStroke(1.5f));
        g2d.drawOval(posX - diametre/2, posY - diametre/2, diametre, diametre);

        // On affiche la valeur de la neurone
        /*
        g2d.setFont(new Font("Dialog", Font.BOLD, 10));
        g2d.setColor(new Color(255 - value, 255 - value, 255 - value));
        g2d.drawString(value + "", posX - g2d.getFontMetrics().stringWidth(value + "")/2, posY + 3);
        */

        g2d.setTransform(backup);
    }

    /**
     * Retourne la couleur de la neurone
     * @return
     */
    public Color getColor() {
        return color;
    }

    /**
     * Change la couleur de la neurone
     * @param color
     */
    public void setColor(Color color) {
        this.color = color;
    }

    /**
     * Retourne la position de la neurone en x
     * @return
     */
    public int getPosX() {
        return posX;
    }

    /**
     * Change la position de la neurone en x
     * @param posX
     */
    public void setPosX(int posX) {
        this.posX = posX;
    }

    /**
     * Retourne la position de la neurone en y
     * @return
     */
    public int getPosY() {
        return posY;
    }

    /**
     * Change la position de la neurone en y
     * @param posY
     */
    public void setPosY(int posY) {
        this.posY = posY;
    }

    /**
     * Retourne le diametre de la neurone
     * @return
     */
    public int getDiametre() {
        return diametre;
    }

    /**
     * Change le diametre de la neurone
     * @param diametre
     */
    public void setDiametre(int diametre) {
        this.diametre = diametre;
    }

    /**
     * Retourne la valeur de la neurone
     * @return
     */
    public int getValue() {
        return value;
    }

    /**
     * Change la valeur de la neurone
     * @param value
     */
    public void setValue(int value) {
        this.value = value;
    }
}
