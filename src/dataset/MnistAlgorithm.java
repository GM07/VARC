package dataset;

import java.util.ArrayList;

import functions.ActivationFunctions;
import image.processing.FileManager;
import neural.network.NeuralNetwork;

public class MnistAlgorithm {
	
	
	public static void main(String[] args) {
		
		
		NeuralNetwork neuralNetwork = new NeuralNetwork(ActivationFunctions.Sigmoid, 784, 400, 200, 100, 50, 10);
		String trainingPath = "D:\\Cegep\\Session_4\\IA Data\\mnist_png\\mnist_png\\training";
		String testingPath = "D:\\Cegep\\Session_4\\IA Data\\mnist_png\\mnist_png\\testing";
		
		ArrayList<String> folders = FileManager.getFoldersFromFolder(trainingPath);
		
		for(int i = 0; i < folders.size(); i++) {
			
			// On parcoure chqque dossier de chaque nombre
			
			int output = Integer.parseInt(folders.get(i));

		}
		
	}
}
