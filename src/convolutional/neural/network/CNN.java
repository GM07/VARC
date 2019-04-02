package convolutional.neural.network;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;

import functions.ActivationFunctions;
import math.MathTools;
import math.Matrix;
/**
 * Le reseau de convolution contenant differents types de layers
 * @author Simon Daze
 * @author Gaya Mehenni
 *
 */
public class CNN implements Serializable {

	private static final long serialVersionUID = -3966726810488689816L;
	private int nbLayers, numberOfOutputs, inputSize, numberOfChannels;
	private double learningRate;
	private ArrayList<CNNLayer> layers;
	private Matrix[] inputs;
	private Matrix outputs;
	private ActivationFunctions activationFunction;

	/**
	 * constructeur du resea

	 */
	public CNN(ActivationFunctions activationFunction, int inputSize, int numberOfChannels, int numberOfOutputs, double learningRate) {
		layers = new ArrayList<>();
		this.inputSize = inputSize;
		this.numberOfChannels = numberOfChannels;
		this.activationFunction = activationFunction;
		this.numberOfOutputs = numberOfOutputs;
		this.learningRate = learningRate;
		outputs = new Matrix(numberOfOutputs, 1);

		this.inputs = new Matrix[numberOfChannels];
		for(int i = 0; i < numberOfChannels; i++) {
			inputs[i] = new Matrix(inputSize, inputSize);
		}
	}
	/**
	 * Constructeur sans paramètres pour l'encodage XML
	 */
	public CNN() {
		layers = new ArrayList<>();
	}

	/**
	 * Methode qui permet d'ajouter une layer au reseau
	 * @param layer la layer a ajouter au reseau
	 */
	public void addLayer(CNNLayer layer) {
		layers.add(layer);
		if (inputs != null) activation();
	}


	/**
	 * Methode qui transforme les entrees en sortie
	 * @return tableau de reels
	 */
	public double[] feedForward() {

		activation();

		double[] out = new double[outputs.getROWS() * outputs.getCOLS()];

		for(int i = 0; i < outputs.getROWS(); i++) {
			for(int j = 0; j < outputs.getCOLS(); j++) {
				out[j * outputs.getCOLS() + i] = outputs.getElement(i, j);

			}
		}

		return out;
	}

	/**
	 * Activation du reseau
	 */
	public void activation() {

		for(int i = 0 ; i < layers.size(); i++) {

			layers.get(i).setLearningRate(learningRate);

			if (layers.get(i) instanceof ConvolutionLayer) {
				ConvolutionLayer l = (ConvolutionLayer) layers.get(i);
				l.setFunction(activationFunction);
			} else if (layers.get(i) instanceof FullyConnectedLayer) {
				FullyConnectedLayer l = (FullyConnectedLayer) layers.get(i);
				l.setFunction(activationFunction);
			}

			if (i == 0) {
				layers.get(i).setInputs(inputs);
				layers.get(i).setOutputs(layers.get(i).operation());
			} else {
				layers.get(i).setInputs(layers.get(i - 1).getOutputs());
				layers.get(i).setOutputs(layers.get(i).operation());

			}


		}

		outputs = layers.get(layers.size() - 1).getOutputs()[0];
	}

	/**
	 * Methode qui va me donner envie d'exploser mon ordi durant la semaine de relache...
	 * Sur une note plus serieuse : methode pour calibrer les poids du reseau
	 */
	public void backPropagation(double[] target) {

		Matrix[] layerError = new Matrix[1];
		Matrix[] finalLayerError = new Matrix[1];
		finalLayerError[0] = new Matrix(MathTools.getAsTwoDimensionalArray(target));

		for(int layer = layers.size() - 1; layer > 0; layer--) {

			if (layer == layers.size() - 1) {
				layerError = layers.get(layer).backpropagation(finalLayerError);
			} else {
				layerError = layers.get(layer).backpropagation(layerError);
			}
		}

	}

	/**
	 * Methode qui ajoute une vectorize layer et une fully connected pour pouvoir completer la structure du reseau de convolution
	 */
	public void endStructure() {

		addLayer(new VectorizationLayer(layers.get(layers.size() - 1).getOutputs(), layers.get(layers.size() - 1).getOutputs()[0].getROWS()));

		addLayer(new FullyConnectedLayer(activationFunction, layers.get(layers.size() - 1).getOutputs()[0].getROWS(), numberOfOutputs, learningRate));

		outputs = layers.get(layers.size() - 1).getOutputs()[0];

	}

	public String toString() {

		String s = "CNN \n\n";
		for(int i = 0; i < layers.size(); i++) {
			s += "\n" + layers.get(i).toString();
		}
		return s;
	}

	/**
	 * Retourne le nombre de couches du reseau
	 * @return le nombre de layers
	 */
	public int getNbLayers() {
		return nbLayers;
	}
	/**
	 * Modifie le nombre de layers
	 * @param nbLayers : le nouveau nombre de layers
	 */ 
	public void setNbLayers(int nbLayers) {
		this.nbLayers = nbLayers;
	}
	/**
	 * return le arraylisyt contenant toutes les layers
	 * @return le arraylisyt contenant toutes les layers
	 */
	public ArrayList<CNNLayer> getLayers() {
		return layers;
	}
	/**
	 * Permet de modifier le arraylisyt contenant toutes les layers
	 * @param layers : le nouveau arraylisyt contenant toutes les layers
	 */
	public void setLayers(ArrayList<CNNLayer> layers) {
		this.layers = layers;
	}
	/**
	 * Retourne les inputs du reseau
	 * @return le tableau de matrices des inputs
	 */
	public Matrix[] getInputs() {
		return inputs;
	}
	/**
	 * initialise les inputs du reseau
	 * @param inputs : le tableau de matrices a mettre en inputs du reseau
	 */
	public void setInputs(Matrix[] inputs) {
		this.inputs = inputs;
	}

