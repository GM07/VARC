package neural.network;

import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InvalidClassException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import javax.swing.JOptionPane;

import functions.ActivationFunctions;
import functions.SoftmaxFunction;
import math.MathTools;
import math.Matrix;

/**
 * Classe qui represente la structure d'un reseau de neurones
 * @author Gaya Mehenni
 */
public class NeuralNetwork implements Serializable {

	private static final long serialVersionUID = 643116515073485682L;

	private Layer[] layers;

	private int NUMBER_OF_LAYERS, INPUT_LAYER_SIZE, OUTPUT_LAYER_SIZE;

	private ActivationFunctions activationFunction;

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
				if (activationFunction == ActivationFunctions.ReLU) {
					layers[layer] = new Layer(OUTPUT_LAYER_SIZE, configuration[NUMBER_OF_LAYERS - 2], LayerType.OutputLayer, ActivationFunctions.Softmax);
				} else {
					layers[layer] = new Layer(OUTPUT_LAYER_SIZE, configuration[NUMBER_OF_LAYERS - 2], LayerType.OutputLayer, activationFunction);
				}

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
			if(layers[NUMBER_OF_LAYERS - 1].getFunction() == ActivationFunctions.Softmax) {
				SoftmaxFunction sf = new SoftmaxFunction();

				layers[NUMBER_OF_LAYERS - 1].getErrors().getMat()[neuron][0] = (layers[NUMBER_OF_LAYERS - 1].getOutputs().getMat()[neuron][0] - expected[neuron]) * sf.getDerivative(neuron, layers[NUMBER_OF_LAYERS - 1].getOutputs(),new Matrix (MathTools.getAsTwoDimensionalArray(expected))) ;

				//layers[NUMBER_OF_LAYERS - 1].getErrors().getMat()[neuron][0] = sf.getValue(layers[NUMBER_OF_LAYERS - 1].getOutputs(), neuron) - expected[neuron];
				//layers[NUMBER_OF_LAYERS - 1].getErrors().getMat()[neuron][0] = sf.getValue(logits, index) - sf.getValue(layers[NUMBER_OF_LAYERS-1].getOutputs(), neuron);

				//layers[NUMBER_OF_LAYERS - 1].getErrors().getMat()[neuron][0] = (layers[NUMBER_OF_LAYERS - 1].getOutputs().getMat()[neuron][0] - expected[neuron]) * sf.getValue(layers[NUMBER_OF_LAYERS-1].getOutputs(), neuron) * (expected[neuron] -  sf.getValue(layers[NUMBER_OF_LAYERS-1].getOutputs(), neuron));

				//layers[NUMBER_OF_LAYERS - 1].getErrors().getMat()[neuron][0] = sf.crossEntropyLoss(layers[NUMBER_OF_LAYERS-1].getOutputs(), expected) * sf.getDerivative(neuron, layers[NUMBER_OF_LAYERS-1].getOutputs(), layers[NUMBER_OF_LAYERS -1].getOutputs());
				//layers[NUMBER_OF_LAYERS - 1].getErrors().getMat()[neuron][0] = (layers[NUMBER_OF_LAYERS - 1].getOutputs().getMat()[neuron][0] - expected[neuron]) * sf.getDerivative(neuron, layers[NUMBER_OF_LAYERS-1].getOutputsZ(), layers[NUMBER_OF_LAYERS -1].getOutputs());
			}else {


				layers[NUMBER_OF_LAYERS - 1].getErrors().getMat()[neuron][0] = (layers[NUMBER_OF_LAYERS - 1].getOutputs().getMat()[neuron][0] - expected[neuron]) * layers[NUMBER_OF_LAYERS - 1].getFunction().getDerivative().getValue(layers[NUMBER_OF_LAYERS - 1].getOutputs().getMat()[neuron][0]);

			}
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

	public static void saveNetworkToXML (NeuralNetwork nn, String path) throws IOException {
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
	 * Methode qui sauvegarde un reseau de neurone
	 * @param path chemin d'acces
	 */
	public void saveNetwork(String path) {
		try {
			FileOutputStream fos = new FileOutputStream(path + ".dat");
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			oos.writeObject(this);
			oos.close();
			fos.close();
		} catch(IOException e) {
			e.printStackTrace();
			System.out.println(path);
			System.out.println("Le fichier n'a pas ete trouve");
		}
	}

	/**
	 * Methode qui charge un reseau de neurone
	 * @param path chemin d'acces du reseau
	 * @return reseau de neurone
	 */
	//Caroline Houle
	public static NeuralNetwork loadNetwork(String path){
		/*
		 * Cette approche permet de lire d�s le d�marrage un fichier existant, s'il est
		 * dans le BuildPath. Approche sugg�r�e: dans Eclipse mettre le fichier dans un dossier 
		 * projet, ajouter ce dossier au BuildPath. Quand le .jar ex�cutable sera
		 * g�n�r�, ce fichier sera "dans" le .jar, et l'application peut le trouver en utilisant le code ci-dessous.
		 * Alternative : placer le fichier dans le m�me dossier que le .jar (car ce
		 * dossier fera aussi partie du BuildPath).
		 */
		ObjectInputStream ois=null;
		InputStream is = ClassLoader.getSystemClassLoader().getResourceAsStream(path);

		if (is == null) {
			JOptionPane.showMessageDialog(null, "Incapable de trouver ce fichier dans le BuildPath (ou dans le jar ex�cutable) " + path );
			return null;
		}


		//ce fichier a �t� con�u d'avance et plac� dans un dossier qui fait partie du Build Path
		try {
			ois = new ObjectInputStream(is);
			//on lit d'un coup un objet stock� dans le fichier
			NeuralNetwork nn = (NeuralNetwork) ois.readObject(); 
			JOptionPane.showMessageDialog(null, "Lecture du fichier " + path + " avec succ�s. ");

			return nn;

		}catch (ClassNotFoundException e) {
			JOptionPane.showMessageDialog(null,"L'objet lu est d'une classe inconnue");
			e.printStackTrace();
			return null;
		}
		catch (InvalidClassException e) {
			JOptionPane.showMessageDialog(null,"Les classes utilis�es pour l'�criture et la lecture diff�rent!");
			e.printStackTrace();
			return null;
		}
		catch (FileNotFoundException e) {
			JOptionPane.showMessageDialog(null, "Fichier " + path + "  introuvable!");
			return null;
		}

		catch (IOException e) {
			JOptionPane.showMessageDialog(null, "Erreur rencontree lors de la lecture " + path);
			e.printStackTrace();
			return null;
		}

		finally {
			//on ex�cutera toujours ceci, erreur ou pas
			try { 
				ois.close();
			}
			catch (IOException e) { 
				System.out.println("Erreur rencontr�e lors de la fermeture!"); 
			}
		}//fin finally


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
	 * @param NUMBER_OF_LAYERS nombre de couches
	 */
	public void setNUMBER_OF_LAYERS(int NUMBER_OF_LAYERS) {
		NUMBER_OF_LAYERS = NUMBER_OF_LAYERS;
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

		NeuralNetwork nn = new NeuralNetwork(ActivationFunctions.Sigmoid, 64 * 64 * 3, 28, 16, 3);

		String path = "D:\\Cegep\\Session_4\\IA Data\\Network Saves\\test3";

		long t1 = System.currentTimeMillis();
		long t2;
		nn.saveNetwork(path);

		t2 = System.currentTimeMillis();
		System.out.println(t2 - t1);

		System.out.println("Loading");



		System.out.println("Fin");

	}






}
