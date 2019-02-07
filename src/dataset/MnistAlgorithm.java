package dataset;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

import functions.ActivationFunctions;
import image.processing.FileManager;
import image.processing.ImageManager;
import neural.network.NeuralNetwork;

public class MnistAlgorithm {
	
	
	public static void main(String[] args) {
		
		double learningRate = 0.01;
		NeuralNetwork neuralNetwork = new NeuralNetwork(ActivationFunctions.Sigmoid, 784, 400, 200, 100, 50, 10);
		String trainingPath = "D:\\Cegep\\Session_4\\IA Data\\mnist_png\\mnist_png\\training";
		String testingPath = "D:\\Cegep\\Session_4\\IA Data\\mnist_png\\mnist_png\\testing";
		String savingPath = "D:\\Cegep\\Session_4\\IA Data\\Network Saves\\MNIST";
		
		ArrayList<String> folders = FileManager.getFoldersFromFolder(trainingPath);
		
		for(int i = 0; i < folders.size(); i++) {
			
			// On parcoure chqque dossier de chaque nombre
			
			int number = Integer.parseInt(folders.get(i));

			ArrayList<BufferedImage> images = FileManager.getImagesFromFolder(trainingPath + "\\" + folders.get(i));
			
			for(int j = 0; j < images.size(); j++) {
				
				//System.out.println("Training for : " + trainingPath + "\\" + folders.get(i) + " : " + j);
				
				// On parcoure chaque image du dossier
				BufferedImage image = images.get(i);
				try {
					
					double[] input = ImageManager.convertGreyValues(ImageManager.getSquaredImage(image, 28));
					double[] output = new double[10];
					
					output[number] = 1;
					
					neuralNetwork.train(input, output, learningRate);
					
				} catch (IOException e) { 
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				
			}
		}
		
		neuralNetwork.saveNetwork(savingPath);
		
	}
}