	/**
	 * Methode qui retourne la fonction d'activation du reseau
	 * @return fonction d'activation
	 */
	public ActivationFunctions getActivationFunction() {
		return activationFunction;
	}

	/**
	 * Methode qui change la fonction d'activation du reseau
	 * @param activationFunction fonction d'activation reseau
	 */
	public void setActivationFunction(ActivationFunctions activationFunction) {
		this.activationFunction = activationFunction;
	}

	/**
	 * Methode qui retourne le taux d'apprentissage
	 * @return taux d'apprentissage
	 */
	public double getLearningRate() {
		return learningRate;
	}

	/**
	 * Methode qui change le taux d'apprentissage
	 * @param learningRate taux d'apprentissage
	 */
	public void setLearningRate(double learningRate) {
		this.learningRate = learningRate;
	}

	/**
	 * Methode qui retourne les sorties sur reseau
	 * @return sorties
	 */
	public Matrix getOutputs() {
		return outputs;
	}

	/**
	 * Methode qui change les sorties sur reseau
	 * @param outputs sorties
	 */
	public void setOutputs(Matrix outputs) {
		this.outputs = outputs;
	}

	/**
	 * test de l'activation : a permi de regler les differentes operations entre les matrices pour obtenir la dimension finale : on y voit que les layers
	 * se transmettent leurs inputs/outputs entre elles et que les differentes operations sont respectees 
	 * A chaque ajout de layer, les operations se repettent et elles contiennent le print : il ya donc plusieurs iterations de print
	 * @param args
	 */
	public static void main(String[] args) {
		CNN cnnTest = new CNN(ActivationFunctions.Sigmoid, 5, 3, 2, 0.03);

		Matrix[] input = new Matrix[3];
		Matrix[] input2 = new Matrix[3];
		double[][] a = {
				{1, 0, 0, 0, 1},
				{0, 1, 0, 1, 0},
				{0, 0, 1, 0, 0},
				{0, 1, 0, 1, 0},
				{1, 0, 0, 0, 1}
		};

		double[][] c = {
				{1, 1, 1, 1, 1},
				{1 ,0, 0, 0, 1},
				{1, 0, 0, 0, 1},
				{1, 0, 0, 0, 1},
				{1, 1, 1, 1, 1}
		};

		Matrix r = new Matrix(a);
		Matrix g = new Matrix(a);
		Matrix b = new Matrix(a);

		Matrix r2 = new Matrix(c);
		Matrix g2 = new Matrix(c);
		Matrix b2 = new Matrix(c);

		input[0] = r;
		input[1] = g;
		input[2] = b;

		input2[0] = r2;
		input2[1] = g2;
		input2[2] = b2;

		cnnTest.setInputs(input);

		double[] output = {1, 0};
		double[] output2 = {0, 1};

		cnnTest.addLayer(new ConvolutionLayer(3, 3, 5, 3));
		cnnTest.addLayer(new ConvolutionLayer(2, 2));
		cnnTest.endStructure();

		System.out.println(Arrays.toString(cnnTest.feedForward()));

		//System.out.println(cnnTest);


		for(int i = 0; i < 10000; i++) {
			if (i % 2 == 0) {
				cnnTest.setInputs(input);
				cnnTest.feedForward();
				cnnTest.backPropagation(output);
			} else {
				cnnTest.setInputs(input2);
				cnnTest.feedForward();
				cnnTest.backPropagation(output2);
			}
		}


		System.out.println("APRES : -----------------------------");

		cnnTest.setInputs(input);
		System.out.println(Arrays.toString(cnnTest.feedForward()));

		//System.out.println(cnnTest);

		cnnTest.setInputs(input2);
		System.out.println(Arrays.toString(cnnTest.feedForward()));

		//System.out.println(cnnTest.getInputs()[0]);
		//System.out.println(cnnTest);



		/*
		Matrix[] inputs = new Matrix[3];
		Matrix i0 = new Matrix(6,6);
		i0.initWithRandomValues();
		inputs[0] = i0;
		//System.out.println(i0);
		Matrix i1 = new Matrix(6,6);
		i1.initWithRandomValues();
		//System.out.println(i1);
		inputs[1] = i1;
		Matrix i2 = new Matrix(6,6);
		i2.initWithRandomValues();
		//System.out.println(i2);
		inputs[2] = i2;
		cnnTest.setInputs(inputs);
		
		cnnTest.addLayer(new ConvolutionLayer(3, 3));
		
		cnnTest.addLayer(new MaxPoolingLayer (2));
		
		cnnTest.addLayer(new ConvolutionLayer(2,2));
		
		cnnTest.addLayer(new MaxPoolingLayer(2));
		
		cnnTest.end();


		*/


	}
}
