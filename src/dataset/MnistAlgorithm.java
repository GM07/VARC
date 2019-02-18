package dataset;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

import functions.ActivationFunctions;
import image.processing.FileManager;
import image.processing.ImageManager;
import math.MathTools;
import neural.network.Batch;
import neural.network.NeuralNetwork;

import javax.xml.crypto.Data;


/**
 * @author Gaya Mehenni
 * Trains a neural network with the MNIST Dataset using Stochastic Gradient Descent
 */
public class MnistAlgorithm {

	/*
		TO DO :
			- SHUFFLE THE DATASET
			- CREATE A BATCH CLASS (LABEL -> INPUTS)
	 */

	private static String trainingPath = "D:\\Cegep\\Session_4\\IA Data\\mnist_png\\mnist_png\\training";
	private static String testingPath = "D:\\Cegep\\Session_4\\IA Data\\mnist_png\\mnist_png\\testing";
	private static String savingPath = "D:\\Cegep\\Session_4\\IA Data\\Network Saves\\MNIST\\save_2";
	private static double learningRate = 0.4;
	private static int numberOfEpochs = 500;
	private static int numberOfImagesPerEpoch = 100;
	private static int batch_size = 5;
	private static int resultCounter = 0;
	private static double lastResult = 0;

	public static void main(String[] args) {

		//NeuralNetwork nn = NeuralNetwork.loadNetwork("D:\\Cegep\\Session_4\\IA Data\\Network Saves\\MNIST.dat");

		NeuralNetwork nn = new NeuralNetwork(ActivationFunctions.Sigmoid, 784, 16, 16, 10);
		//NeuralNetwork nn2 = new NeuralNetwork(ActivationFunctions.Sigmoid, 784, 800, 400, 200, 100, 50, 10);

		System.out.println("\nNON TRAINED NETWORK \n" + testNetwork(nn));

		trainNetwork(nn);

		System.out.println("\n" + "TESTING TRAINED NETWORK \n" + testNetwork(nn));
		//System.out.println("\n" + "TESTING NON-TRAINED NETWORK \n" + testNetwork(nn2));

	}

	public static double testNetwork(NeuralNetwork nn) {

		ArrayList<String> folders = FileManager.getFoldersFromFolder(testingPath);

		double result = 0;
		double total = 0;

		for(int i = 0; i < folders.size(); i++) {

			int number = Integer.parseInt(folders.get(i));

			ArrayList<BufferedImage> images = FileManager.getImagesFromFolder(testingPath + "\\" + folders.get(i));

			//System.out.println("IMAGE SIZE : " + images.size());

			for(int j = 0; j < images.size()/100; j++) {

				BufferedImage image = images.get(j);

				//System.out.println("\t TESTING IMAGE : " + j);

				try {
					double[] input = ImageManager.convertGreyValues(ImageManager.getSquaredImage(image, 28));
					double[] expected = new double[10];
					expected[number] = 1;

					nn.feedForward(input);
					//System.out.println("RESULTS : \n" + nn.getResults());
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


		System.out.println(result + " " + total);
		return result/total;
	}

	public static void trainNetwork(NeuralNetwork nn) {

		ArrayList<String> folders = FileManager.getFoldersFromFolder(trainingPath);
		Batch<DataElement> batch = new Batch<DataElement>();

		System.out.println("Loading data");
		for(int folder = 0; folder < folders.size(); folder++) {
			// On parcoure chaque dossier

			ArrayList<BufferedImage> images = FileManager.getImagesFromFolder(trainingPath + "\\" + folders.get(folder));

			for(int file = 0; file < images.size(); file++) {

				BufferedImage image = images.get(file);
				batch.addElementToDataset(new DataElement<>(Integer.parseInt(folders.get(folder)), image));
			}
		}

		System.out.println("Data loaded (Size : " + batch.getDataset().size() + ")");
		System.out.println("Training neural network...");

		for(int epoch = 0; epoch < numberOfEpochs; epoch++) {

			System.out.println("Epoch : " + epoch);

			try {
				NeuralNetwork.saveNetworkToXML(nn, "res/network_saves/neural_network_save_1.xml");
			} catch(IOException e) {
				e.printStackTrace();
			}

			// Shuffling the dataset to get better results
			batch.shuffleDataset();

			ArrayList<DataElement> epochData = batch.getPartOfDataset(numberOfImagesPerEpoch);

			for(int data = 0; data < epochData.size(); data++) {

				DataElement dataElement = batch.getDataset().get(data);

				try {
					double[] input = ImageManager.convertGreyValues(ImageManager.getSquaredImage((BufferedImage) dataElement.getData(), 28));
					double[] output = new double[10];
					output[(int) dataElement.getLabel()] = 1;

					nn.train(input, output);

					if (data % batch_size == 0) nn.updateWeightsAndBiases(learningRate);

				} catch(IOException e) {
					e.printStackTrace();
				}
			}

			double result = testNetwork(nn);
			double delta = result - lastResult;
			System.out.println(result + " - " + delta);
			lastResult = result;
			if (delta <= 0) {
				nn.loadNetwork("res/network_saves/neural_network_save_1");
			}


			//System.out.println(nn.getLayer(nn.getNUMBER_OF_LAYERS() - 1));
		}

	}

	/*
	public static void trainNetwork(NeuralNetwork neuralNetwork) {

		ArrayList<String> folders = FileManager.getFoldersFromFolder(trainingPath);

		for(int epoch = 0; epoch < numberOfEpochs; epoch++) {

			System.out.println("EPOCHS : " + epoch);

			for (int i = 0; i < folders.size(); i++) {

				System.out.println("\tDOSSIER : " + i);

				// On parcoure le dossier de chaque nombre
				int number = Integer.parseInt(folders.get(i));

				ArrayList<BufferedImage> images = FileManager.getImagesFromFolder(trainingPath + "\\" + folders.get(i));

				System.out.println("\tNOMBRE D'IMAGES A TESTER : " + images.size()/5);

				for (int j = 0; j < images.size()/100; j++) {

					//System.out.println("\t\t IMAGE : " + j);

					// On parcoure chaque image du dossier
					BufferedImage image = images.get(j);
					try {

						double[] input = ImageManager.convertGreyValues(ImageManager.getSquaredImage(image, 28));
						double[] output = new double[10];

						output[number] = 1;

						neuralNetwork.train(input, output, learningRate);

					} catch (IOException e) {

						e.printStackTrace();
					}

				}
			}
		}

		neuralNetwork.saveNetwork(savingPath);
	}
	*/
}
