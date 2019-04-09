package test;

import algorithm.CarAI;
import dataset.Batch;
import dataset.DataElement;
import functions.ActivationFunctions;
import image.processing.FileManager;
import image.processing.ImageManager;
import math.MathTools;
import neural.network.NeuralNetwork;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Classe de test pour trouver l'efficacite du reseau de neurone
 * @author Gaya Mehenni
 */
public class CarAlgorithm {

    private static String trainingPath = "D:\\Cegep\\Session_4\\IA Data\\Dataset_Voiture_Moto_Camion\\training";
    private static String testingPath = "D:\\Cegep\\Session_4\\IA Data\\Dataset_Voiture_Moto_Camion\\testing";
    private static String savingPath = "D:\\Cegep\\Session_4\\IA Data\\Network Saves\\trained_network_";
    private static double learningRate = 3E-5;
    private static int numberOfEpochs = 200;
    private static int numberOfImagesPerEpoch = 20000;
    private static int batch_size = 1;
    private static int resultCounter = 0;
    private static double lastResult = 0;
    private static double delta = 0;

    /**
     * Methode principale qui lance l'application
     * @param args
     */
    public static void main(String[] args) {

        System.out.print("STARTING PROGRAM...");

        NeuralNetwork nn = new NeuralNetwork(ActivationFunctions.Sigmoid, 28 * 28 * 3, 64, 32, 3);

        System.out.println("\nWITHOUT TRAINING\n" + testNetwork(nn));

        trainNetwork(nn);

        System.out.println("\nAFTER TRAINING\n" + testNetwork(nn));

    }

    /**
     * Methode qui teste l'efficacite du reseau de neurone
     * @param nn reseau de neurone
     * @return succes du reseau (%)
     */
    public static double testNetwork(NeuralNetwork nn){

        ArrayList<String> folders = FileManager.getFoldersFromFolder(testingPath);

        double result = 0;
        double total = 0;

        for(int i = 0; i < folders.size(); i++) {

            String type = folders.get(i);

            ArrayList<BufferedImage> images = FileManager.getImagesFromFolder(testingPath + "\\" + folders.get(i));

            //System.out.println("IMAGE SIZE : " + images.size());

            for(int j = 0; j < images.size(); j++) {

                BufferedImage image = images.get(j);

                //System.out.println("\t TESTING IMAGE : " + j);

                try {
                    double[] input = MathTools.mapArray(ImageManager.convertRGB(ImageManager.getSquaredImage(image, 28)), 0, 255, 0, 1);
                    double[] expected = CarAI.getOutputFromString(type, nn.getOUTPUT_LAYER_SIZE());

                    nn.feedForward(input);

                    double[] outputs = MathTools.getAsOneDimension(nn.getResults().getMat());

                    //System.out.println(nn.getResults());
                    //System.out.println(MathTools.getHighestIndex(outputs) + " - " + MathTools.getHighestIndex(expected));
                    if (MathTools.getHighestIndex(outputs) == MathTools.getHighestIndex(expected)) result++;
                    total++;

                } catch(IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return result/total;
    }

    /**
     * Methode qui entraine un reseau en fonction du chemin d'entrainement de la classe
     * @param nn reseau de neurone
     */
    public static void trainNetwork(NeuralNetwork nn) {

        ArrayList<String> folders = FileManager.getFoldersFromFolder(trainingPath);
        Batch<DataElement> batch = new Batch<DataElement>();

        System.out.println("Loading data");
        for(int folder = 0; folder < folders.size(); folder++) {
            // On parcoure chaque dossier

            ArrayList<BufferedImage> images = FileManager.getImagesFromFolder(trainingPath + "\\" + folders.get(folder));
            String type = folders.get(folder);
            System.out.print(" " + type);

            for (BufferedImage image : images) {

                DataElement<String, BufferedImage> dE = new DataElement<String, BufferedImage>(type, image);
                batch.addElementToDataset(dE);
            }

        }

        System.out.println();

        System.out.println("Data loaded (Size : " + batch.getDataset().size() + ")");

        batch.shuffleDataset();

        System.out.println("Training neural network...");

        for(int epoch = 0; epoch < numberOfEpochs; epoch++) {

            System.out.println("Epoch : " + epoch + " (" + numberOfImagesPerEpoch + ")");

            ArrayList<DataElement> epochData = batch.getPartOfDataset(numberOfImagesPerEpoch);

            for(int data = 0; data < epochData.size(); data++) {

                DataElement dataElement = batch.getDataset().get(data);

                try {
                    //double[] input = MathTools.mapArray(ImageManager.convertGreyValues(ImageManager.getSquaredImage((BufferedImage) dataElement.getData(), 28)), 0, 255, 0, 1);
                    double[] input = MathTools.mapArray(ImageManager.convertRGB(ImageManager.getSquaredImage((BufferedImage) dataElement.getData(), 28)), 0, 255, 0, 255);
                    double[] output = CarAI.getOutputFromString((String) dataElement.getLabel(), nn.getOUTPUT_LAYER_SIZE());

                    //System.out.println(Arrays.toString(output) + " - " + (String) (dataElement.getLabel()));

                    nn.train(input, output);

                    if (data % batch_size == 0) {
                        nn.updateWeightsAndBiases(learningRate/batch_size);
                    }

                } catch(IOException e) {
                    e.printStackTrace();
                }
            }

            double current = testNetwork(nn);

//            delta = current - lastResult;
//            if (delta > 0) {
//                learningRate /= (1 + (delta));
//            } else {
//                learningRate *= (1 + (-delta));
//            }
            lastResult = current;
            System.out.println(lastResult + ", " + learningRate);


            if (current > 0.70) {
                nn.saveNetwork(savingPath + ((int) (100 * testNetwork(nn))) + "%");
            }
            //System.out.println(nn.getLayer(nn.getNUMBER_OF_LAYERS() - 1));
        }

        //nn.saveNetwork(savingPath + ((int) (100 * testNetwork(nn))) + "%");
    }

}
