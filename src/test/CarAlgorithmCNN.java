package test;

import algorithm.CarAI;
import convolutional.neural.network.AveragePoolingLayer;
import convolutional.neural.network.CNN;
import convolutional.neural.network.ConvolutionLayer;
import dataset.Batch;
import dataset.DataElement;
import functions.ActivationFunctions;
import image.processing.FileManager;
import image.processing.ImageManager;
import math.MathTools;
import math.Matrix;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

/**
 * Classe qui teste le reseau de convolution sur le dataset des vehicules
 * @author Gaya Mehenni
 */
public class CarAlgorithmCNN {

    private static String trainingPath = "D:\\Cegep\\Session_4\\IA Data\\Dataset_Marques\\training";
    private static String testingPath = "D:\\Cegep\\Session_4\\IA Data\\Dataset_Marques\\testing";
    private static String savingPath = "D:\\Cegep\\Session_4\\IA Data\\CNN_Saves\\trained_cnn_";
    private static double learningRate = 0.001;
    private static int numberOfEpochs = 10;
    private static int numberOfImagesPerEpoch = 200;
    private static int numberOfImagesToTest = 1000;
    private static int batch_size = 1;
    private static int resultCounter = 0;
    private static double lastResult = 0;

    private static ArrayList<DataElement> testImages = new ArrayList<>();
    private static Batch<DataElement> batch = new Batch<DataElement>();

    public static void main(String[] args) {

        System.out.println("STARTING PROGRAM...");

        CNN cnn = new CNN(ActivationFunctions.Sigmoid, 28, 3, 3, learningRate);

        cnn.addLayer(new ConvolutionLayer(7, 7, 28, 3));
        //cnn.addLayer(new AveragePoolingLayer(3, 3));
        cnn.addLayer(new ConvolutionLayer(5, 5));
        //cnn.addLayer(new AveragePoolingLayer(3, 3));
        cnn.addLayer(new ConvolutionLayer(3, 3));
        //cnn.addLayer(new ConvolutionLayer(3, 3));
        cnn.endStructure();

        loadData();

        System.out.println("BEFORE TRAINING : " + testNetwork(cnn));

        //System.out.println(cnn);

        trainNetwork(cnn);

        System.out.println("AFTER TRAINING : " + testNetwork(cnn));

    }

    /**
     * Methode qui teste le reseau de convolution
     * @param cnn reseau de convolution
     * @return taux de reussite
     */
    public static double testNetwork(CNN cnn) {

        //ArrayList<String> folders = FileManager.getFoldersFromFolder(testingPath);

        double result = 0;
        double total = 0;

        Collections.shuffle(testImages);

        for (int i = 0; i < testImages.size(); i++) {

            //System.out.println(testImages.get(i).getData().getClass().getName());

            DataElement d = testImages.get(i);
            String type = (String) (d.getLabel());
            BufferedImage image = (BufferedImage) (d.getData());

            //System.out.println("\t TESTING IMAGE : " + j);
            try {
                double[][][] input = MathTools.mapArray(ImageManager.convertRGB2D(ImageManager.getSquaredImage(image, 28)), 0, 255, 0, 1);

                double[] expected = CarAI.getOutputFromStringBrands(type, cnn.getOutputs().getROWS());

                Matrix[] in = new Matrix[3];
                in[0] = new Matrix(input[0]);
                in[1] = new Matrix(input[1]);
                in[2] = new Matrix(input[2]);
                cnn.setInputs(in);

                double[] output = cnn.feedForward();

                if (MathTools.getHighestIndex(output) == MathTools.getHighestIndex(expected)) result++;
                total++;

            } catch (IOException e) {
                e.printStackTrace();
            }
        }


//		System.out.println(result + " " + total);
        return result / total;
    }

    /**
     * Methode qui charge les images pour l'entrainement et le test
     */
    public static void loadData() {

        System.out.println("Loading data");

        ArrayList<String> folders2 = FileManager.getFoldersFromFolder(testingPath);

        for (int i = 0; i < folders2.size(); i++) {

            ArrayList<BufferedImage> images = FileManager.getImagesFromFolder(testingPath + "\\" + folders2.get(i));

            for (int j = 0; j < images.size(); j++) {
                DataElement<String, BufferedImage> e = new DataElement(folders2.get(i), images.get(i));
                testImages.add(e);
            }

            System.out.println("\t" + folders2.get(i));

        }

        System.out.println("Testing images loaded (" + testImages.size() + ")");

        ArrayList<String> folders = FileManager.getFoldersFromFolder(trainingPath);

        for (int folder = 0; folder < folders.size(); folder++) {
            // On parcoure chaque dossier

            ArrayList<BufferedImage> images = FileManager.getImagesFromFolder(trainingPath + "\\" + folders.get(folder));
            String type = folders.get(folder);
            System.out.println("\t" + type);

            for (BufferedImage image : images) {

                DataElement<String, BufferedImage> dE = new DataElement<String, BufferedImage>(type, image);
                batch.addElementToDataset(dE);
            }

        }

        System.out.println("Training images loaded (" + batch.getDataset().size() + ")");
    }

    /**
     * Methode qui entraine le reseau de convolution
     *
     * @param cnn reseau de convolution
     */
    public static void trainNetwork(CNN cnn) {

        long t1, t2;

        System.out.println();

        batch.shuffleDataset();

        System.out.println("Training neural network...");

        for (int epoch = 0; epoch < numberOfEpochs; epoch++) {

            System.out.print("Epoch : " + epoch);
            t1 = System.currentTimeMillis();

            ArrayList<DataElement> epochData = batch.getPartOfDataset(numberOfImagesPerEpoch);

            for (int data = 0; data < epochData.size(); data++) {

                DataElement dataElement = batch.getDataset().get(data);
                try {
                    double[][][] input = MathTools.mapArray(ImageManager.convertRGB2D(ImageManager.getSquaredImage((BufferedImage) (dataElement.getData()), 28)), 0, 255, 0, 1);

                    Matrix[] in = new Matrix[3];
                    in[0] = new Matrix(input[0]);
                    in[1] = new Matrix(input[1]);
                    in[2] = new Matrix(input[2]);

                    cnn.setInputs(in);

                    double[] output = CarAI.getOutputFromStringBrands((String) dataElement.getLabel(), cnn.getOutputs().getROWS());

                    //System.out.println(Arrays.toString(output) + " - " + dataElement.getLabel());

                    cnn.feedForward();

                    cnn.backPropagation(output);

                    //if (data % batch_size == 0) nn.updateWeightsAndBiases(learningRate/batch_size);

                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

            t2 = System.currentTimeMillis();
            System.out.println(" (" + (t2 - t1) + ")");

            System.out.println(testNetwork(cnn));
            System.out.println(testNetwork(cnn));
            System.out.println(testNetwork(cnn));
            //System.out.println(cnn);

            cnn.saveNetwork(savingPath + ((int) (100 * testNetwork(cnn))) + "%");
        }


    }

}

