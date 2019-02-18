package neural.network;

import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
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

	private int NUMBER_OF_LAYERS, INPUT_LAYER_SIZE, OUTPUT_LAYER_SIZE;

	private ActivationFunctions activationFunction;

	public NeuralNetwork() {}

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

			    //System.out.println(layer + " - " + layers[layer]);
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
	 */
	public void backpropagation(double[] expected) {

		Matrix expectedMatrix = new Matrix(MathTools.getAsTwoDimensionalArray(expected));

		// Error in the output layer
		layers[NUMBER_OF_LAYERS - 1].setErrors(Matrix.subtract(expectedMatrix, layers[NUMBER_OF_LAYERS - 1].getOutputs()).getSquaredMatrix().product(layers[NUMBER_OF_LAYERS - 1].getOuputsZ().applyFunctionDerivative(layers[NUMBER_OF_LAYERS - 1].getFunction())));

		// Error in the hidden layers
		for(int layer = NUMBER_OF_LAYERS - 2; layer > 0; layer--) {

            Layer currentLayer = layers[layer];
            Layer nextLayer = layers[layer + 1];
            Matrix wT = nextLayer.getWeights().transpose();
            Matrix nextError = nextLayer.getErrors();
            Matrix dZ = currentLayer.getOuputsZ().applyFunctionDerivative(currentLayer.getFunction());
            Matrix errors = wT.multiply(nextError).product(dZ);
            currentLayer.setErrors(Matrix.sum(currentLayer.getErrors(), errors));

//			Matrix errorLayer = layers[layer + 1].getWeights().transpose().multiply(layers[layer + 1].getErrors()).product(layers[layer].getOuputsZ().applyFunctionDerivative(layers[layer].getFunction()));
//			layers[layer].setErrors(errorLayer);
		}

	}
	/**
	 * Updating each weights and biases with the error associated to each node
	 * @param learningRate constant at which the neural network learns
	 */
	public void updateWeightsAndBiases(double learningRate) {

		for(int layer = NUMBER_OF_LAYERS - 1; layer > 1; layer--) {
            layers[layer].backpropagation(learningRate);
//			Layer currentLayer = layers[layer];
//			Matrix weights = currentLayer.getWeights();
//			Matrix errors = currentLayer.getErrors();
//
//			Layer previousLayer = layers[layer - 1];
//			Matrix outputs = previousLayer.getOutputs();
//
//			for(int neuron = 0; neuron < currentLayer.get; neuron++) {
//
//				for (int prevNeuron = 0; prevNeuron < NB_INPUT_NEURONS; prevNeuron++) {
//
//
//				}
//			}
        }
	}

	/**
	 * Trains the neural network
	 * @param inputs neural network's inputs (going in the input layer)
	 * @param target outputs expected from the neural network
	 */
	public void train(double[] inputs, double[] target) {

		feedForward(inputs);
		backpropagation(target);
	}

	/**
	 * Sets the inputs of the neural network
	 * @param inputs neural network's inputs (going in the input layer)
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
	 * @param path a path where the neural network will be saved
	 */
	public void saveNetwork(String path) {
		try {
			//File f = new File(path + ".dat");
			FileOutputStream fos = new FileOutputStream(path + ".dat");
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			oos.writeObject(this);
			oos.flush();
			oos.close();
			fos.close();
		} catch(IOException e) {
			e.printStackTrace();
			System.out.println(path);
			System.out.println("File hasn't been found");
		}
	}

	/**
	 * Load a neural network
	 * @param path a path which tells where to load the neural network
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

		//System.out.println("RESULTS : " + layers[NUMBER_OF_LAYERS - 1]);
		return layers[NUMBER_OF_LAYERS - 1].getOutputs();
	}

	/**
	 * Gets a certain layer
	 * @param index
	 * @return layer
	 */
	public Layer getLayer(int index) {

		if (index > 0 && index < NUMBER_OF_LAYERS ) return layers[index];
		else {
			System.out.println("Index out of bounds");
			return null;
		}
	}

	/**
	 *
	 * @return
	 */
	@Override
	public String toString() {

		String s = "NEURAL NETWORK \n";

		for(int layer = 0; layer < NUMBER_OF_LAYERS; layer++) {

			//s += layers[layer].getType();
			s += "LAYER " + layer + " : " + layers[layer].getType() + " ------------------------- \n";
			s += "\t" + layers[layer].toString() + "\n";
		}

		return s;
	}
	public Layer[] getLayers() {
		return layers;
	}

	public void setLayers(Layer[] layers) {
		this.layers = layers;
	}

	public ActivationFunctions getActivationFunction() {
		return activationFunction;
	}

	public void setActivationFunction(ActivationFunctions activationFunction) {
		this.activationFunction = activationFunction;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }


    public int getNUMBER_OF_LAYERS() {
		return NUMBER_OF_LAYERS;
	}

	public void setNUMBER_OF_LAYERS(int nUMBER_OF_LAYERS) {
		NUMBER_OF_LAYERS = nUMBER_OF_LAYERS;
	}

	public void setINPUT_LAYER_SIZE(int iNPUT_LAYER_SIZE) {
		INPUT_LAYER_SIZE = iNPUT_LAYER_SIZE;
	}

	public void setOUTPUT_LAYER_SIZE(int oUTPUT_LAYER_SIZE) {
		OUTPUT_LAYER_SIZE = oUTPUT_LAYER_SIZE;
	}

	public int getINPUT_LAYER_SIZE() {
		return INPUT_LAYER_SIZE;
	}

	public int getOUTPUT_LAYER_SIZE() {
		return OUTPUT_LAYER_SIZE;
	}

	/**
	 * methode pour sauvegarder un reseau sour format XML
	 * @param nn : le reseau de neurones a sauvegarder
	 * @param path : le buildpath ou sauvegarder le reseau
	 */
	//Auteurs : source web : https://www.edureka.co/blog/serialization-of-java-objects-to-xml-using-xmlencoder-decoder/

	public static void saveNetworkToXML (NeuralNetwork nn, String path) throws IOException
	{
		XMLEncoder encoder=null;
		try{
			encoder=new XMLEncoder( new BufferedOutputStream(new FileOutputStream(path)));
            encoder.writeObject(nn);
            encoder.close();
		}catch(FileNotFoundException fileNotFound){
		    fileNotFound.printStackTrace();
			System.out.println("ERROR: While Creating or Opening the File : " + path);
		}

	}
	/**
	 * permet de lire un reseau sauvergarde sous format XML
	 * @param path : le path du reseau a charger
	 * @return le reseau charge a partir du fichier
	 * @throws IOException  
	 */
	//auteurs Source web : https://www.edureka.co/blog/serialization-of-java-objects-to-xml-using-xmlencoder-decoder/

	public static NeuralNetwork loadNetworkFromXML(String path) throws IOException {
		XMLDecoder decoder=null;
		try {
			decoder=new XMLDecoder(new BufferedInputStream(new FileInputStream(path)));
            NeuralNetwork decodedNetwork = (NeuralNetwork) decoder.readObject();
            decoder.close();
            return decodedNetwork;
		} catch (FileNotFoundException e) {
		    e.printStackTrace();
			System.out.println("ERROR: File "+path+".xml not found");
			return null;
		}
	}

	/**
	 * test du chargement de la sauvegarde du reseau
	 * @param args
	 * @throws IOException
	 */
	//auteur : Simon Daze
	public static void main(String[] args) throws IOException {
		NeuralNetwork nn = new NeuralNetwork(ActivationFunctions.Sigmoid, 3,2,1,2);
		double[] inputs = new double[]{1,2,3};
		double[] outputs = new double[] {2,1};
		nn.train(inputs, outputs);
		nn.updateWeightsAndBiases(0.1);
		System.out.println(nn);
		String path = "C:\\Users\\mehga\\Desktop\\Network Saves\\neural2.xml";
		/*NeuralNetwork nn3 = new NeuralNetwork();
		nn3.setActivationFunction(ActivationFunctions.Sigmoid);
		NeuralNetwork.saveNetworkToXML(nn3, "res/network_saves/neural");*/
		NeuralNetwork.saveNetworkToXML(nn, path);
		NeuralNetwork nn2 = NeuralNetwork.loadNetworkFromXML(path);
		//System.out.println(NeuralNetwork.loadNetwork(path));

		System.out.println(nn2);

	}





}
