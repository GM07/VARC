package neural.network;

import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;

import functions.ActivationFunctions;
import math.MathTools;
import math.Matrix;

public class NeuralNetwork implements Serializable {

	private static final long serialVersionUID = 643116515073485682L;

	private Layer[] layers;

	private int NUMBER_OF_LAYERS, INPUT_LAYER_SIZE, OUTPUT_LAYER_SIZE;

	private ActivationFunctions activationFunction;

	/**
	 * Constructeur nul pour permettre la sauvegarde et le chargement en XML
	 */
	public NeuralNetwork() {}

	/**
	 * Constructeur
	 * @param function fonction d'activation du reseau
	 * @param configuration configuration du reseau (nombre de neurones par couche)
	 */
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
	 * Calcul le resultat que le reseau genere pour des donnees en entree
	 * @param inputs donnees en entree
	 */
	public void feedForward(double[] inputs) {

		if (inputs.length == INPUT_LAYER_SIZE) {

			Matrix matrixInputs = new Matrix(MathTools.getAsTwoDimensionalArray(inputs));

			layers[0].setInputs(matrixInputs);

			for(int layer = 0; layer < layers.length; layer++) {

			    //System.out.println(layer + " - " + layers[layer]);
				if (layers[layer].getType() != LayerType.InputLayer) {
					layers[layer].setInputs(layers[layer - 1].getOutputs());
				}
				layers[layer].feedForward();
			}

		} else {

			System.out.println("ERROR WHILE TRYING TO FEED FORWARD : INPUT SIZE (" + inputs.length + ") IS NOT THE SAME AS THE NETWORK'S (" + INPUT_LAYER_SIZE + ")");

		}
	}

	/**
	 * Applique l'algorithme de backpropagation base sur la descente du gradient au reseau de neurone
	 * @param expected resultat voulu
	 */
	public void backpropagation(double[] expected) {


		for(int neuron = 0; neuron < OUTPUT_LAYER_SIZE; neuron++) {
			layers[NUMBER_OF_LAYERS - 1].getErrors().getMat()[neuron][0] = (layers[NUMBER_OF_LAYERS - 1].getOutputs().getMat()[neuron][0] - expected[neuron]) * layers[NUMBER_OF_LAYERS - 1].getFunction().getDerivative().getValue(layers[NUMBER_OF_LAYERS - 1].getOutputs().getMat()[neuron][0]);
		}

		for(int layer = NUMBER_OF_LAYERS - 2; layer > 0; layer--) {

			Layer currentLayer = layers[layer];

			for(int neuron = 0; neuron < currentLayer.getNB_NEURONS(); neuron++) {

				double sum = 0;

				for(int nextNeuron = 0; nextNeuron < layers[layer + 1].getNB_NEURONS(); nextNeuron++) {

					sum += layers[layer + 1].getErrors().getMat()[nextNeuron][0] * layers[layer + 1].getWeights().getMat()[nextNeuron][neuron];
				}

				currentLayer.getErrors().getMat()[neuron][0] = sum * currentLayer.getFunction().getDerivative().getValue(currentLayer.getOutputs().getMat()[neuron][0]);
			}
		}

	}
	/**
	 * Met a jour la valeur de chaque poids et chaque biais en fonction de l'erreur de la neurone
	 * @param learningRate constante a laquelle le reseau apprend
	 */
	public void updateWeightsAndBiases(double learningRate) {

		for(int layer = NUMBER_OF_LAYERS - 1; layer > 0; layer--) {

			for(int neuron = 0; neuron < layers[layer].getNB_NEURONS(); neuron++) {

					double dB = -learningRate * layers[layer].getErrors().getMat()[neuron][0];
					layers[layer].getBiases().getMat()[neuron][0] += dB;

					for(int prevNeuron = 0; prevNeuron < layers[layer - 1].getNB_NEURONS(); prevNeuron++) {
						double dW = dB * layers[layer - 1].getOutputs().getMat()[prevNeuron][0];
						layers[layer].getWeights().getMat()[neuron][prevNeuron] += dW;
					}
			}

        }
	}

	/**
	 * Entraine le reseau en appliquant l'algorithme de feed forward et de backpropagation
	 * @param inputs l'entree du reseau de neurone
	 * @param target le resultat qui est attendu
	 */
	public void train(double[] inputs, double[] target) {

		feedForward(inputs);
		backpropagation(target);
	}

	/**
	 * Change l'entree du reseau
	 * @param inputs entree du reseau
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
	 * Retourne la matrice de sortie du reseau
	 * @return matrice de sortie
	 */
	public Matrix getResults() {
		return layers[NUMBER_OF_LAYERS - 1].getOutputs();
	}

	/**
	 * Retourne une certaine couche du reseau
	 * @param index numero de la couche (la premiere couche est de numero 0)
	 * @return couche qui correspond au numero
	 */
	public Layer getLayer(int index) {

		if (index > 0 && index < NUMBER_OF_LAYERS ) return layers[index];
		else {
			System.out.println("Index out of bounds");
			return null;
		}
	}

	/**
	 * Methode qui retourne les donnees du reseau de neurone
	 * @return les donnees du reseau sous forme de chaine de caracteres
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


	/**
	 * Methode pour sauvegarder un reseau sour format XML
	 * @param nn le reseau de neurones a sauvegarder
	 * @param path le chemin d'acces ou sauvegarder le reseau
	 */
	//Auteurs : source web : https://www.edureka.co/blog/serialization-of-java-objects-to-xml-using-xmlencoder-decoder/

