package algorithm;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JPanel;
import javax.swing.JProgressBar;

import dataset.Batch;
import dataset.DataElement;
import functions.ActivationFunctions;
import image.processing.FileManager;
import image.processing.ImageManager;
import listeners.TrainingEvents;
import math.MathTools;
import neural.network.NeuralNetwork;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import test.CarAlgorithm;

/**
 * Classe qui contient le reseau de neurone et qui s'occupe de l'entrainer, de la tester, de la sauvegarder et de la charger
 * Cette classe contient une reference a l'application pour pouvoir faire une animation de l'entrainement
 * @author Gaya Mehenni
 *
 * A faire :
 * Ajout d'un evenement CarAI qui est lance lorsque l'animation est terminee
 *
 */
public class CarAI extends JPanel implements Runnable{

    private String trainingPath = "D:\\cegep prog\\\\Dataset_Voiture_Moto_Camion\\training";
    private String testingPath = "D:\\cegep prog\\Dataset_Voiture_Moto_Camion\\testing";
    //private String savingPath = "D:\\Cegep\\Session_4\\IA Data\\neuralTrained";

    private double learningRate = 0.3;
    private int numberOfEpochs, batchSize, numberImagesPerFolderMax = 3000;

    private int numberTotalOfImages = 0;
    private ArrayList<TrainingEvents> events = new ArrayList<>();

    // ArrayList qui contient toutes les images;
    private Batch<DataElement> batch = new Batch<DataElement>();
    private BufferedImage img;

    private boolean enCours = false;
    private boolean dataLoaded = false;
    private int counter = 0;
    private int data = 0;

    private JProgressBar bar;

    /*
        La taille des images en entree du reseau de neurone est fixee a 28x28
     */
    private final int IMAGE_SIZE = 28;

    /*
        Le reseau de neurone identifie 3 types de vehicules
        Voitures
        Motos
        Camions
     */
    private final int NUMBER_OF_OUTPUTS = 3;

    /*
     * La taille du reseau de neurone est fixee a input - 64 - 64 - 64 - 3;
     */
    private NeuralNetwork neuralNetwork;

    /**
     * Constructeur
     * @param learningRate taux d'apprentissage
     * @param numberOfEpochs nombre d'epoch
     * @param batchSize taille d'un seul batch
     */
    public CarAI(double learningRate, int numberOfEpochs, int batchSize) {
        this.learningRate = learningRate;
        this.numberOfEpochs = numberOfEpochs;
        this.batchSize = batchSize;

        neuralNetwork = new NeuralNetwork(ActivationFunctions.Sigmoid, IMAGE_SIZE * IMAGE_SIZE * 3, 28, 16, NUMBER_OF_OUTPUTS);

        setBackground(Color.WHITE);
    }

