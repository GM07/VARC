package neural.network;

import java.beans.ExceptionListener;
import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import functions.ActivationFunctions;
import math.MathTools;
import math.Matrix;

public class NeuralNetwork implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 643116515073485682L;

	private Layer[] layers;

	private final int NUMBER_OF_LAYERS, INPUT_LAYER_SIZE, OUTPUT_LAYER_SIZE;

	private ActivationFunctions activationFunction;
	
	public NeuralNetwork() {

		NUMBER_OF_LAYERS = 0;
		INPUT_LAYER_SIZE = 0;
		OUTPUT_LAYER_SIZE = 0;
	}
	
	public NeuralNetwork(ActivationFunctions function, int... configuration) {

		// Activation function
		activationFunction = function;

		NUMBER_OF_LAYERS = configuration.length;
		INPUT_LAYER_SIZE = configuration[0];
		OUTPUT_LAYER_SIZE = configuration[NUMBER_OF_LAYERS - 1];

		layers = new Layer[configuration.length];

		// Configurating each layer
		for(int layer = 0; layer < NUMBER_OF_LAYERS; layer++) {


			if (layer == 0) {

				// Input Layer
				layers[layer] = new Layer(INPUT_LAYER_SIZE, INPUT_LAYER_SIZE, LayerType.InputLayer, activationFunction);

			} else if (layer == configuration.length - 1) {

				// Output layer
				layers[layer] = new Layer(OUTPUT_LAYER_SIZE, configuration[NUMBER_OF_LAYERS - 2], LayerType.OutputLayer, activationFunction);

			} else {

				// Hidden layer
				layers[layer] = new Layer(configuration[layer], configuration[layer - 1], LayerType.HiddenLayer, activationFunction);

			}


		}

		for(int layer = 0; layer < NUMBER_OF_LAYERS; layer++) {

			if (layer != 0) {
				layers[layer].setInputs(layers[layer - 1].getOutputs());
			}
		}
	}

	/**
	 * Gets the output of the neuron given a certain input by using the feed forward algorithm
	 * @param inputs
	 */
	public void feedForward(double[] inputs) {
		
		if (inputs.length == INPUT_LAYER_SIZE) {

			Matrix matrixInputs = new Matrix(MathTools.getAsTwoDimensionalArray(inputs));

			layers[0].setInputs(matrixInputs);

			for(int layer = 0; layer < layers.length; layer++) {

				if (layers[layer].getType() != LayerType.InputLayer) layers[layer].setInputs(layers[layer - 1].getOutputs());
				layers[layer].feedForward();
			}
			
		} else {
			
			System.out.println("ERROR WHILE TRYING TO FEED FORWARD : INPUT SIZE (" + inputs.length + ") IS NOT THE SAME AS THE NETWORK'S (" + INPUT_LAYER_SIZE + ")");

		}
	}

	/**
	 * Applies the backpropagation algorithm to train the network
	 * @param expected
	 * @param learningRate
	 */
	public void backpropagation(double[] expected, double learningRate) {

		
		// Error in the output layer
		Matrix expectedMatrix = new Matrix(MathTools.getAsTwoDimensionalArray(expected));
		
		layers[NUMBER_OF_LAYERS - 1].setErrors(Matrix.subtract(expectedMatrix, layers[NUMBER_OF_LAYERS - 1].getOutputs()).product(layers[NUMBER_OF_LAYERS - 1].getOutputs().applyFunctionDerivative(activationFunction)));
		
		// Error in the hidden layers
		for(int layer = NUMBER_OF_LAYERS - 2; layer > 0; layer--) {
			
			Matrix errorLayer = layers[layer + 1].getWeights().transpose().multiply(layers[layer + 1].getErrors()).product(layers[layer].getOutputs().applyFunctionDerivative(layers[layer].getFunction()));
			layers[layer].setErrors(errorLayer);
		}
		
		for(int layer = 0; layer < NUMBER_OF_LAYERS; layer++) {
			
			layers[layer].backpropagation(learningRate);
		}
		
	}
	
	/**
	 * Trains the neural network
	 * @param inputs
	 * @param target
	 * @param learningRate
	 */
	public void train(double[] inputs, double[] target, double learningRate) {
		
		feedForward(inputs);
		backpropagation(target, learningRate);
	}

	
	/**
	 * Sets the inputs of the neural network
	 * @param inputs
	 */
	public void setInputs(double[] inputs) {

		if (inputs.length == INPUT_LAYER_SIZE) {

			Matrix matrixInputs = new Matrix(MathTools.getAsTwoDimensionalArray(inputs));
			layers[0].setInputs(matrixInputs);

		} else {
			System.out.println("INPUT SIZE IS NOT THE SAME AS THE NETWORK'S");
			System.out.println("INPUT SIZE : " + inputs.length);
			System.out.println("NETWORK'S INPUT SIZE : " + INPUT_LAYER_SIZE);
		}

	}
	

	/**
	 * Save the neural network
	 * @param path
	 */
	public void saveNetwork(String path) {
		try {
			//File f = new File(path + ".dat");
			FileOutputStream fos = new FileOutputStream(path + ".dat");
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			oos.writeObject(this);
			//oos.flush();
			oos.close();
			//fos.close();
		} catch(IOException e) {
			e.printStackTrace();
			System.out.println(path);
			System.out.println("File hasn't been found");
		}
	}

	/**
	 * Load a neural network
	 * @param path
	 * @return neural network
	 */
	public static NeuralNetwork loadNetwork(String path){
		try {
			File f = new File(path);
			FileInputStream fis = new FileInputStream(f);
			ObjectInputStream ois = new ObjectInputStream(fis);
			NeuralNetwork network = (NeuralNetwork) ois.readObject();
			ois.close();
			fis.close();
			return network;
		} catch(IOException e) {
			System.out.println("File doesn't exist");
			return null;
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			return null;
		} catch(NullPointerException e) {
			return null;
		}
	}

	
	/**
	 * Returns the last layer of the neural network
	 * @return
	 */
	public Matrix getResults() {
		
		System.out.println("RESULTS : " + layers[NUMBER_OF_LAYERS - 1]);
		return layers[NUMBER_OF_LAYERS - 1].getOutputs();
	}

	public String toString() {

		String s = "NEURAL NETWORK \n";

		for(int layer = 0; layer < NUMBER_OF_LAYERS; layer++) {

			//s += layers[layer].getType();
			s += "LAYER " + layer + " : " + layers[layer].getType() + " ------------------------- \n";
			s += layers[layer].toString() + "\n";
		}

		return s;
	}
	
	
	//methode pour sauvegarder un reseau sour forme xml
	public static void saveNetworkToXML (NeuralNetwork nn, String path) throws IOException
	{
	    FileOutputStream fos = new FileOutputStream(path + ".xml");
	    XMLEncoder encoder = new XMLEncoder(fos);
	    encoder.setExceptionListener(new ExceptionListener() {
	            public void exceptionThrown(Exception e) {
	                System.out.println("Exception! :"+e.toString());
	            }
	    });
	    encoder.writeObject(nn);
	    encoder.close();
	    fos.close();
	}
	public static NeuralNetwork loadNetworkFromXML(String path) throws IOException {
	    FileInputStream fis = new FileInputStream(path + ".xml");
	    XMLDecoder decoder = new XMLDecoder(fis);
	   NeuralNetwork decodedSettings = (NeuralNetwork) decoder.readObject();
	    decoder.close();
	    fis.close();
	    return decodedSettings;
	}
	
	
}
