package dessin;

import neural.network.Layer;
import neural.network.LayerType;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;

/**
 * Dessin d'un reseau de neurone
 * @author Gaya Mehenni
 *
 */
public class DrawableNeuralNetwork implements Dessinable {

    private DrawableNeuron[][] neurons;
    private float[][][] weights;
    private int[] layers;
    private int tailleX, tailleY, posX, posY;

    /**
     * constructeur du dessin
     */
    public DrawableNeuralNetwork(int posX, int posY, int tailleX, int tailleY, int... layers) {

        this.posX = posX;
        this.posY = posY;
        this.tailleX = tailleX;
        this.tailleY = tailleY;
        this.layers = layers;

        neurons = new DrawableNeuron[layers.length][];

        for(int i = 0; i < layers.length; i++) {
            neurons[i] = new DrawableNeuron[layers[i]];
            for(int j = 0; j < neurons[i].length; j++) {

                LayerType type;
                if (i == 0) type = LayerType.InputLayer;
                else if (i == layers.length - 1) type = LayerType.OutputLayer;
                else type = LayerType.HiddenLayer;

                neurons[i][j] = new DrawableNeuron(25, type);
            }
        }

        // On place la position de chaque neurone
        for(int i = 0; i < neurons.length; i++) {

            int translationX = i * (tailleX/(neurons.length - 1));

            for(int j = 0; j < neurons[i].length; j++) {

                int translationY = 0;
                double pY = 0;

                if (neurons[i].length % 2 != 0) {
                    // Nombre de neurons impair
                    translationY = (tailleY / (neurons[i].length));
                    pY = -(neurons[i].length/2) + j;
                }
                else {
                    // Nombre de neurones pair
                    translationY = (tailleY / (neurons[i].length * 1));;
                    pY = -(1.5) + j;
                }

                neurons[i][j].setPosX(translationX);
                neurons[i][j].setPosY((int) (translationY * pY));

            }
        }

        // Initialise les weights
        weights = new float[neurons.length][][];
        for(int i = 1; i < neurons.length; i++) {

            weights[i] = new float[neurons[i].length][];
            for(int j = 0; j < neurons[i].length; j++) {

                weights[i][j] = new float[neurons[i - 1].length];
                for(int k = 0; k < neurons[i - 1].length; k++) {
                    weights[i][j][k] = (float) (Math.random() * 255);
                }
            }
        }
    }

    /**
     * dessin du reseau
     */
    public void dessiner(Graphics2D g2d) {

        AffineTransform backup = new AffineTransform(g2d.getTransform());

//        g2d.drawRect(posX, posY, tailleX, tailleY);
        g2d.translate(posX, posY);
        g2d.translate(0, tailleY/2);

        // Dessin des weights
        for(int i = 1; i < neurons.length; i++) {

            for(int j = 0; j < neurons[i].length; j++) {

                for(int k = 0; k < neurons[i - 1].length; k++) {

                    Point depart = new Point(neurons[i][j].getPosX(), neurons[i][j].getPosY());
                    Point arrivee = new Point(neurons[i - 1][k].getPosX(), neurons[i - 1][k].getPosY());


                    //g2d.setColor(new Color(value, 0, (255 - value)));
                    g2d.setStroke(new BasicStroke((weights[i][j][k]/255f) * 3));
                    g2d.drawLine((int) depart.getX(), (int) depart.getY(), (int) arrivee.getX(), (int) arrivee.getY());

                }
            }
        }

        // Dessin des neurones
        for(int i = 0; i < neurons.length; i++) {
            for(int j = 0; j < neurons[i].length; j++) {
                neurons[i][j].dessiner(g2d);
            }
        }


        g2d.setTransform(backup);
    }

    /**
     * Methode qui change la valeur des weights
     */
    public void changeWeights() {
        for(int i = 1; i < neurons.length; i++) {

            for(int j = 0; j < neurons[i].length; j++) {

                for(int k = 0; k < neurons[i - 1].length; k++) {
                    weights[i][j][k] = (float) (Math.random() * 255);
                }
            }
        }
    }

    /**
     * Methode qui change la valeur des neurons
     */
    public void changeNeurons() {
        for(int i = 0; i < neurons.length; i++) {
            for(int j = 0; j < neurons[i].length; j++) {

                /*
                double variation = Math.random() * 6 - 3;
                if ((int) (neurons[i][j].getValue() + variation) > 255 || (int) (neurons[i][j].getValue() + variation) < 0) neurons[i][j].setValue((int) (Math.random() * 255));
                else neurons[i][j].setValue((int) (neurons[i][j].getValue() + variation));
                */
                neurons[i][j].setValue((int) (Math.random() * 255));
            }
        }
    }

    /**
     * Retourne le nombre de layers du réseau de neurone
     * @return
     */
    public int[] getLayers() {
        return layers;
    }

    /**
     * Change le nombre de layers du réseau de neurone
     * @param layers
     */
    public void setLayers(int[] layers) {
        this.layers = layers;
    }

    /**
     * Retourne la taille en x que le dessin est supposee prendre
     * @return
     */
    public int getTailleX() {
        return tailleX;
    }

    /**
     * Change la taille en x que le dessin est supposee prendre
     * @param tailleX
     */
    public void setTailleX(int tailleX) {
        this.tailleX = tailleX;
    }

    /**
     * Retourne la taille en y que le dessin est suppose prendre
     * @return
     */
    public int getTailleY() {
        return tailleY;
    }

    /**
     * Change la taille en y que le dessin est suppose prendre
     * @param tailleY
     */
    public void setTailleY(int tailleY) {
        this.tailleY = tailleY;
    }

    /**
     * Retourne la position en x du reseau
     * @return
     */
    public int getPosX() {
        return posX;
    }

    /**
     * Change la position en x du reseau
     * @param posX
     */
    public void setPosX(int posX) {
        this.posX = posX;
    }

    /**
     * Retourne la position en y du reseau
     * @return
     */
    public int getPosY() {
        return posY;
    }

    /**
     * Change la position en y du reseau
     * @param posY
     */
    public void setPosY(int posY) {
        this.posY = posY;
    }
}