	public static void saveNetworkToXML (NeuralNetwork nn, String path) throws IOException
	{
		XMLEncoder encoder = null;
		try{
			System.out.println(path);
			encoder = new XMLEncoder(new BufferedOutputStream(new FileOutputStream(path + ".xml")));
			encoder.writeObject(nn);
			encoder.close();
			System.out.println("Le reseau a bel et bien ete sauvegarde : " + path);
		}catch(FileNotFoundException fileNotFound){
			fileNotFound.printStackTrace();
			System.out.println("Erreur en creant le fichier : " + path);
		}

	}
	/**
	 * Permet de lire un reseau sauvergarde sous format XML
	 * @param path le path du reseau a charger
	 * @return le reseau charge a partir du fichier
	 * @throws IOException
	 */
	//auteurs Source web : https://www.edureka.co/blog/serialization-of-java-objects-to-xml-using-xmlencoder-decoder/

	public static NeuralNetwork loadNetworkFromXML(String path) throws IOException {
		XMLDecoder decoder = null;
		try {
			decoder = new XMLDecoder(new BufferedInputStream(new FileInputStream(path)));
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
	 * Retourne les couches du reseau de neurone
	 * @return les couches du reseau
	 */
	public Layer[] getLayers() {
		return layers;
	}

	/**
	 * Change les couches du reseau de neurone selon une nouvelle configuration de couches
	 * @param layers nouvelle configuration de couches
	 */
	public void setLayers(Layer[] layers) {
		this.layers = layers;
	}

	/**
	 * Retourne la fonction d'activation utilisee par le reseau de neurone
	 * @return la fonction d'activation
	 */
	public ActivationFunctions getActivationFunction() {
		return activationFunction;
	}

	/**
	 * Change la fonction d'activation utilisee par le reseau de neurone
	 * @param activationFunction fonction d'activation
	 */
	public void setActivationFunction(ActivationFunctions activationFunction) {
		this.activationFunction = activationFunction;
	}

	/*
	Tous les getter et setter suivant n'ont pour simple but que de permettre la sauvegarde du reseau dans un fichier XML
	Les valeurs vont toujours etre constantes pour une instance de la classe
	 */

	/**
	 * Retourne le nombre de couches du reseau
	 * @return nombre de couches
	 */
    public int getNUMBER_OF_LAYERS() {
		return NUMBER_OF_LAYERS;
	}

	/**
	 * Change le nombre de couches du reseau
	 * @param nUMBER_OF_LAYERS nombre de couches
	 */
	public void setNUMBER_OF_LAYERS(int nUMBER_OF_LAYERS) {
		NUMBER_OF_LAYERS = nUMBER_OF_LAYERS;
	}

	/**
	 * Change le nombre de neurones dans la couche d'entree du reseau
	 * @param iNPUT_LAYER_SIZE nombre de neurones en entree
	 */
	public void setINPUT_LAYER_SIZE(int iNPUT_LAYER_SIZE) {
		INPUT_LAYER_SIZE = iNPUT_LAYER_SIZE;
	}

	/**
	 * Retourne le nombre de neurone dans la premiere couche du reseau
	 * @return nombre de neurones dans la couche d'entree
	 */
	public int getINPUT_LAYER_SIZE() {
		return INPUT_LAYER_SIZE;
	}

	/**
	 * Change le nombre de neurones dans la derniere couche du reseau
	 * @param OUTPUT_LAYER_SIZE nombre de neurones en sortie
	 */
	public void setOUTPUT_LAYER_SIZE(int OUTPUT_LAYER_SIZE) {
		OUTPUT_LAYER_SIZE = OUTPUT_LAYER_SIZE;
	}

	/**
	 * Retourne le nombre de neurones dans la derniere couche du reseau
	 * @return nombre de neurones en sortie
	 */
	public int getOUTPUT_LAYER_SIZE() {
		return OUTPUT_LAYER_SIZE;
	}

	/**
	 * Test du chargement de la sauvegarde du reseau
	 * @param args
	 * @throws IOException
	 */
	//auteur : Simon Daze
	public static void main(String[] args) throws IOException {

		System.out.println(NeuralNetwork.class.getClassLoader().getResource(""));

		NeuralNetwork nn = new NeuralNetwork(ActivationFunctions.Sigmoid, 3,2,1,2);
		double[] inputs = new double[]{1,2,3};
		double[] outputs = new double[] {2,1};
		nn.train(inputs, outputs);
		nn.updateWeightsAndBiases(0.1);
		System.out.println(nn);
		String path = "C:\\Users\\mehga\\Documents\\Autres\\Network Savee\\neural3";
		/*NeuralNetwork nn3 = new NeuralNetwork();
		nn3.setActivationFunction(ActivationFunctions.Sigmoid);
		NeuralNetwork.saveNetworkToXML(nn3, "res/network_saves/neural");*/
		NeuralNetwork.saveNetworkToXML(nn, path);
		NeuralNetwork nn2 = NeuralNetwork.loadNetworkFromXML(path);
		//System.out.println(NeuralNetwork.loadNetwork(path));

		System.out.println(nn2);

	}






}
