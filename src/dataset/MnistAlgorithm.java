package dataset;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

import functions.ActivationFunctions;
import image.processing.FileManager;
import image.processing.ImageManager;
import math.MathTools;
import neural.network.NeuralNetwork;

/**
 * Entraine un reseau de neurone en utilisant le MNIST dataset
 * Classe de test au reseau de neurone
 * @author Gaya Mehenni
 */
public class MnistAlgorithm {

	private static String trainingPath = "D:\\Cegep\\Session_4\\IA Data\\mnist_png\\mnist_png\\training";
	private static String testingPath = "D:\\Cegep\\Session_4\\IA Data\\mnist_png\\mnist_png\\testing";
	private static String savingPath = "D:\\Cegep\\Session_4\\IA Data\\Network Saves\\MNIST\\neural_";
	private static double learningRate = 0.03;
	private static int numberOfEpochs = 200;
	private static int numberOfImagesPerEpoch = 10000;
	private static int batch_size = 15;
	private static int resultCounter = 0;
	private static double lastResult = 0;

	public static void main(String[] args) {

		NeuralNetwork nn = new NeuralNetwork(ActivationFunctions.Sigmoid, 784, 16, 16, 10);

		System.out.println("\nNON TRAINED NETWORK \n" + testNetwork(nn));

		trainNetwork(nn);

		System.out.println("\n" + "TESTING TRAINED NETWORK \n" + testNetwork(nn));

	}

	/**
	 * Methode qui teste le reseau de neurone
	 * @param nn reseau de neurone
	 * @return taux de reussite
	 */
	public static double testNetwork(NeuralNetwork nn) {

		ArrayList<String> folders = FileManager.getFoldersFromFolder(testingPath);

		double result = 0;
		double total = 0;

		for(int i = 0; i < folders.size(); i++) {

			int number = Integer.parseInt(folders.get(i));

			ArrayList<BufferedImage> images = FileManager.getImagesFromFolder(testingPath + "\\" + folders.get(i));

			//System.out.println("IMAGE SIZE : " + images.size());

			for(int j = 0; j < images.size(); j++) {

				BufferedImage image = images.get(j);

				//System.out.println("\t TESTING IMAGE : " + j);

				try {
					double[] input = MathTools.mapArray(ImageManager.convertGreyValues(ImageManager.getSquaredImage(image, 28)), 0, 255, 0, 1);
					double[] expected = new double[10];
					expected[number] = 1;

					nn.feedForward(input);
					//System.out.println("RESULTS : " + number + "\n" + nn.getLayer(nn.getNUMBER_OF_LAYERS() - 1).getOutputsZ());
					double[] output = MathTools.getAsOneDimension(nn.getResults().getMat());
					//System.out.print(MathTools.getHighestIndex(output) + ", " + number + " --\t ");

					//for(int k = 0; k < output.length; k++) System.out.print("\t" + output[k]);
					if (MathTools.getHighestIndex(output) == number) result++;
					total++;

				} catch(IOException e) {
					e.printStackTrace();
				}
			}
		}


//		System.out.println(result + " " + total);
		return result/total;
	}

	/**
	 * Methode qui entraine le reseau
	 * @param nn reseau de neurone
	 */
	public static void trainNetwork(NeuralNetwork nn) {

		ArrayList<String> folders = FileManager.getFoldersFromFolder(trainingPath);
		Batch<DataElement> batch = new Batch<DataElement>();

		System.out.println("Loading data");
		for(int folder = 0; folder < folders.size(); folder++) {
			// On parcoure chaque dossier

			ArrayList<BufferedImage> images = FileManager.getImagesFromFolder(trainingPath + "\\" + folders.get(folder));
			int number = Integer.parseInt(folders.get(folder));
			System.out.print(" " + number);

			for (BufferedImage image : images) {

				DataElement<Integer, BufferedImage> dE = new DataElement<Integer, BufferedImage>(number, image);
				batch.addElementToDataset(dE);
			}

		}

		System.out.println();

		System.out.println("Data loaded (Size : " + batch.getDataset().size() + ")");

		batch.shuffleDataset();

		System.out.println("Training neural network...");

		for(int epoch = 0; epoch < numberOfEpochs; epoch++) {

			System.out.println("Epoch : " + epoch);

			ArrayList<DataElement> epochData = batch.getPartOfDataset(numberOfImagesPerEpoch);

			for(int data = 0; data < epochData.size(); data++) {

				DataElement dataElement = batch.getDataset().get(data);

				try {
					double[] input = MathTools.mapArray(ImageManager.convertGreyValues(ImageManager.getSquaredImage((BufferedImage) dataElement.getData(), 28)), 0, 255, 0, 1);
					double[] output = new double[10];
					int number = (Integer) dataElement.getLabel();
					output[number] = 1;

					//System.out.println(Arrays.toString(output) + " - " + number);

					nn.train(input, output);

					if (data % batch_size == 0) nn.updateWeightsAndBiases(learningRate/batch_size);

				} catch(IOException e) {
					e.printStackTrace();
				}
			}


			System.out.println(testNetwork(nn));



			//System.out.println(nn.getLayer(nn.getNUMBER_OF_LAYERS() - 1));
		}

		try {
			NeuralNetwork.saveNetworkToXML(nn, savingPath + ((int) (100 * testNetwork(nn))) + "%.xml");
		} catch(IOException e) {
			e.printStackTrace();
		}
	}

}