    /**
     * Methode qui redessine les composants du panel
     * @param g contexte graphique
     */
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        if (dataLoaded) g2d.drawImage(img, 0, 0, getWidth(), getHeight(), null);
        else {
            String s = "Chargement des images";
            String s2 = "(" + batch.getDataset().size() + "/" + numberTotalOfImages + ")";
            g2d.drawString(s, getWidth()/2 - 60, getHeight()/2);
            g2d.drawString(s2, getWidth()/2 - 10, getHeight()/2 + 30);
        }
    }

    /**
     * Methode qui s'occupe de l'animation du panel
     */
    @Override
    public void run() {

        while(enCours) {

            if (dataLoaded) {
                System.out.println("Epoch : " + counter);

                // On entraine le reseau
                trainNetwork();

                // On change la valeur de la barre de progression
                try {
                    bar.setMaximum(numberOfEpochs);
                    bar.setValue(counter);
                } catch (NullPointerException e) {
                    System.out.println("Ne peut pas changer la valeur de la barre de progression, car l'algorithme ne possede pas de reference a celle-ci");
                }

                // Si le nombre d'epoch est termine, on arrete tout
                if (counter >= numberOfEpochs) {
                    trainingEnded();
                    //neuralNetwork.saveNetwork(savingPath);
                    System.out.println(counter + ", " + numberOfEpochs);
                    counter = 0;
                    enCours = false;
                }

                counter++;
            } else {
                loadData();
            }

            try{
                Thread.sleep(75);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Methode qui demarre l'application
     */
    public void demarrer() {

        System.out.println("Training neural network...");

        Thread t = new Thread(this);
        t.start();
        enCours = true;
    }

    /**
     * Methode qui s'occupe de charger toutes les images d'entrainement
     * L'algorithme ne prend en compte qu'une seule extension de fichier d'image (.png ou .jpg, mais pas les deux)
     */
    public void loadData() {

        ArrayList<String> folders = FileManager.getFoldersFromFolder(trainingPath);

        for(int i = 0; i < folders.size(); i++) {
            File f = new File(trainingPath + "\\" + folders.get(i));
            if (f.listFiles().length > numberImagesPerFolderMax) numberTotalOfImages += numberImagesPerFolderMax;
            else numberTotalOfImages += f.listFiles().length;
        }

        System.out.println("Loading data  : " + numberTotalOfImages);
        for(int folder = 0; folder < folders.size(); folder++) {
            // On parcoure chaque dossier

            System.out.println("\t- " + folders.get(folder));

            ArrayList<BufferedImage> images = FileManager.getImagesFromFolder(trainingPath + "\\" + folders.get(folder), numberImagesPerFolderMax);

            for(BufferedImage i : images) {
                DataElement<String, BufferedImage> data = new DataElement<>(folders.get(folder), i);
                batch.addElementToDataset(data);
                repaint();
            }

            System.out.println("\t" + folders.get(folder) + " : " + batch.getDataset().size());

        }

        System.out.println();

        System.out.println("Data loaded (Size : " + batch.getDataset().size() + ")");

        dataLoaded = true;

    }

    /**
     * Methode qui passe une image a travers le reseau de neurone et retourne la valeur predite par le reseau
     * @param img image
     * @return valeur predite par le reseau
     */
    public double[] testNetwork(BufferedImage img) {
        double[] out = new double[neuralNetwork.getOUTPUT_LAYER_SIZE()];
        try {
            neuralNetwork.feedForward(ImageManager.convertRGB(ImageManager.getSquaredImage(img, IMAGE_SIZE)));
            out = MathTools.getAsOneDimension(neuralNetwork.getResults().getMat());
        } catch (IOException e){
            System.out.println("L'image n'a pas pu etre chargee pour le test");
        }
        return out;
    }

    /**
     * Methode qui s'occupe d'entrainer le reseau de neurone en utilisant les proprietes de la classe comme hyperparametres
     */
    public void trainNetwork() {

        batch.shuffleDataset();
        ArrayList<DataElement> epochData = batch.getDataset();

        System.out.println("\t" + epochData.size());

        for (data = 0; data < epochData.size(); data++) {

            DataElement dataElement = batch.getDataset().get(data);

            try {
                double[] input = MathTools.mapArray(ImageManager.convertRGB(ImageManager.getSquaredImage((BufferedImage) dataElement.getData(), IMAGE_SIZE)), 0, 255, 0, 1);
                double[] output = getOutputFromString((String) dataElement.getLabel(), neuralNetwork.getOUTPUT_LAYER_SIZE());

                neuralNetwork.train(input, output);

                img = (BufferedImage) (dataElement.getData());
                if (data % 50 == 0) repaint();

                if (data % batchSize == 0) neuralNetwork.updateWeightsAndBiases(learningRate);

            } catch (IOException e){
                System.out.println("Erreur lors du chargement de l'image : " + data);
            }


        }
        //System.out.println(CarAlgorithm.testNetwork(neuralNetwork));


    }

    /**
     * Methode qui met l'entrainement en pause
     */
    public void pauseTraining() {
        enCours = false;
    }

    /**
     * Methode qui sauvegarde le reseau de neurone
     * @param path chemin d'acces de la sauvegarde du reseau
     */
    public void saveNetwork(String path){
        neuralNetwork.saveNetwork(path);
    }

    /**
     * Methode qui charge le reseau de neurone
     * @param path chemin d'acces du chargement du reseau
     */
    public void loadNetwork(String path) {
        neuralNetwork = NeuralNetwork.loadNetwork(path);
    }

    /**
     * Methode qui leve les evenements
     */
    public void trainingEnded() {
        for(int i = 0; i < events.size(); i++) {
            events.get(i).trainingEnded();
        }
    }

    /**
     * Methode qui ajoute les evenements
     * @param t evenement
     */
    public void addTrainingEvent(TrainingEvents t) {
        events.add(t);
    }

    /**
     * Methode qui convertit une chaine de caractere representant le type de vehicule en tableau de sortie pour les reseaux de neurones
     * @param s nom du label
     * @param numberOfOutputs nombre de sorties du reseau
     * @return tableau de sortie attendu du neurone
     */
    public static double[] getOutputFromString(String s, int numberOfOutputs) {

        double[] a = new double[numberOfOutputs];

        switch (s) {
            case "Voiture":
                a[0] = 1;
                break;
            case "Moto":
                a[1] = 1;
                break;
            case "Camion":
                a[2] = 1;
                break;
            default:
                System.out.println("L'algorithme n'a pas reconnue le label");
                break;
        }

        return a;
    }

    /**
     * Methode qui convertit une chaine de caractere representant le type de vehicule en tableau de sortie pour les reseaux de neurones avec seulement 2 sorties
     * @param s nom de label
     * @param numberOfOutputs nombres de sorties du reseau
     * @return tableau de sortie attendu du neurone
     */
    public static double[] getOutputFromString2Vehicles(String s, int numberOfOutputs) {
        double[] a = new double[numberOfOutputs];

        switch (s) {
            case "Voiture":
                a[0] = 1;
                break;
            case "Camion":
                a[1] = 1;
                break;
            default:
                System.out.println("L'algorithme n'a pas reconnue le label");
                break;
        }

        return a;
    }

    /**
     * Methode qui convertit une chaine de caractere representant la marque d'un vehicule en tableau de sortie pour les reseaux de neurones
     * @param s nom du label
     * @param numberOfOutputs nombre de sorties pour les reseaux
     * @return tableau de sortie attendu du reseau
     */
    public static double[] getOutputFromStringBrands(String s, int numberOfOutputs) {

        double[] a = new double[numberOfOutputs];

        switch (s) {
            case "BMW":
                a[0] = 1;
                break;
            case "Chevrolet":
                a[1] = 1;
                break;
            case "Toyota":
                a[2] = 1;
                break;
            default:
                System.out.println("L'algorithme n'a pas reconnue le label");
                break;
        }

        return a;
    }


    /**
     * Methode qui verifie si un chemin d'acces est valide pour l'entrainement
     * @param trainingPath chemin d'acces
     * @return valeur booleenne qui dit si le chemin d'acces est valide ou pas
     */
    public boolean isPathValid(String trainingPath) {

        String back = this.trainingPath;
        this.trainingPath = trainingPath;

        try{
            loadData();
            return true;
        } catch (NullPointerException e) {
            return false;
        }

    }

    /**
     * Retourne le taux d'apprentissage du reseau
     * @return
     */
    public double getLearningRate() {
        return learningRate;
    }

    /**
     * Change le taux d'apprentissage du reseau
     * @param learningRate
     */
    public void setLearningRate(double learningRate) {
        this.learningRate = learningRate;
    }

    /**
     * Retourne le nombre d'epoch
     * @return
     */
    public int getNumberOfEpochs() {
        return numberOfEpochs;
    }

    /**
     * Change le nombre d'epoch
     * @param numberOfEpochs
     */
    public void setNumberOfEpochs(int numberOfEpochs) {
        this.numberOfEpochs = numberOfEpochs;
    }

    /**
     * Retourne la taille d'un seul batch
     * @return
     */
    public int getBatchSize() {
        return batchSize;
    }

    /**
     * Change la taille d'un seul batch
     * @param batchSize
     */
    public void setBatchSize(int batchSize) {
        this.batchSize = batchSize;
    }

    /**
     * Retourne la bar de progression liee au reseau
     * @return
     */
    public JProgressBar getBar() {
        return bar;
    }

    /**
     * Change la barre de progression liee au reseau
     * @param bar
     */
    public void setBar(JProgressBar bar) {
        this.bar = bar;
    }

    /**
     * Retourne le chemin d'acces vers les images d'entrainement
     * @return
     */
    public String getTrainingPath() {
        return trainingPath;
    }

    /**
     * Change le chemin d'acces vers les images d'entrainement
     * @param trainingPath
     */
    public void setTrainingPath(String trainingPath) {
        this.trainingPath = trainingPath;
    }

    /**
     * Retourne le chemin d'acces vers les images de test
     * @return
     */
    public String getTestingPath() {
        return testingPath;
    }
    /**
     * Change le chemin d'acces vers les images de test
     * @param testingPath
     */
    public void setTestingPath(String testingPath) {
        this.testingPath = testingPath;
    }
}
