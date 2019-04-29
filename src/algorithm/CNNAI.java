package algorithm;

import convolutional.neural.network.CNN;
import convolutional.neural.network.CNNLayer;
import image.processing.ImageManager;
import math.MathTools;
import math.Matrix;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * Classe qui gere le reseau de convolution dans l'application
 * On considere que le reseau est deja entraine, les utilisateurs ne peuvent pas l'entrainer (trop de temps)
 * @author Gaya Mehenni
 */
public class CNNAI extends JPanel {

    private CNN cnn;
    public static final String loadingPath = "network_saves/trained_cnn.dat";
    private int imageSize;

    public CNNAI(int imageSize) {

        this.imageSize = imageSize;
        cnn = CNN.loadNetwork(loadingPath);

    }

    /**
     * Methode qui passe une image a travers le reseau
     * @param img image
     * @return retourne le tableau de probabilites des marques
     */
    public double[] feedForward(BufferedImage img) {

        double[] out = new double[3];
        try {
            double[][][] input = MathTools.mapArray(ImageManager.convertRGB2D(ImageManager.getSquaredImage(img, imageSize)), 0, 255, 0, 1);

            Matrix[] in = new Matrix[3];

            for (int i = 0; i < in.length; i++) {
                in[i] = new Matrix(input[i]);
            }

            cnn.setInputs(in);

            out = cnn.feedForward();
        } catch (IOException e) {
            System.out.println("Erreur lors du chargement de l'image");
        }

        repaint();

        return out;

    }

    /**
     * Methode qui dessine le reseau
     * @param g contexte graphique
     */
    public void paintComponent(Graphics g) {

        Graphics2D g2d = (Graphics2D) g;
        g2d.setStroke(new BasicStroke(3));
        double nbLayers = cnn.getLayers().size() - 1, x = 0, y = 0, offsetX = 20, offsetY = 20;
        double sizeLayer = (getWidth() - (nbLayers + 1) * offsetX) / nbLayers;

        x = offsetX;

        for(int layer = 0; layer < cnn.getLayers().size() - 1; layer++) {

            CNNLayer l = cnn.getLayers().get(layer);

            double sizeInput = (getHeight() - ((l.getInputs().length + 1) * offsetY)) / l.getInputs().length;

            y = offsetY;
            for(int input = 0; input < l.getInputs().length; input++) {

                Matrix m = l.getInputs()[input];

                BufferedImage img = ImageManager.transformMatrixToImage(m);

                System.out.println(x + ", " + y);
                g2d.drawImage(img, (int) x, (int) y, (int) sizeLayer, (int) sizeInput, null);
                g2d.setColor(new Color(0, 0, 0));
                g2d.drawRect((int) x, (int) y, (int) sizeLayer, (int) sizeInput);

                y += offsetX;
                y += sizeInput;

            }

            x += offsetX;
            x += sizeLayer;

        }
    }


}
