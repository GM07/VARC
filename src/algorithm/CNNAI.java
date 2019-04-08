package algorithm;

import convolutional.neural.network.CNN;
import image.processing.ImageManager;
import math.MathTools;
import math.Matrix;

import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * Classe qui gere le reseau de convolution dans l'application
 * On considere que le reseau est deja entraine, les utilisateurs ne peuvent pas l'entrainer (trop de temps)
 * @author Gaya Mehenni
 */
public class CNNAI {

    private CNN cnn;
    private final String loadingPath = "network_saves/trained_cnn.dat";
    private int imageSize;

    public CNNAI(int imageSize) {

        this.imageSize = imageSize;
        cnn = CNN.loadNetwork(getClass().getClassLoader().getResource(loadingPath).getPath());
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

        return out;

    }




}
