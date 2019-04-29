package convolutional.neural.network;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InvalidClassException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.JOptionPane;

import functions.ActivationFunctions;
import math.MathTools;
import math.Matrix;
import neural.network.NeuralNetwork;
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
	 * Constructeur du reseau
	 */
	// Gaya Mehenni
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
	 * Methode qui permet d'ajouter une layer au reseau
	 * @param layer la layer a ajouter au reseau
	 */
	// Simon Daze
	public void addLayer(CNNLayer layer) {
		layers.add(layer);
		if (inputs != null) activation();
	}

	/**
	 * Methode qui transforme les entrees en sortie
	 * @return tableau des sorties sur reseau
	 */
	// Gaya Mehenni
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
	 * Activation du reseau qui relie les entrees et les sorites de chaque couche
	 */
	// Simon Daze
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
	// Gaya Mehenni
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
	 * Methode qui sauvegarde un reseau de convolution
	 * @param path le nom du fichier de sauvegarde du reseau
	 */
	// Simon Daze
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
	 * Methode qui charge un reseau de convolution
	 * @param path chemin d'acces du reseau
	 * @return reseau de convolution
	 */
	// Simon Daze
	public static CNN loadNetwork(String path){
		/*try {
			File f = new File(path);
			FileInputStream fis = new FileInputStream(f);
			ObjectInputStream ois = new ObjectInputStream(fis);
			CNN cnn = (CNN) ois.readObject();
			ois.close();
			fis.close();
			return cnn;
		} catch(IOException e) {
			System.out.println("Le fichier n'existe pas");
			return null;
		} catch (ClassNotFoundException e) {
			System.out.println("La classe n'a pas ete trouvee");
			return null;
		}*/
		ObjectInputStream ois=null;
		InputStream is = ClassLoader.getSystemClassLoader().getResourceAsStream(path);
		
		if (is == null) {
			JOptionPane.showMessageDialog(null, "Incapable de trouver ce fichier dans le BuildPath (ou dans le jar exécutable) " + path );
			return null;
		}


		 //ce fichier a été conçu d'avance et placé dans un dossier qui fait partie du Build Path
		try {
			 ois = new ObjectInputStream(is);
			 //on lit d'un coup un objet stocké dans le fichier
			CNN cnn = (CNN) ois.readObject(); 
			 //JOptionPane.showMessageDialog(null, "Lecture du fichier " + path + " avec succès. ");
	
			 return cnn;
			
		}catch (ClassNotFoundException e) {
			JOptionPane.showMessageDialog(null,"L'objet lu est d'une classe inconnue");
			e.printStackTrace();
			return null;
		}
		catch (InvalidClassException e) {
			JOptionPane.showMessageDialog(null,"Les classes utilisées pour l'écriture et la lecture diffèrent!");
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
			//on exécutera toujours ceci, erreur ou pas
		  	try { 
		  		ois.close();
		  	}
		    catch (IOException e) { 
		    	System.out.println("Erreur rencontrée lors de la fermeture!"); 
		    }
		}//fin finally
	}

	/**
	 * Methode qui ajoute une vectorize layer et une fully connected pour pouvoir completer la structure du reseau de convolution
	 */
	// Gaya Mehenni
	public void endStructure() {

		addLayer(new VectorizationLayer(layers.get(layers.size() - 1).getOutputs(), layers.get(layers.size() - 1).getOutputs()[0].getROWS()));

		addLayer(new FullyConnectedLayer(activationFunction, layers.get(layers.size() - 1).getOutputs()[0].getROWS(), numberOfOutputs, learningRate));

		outputs = layers.get(layers.size() - 1).getOutputs()[0];

	}

	/**
	 * Methode qui retourne une chaine affichant les details du CNN
	 * @return
	 */
	// Gaya Mehenni
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
	//Simon Daze
	public int getNbLayers() {
		return nbLayers;
	}
	/**
	 * Modifie le nombre de layers
	 * @param nbLayers : le nouveau nombre de layers
	 */ 
	//Simon Daze
	public void setNbLayers(int nbLayers) {
		this.nbLayers = nbLayers;
	}
	/**
	 * return le arraylisyt contenant toutes les layers
	 * @return le arraylisyt contenant toutes les layers
	 */
	//Simon Daze
	public ArrayList<CNNLayer> getLayers() {
		return layers;
	}
	/**
	 * Permet de modifier le arraylisyt contenant toutes les layers
	 * @param layers : le nouveau arraylisyt contenant toutes les layers
	 */
	//Simon Daze
	public void setLayers(ArrayList<CNNLayer> layers) {
		this.layers = layers;
	}
	/**
	 * Retourne les inputs du reseau
	 * @return le tableau de matrices des inputs
	 */
	//Simon Daze
	public Matrix[] getInputs() {
		return inputs;
	}
	/**
	 * initialise les inputs du reseau
	 * @param inputs : le tableau de matrices a mettre en inputs du reseau
	 */
	//Simon Daze
	public void setInputs(Matrix[] inputs) {
		this.inputs = inputs;
	}

	/**
	 * Methode qui retourne la fonction d'activation du reseau
	 * @return fonction d'activation
	 */
	//Simon Daze
	public ActivationFunctions getActivationFunction() {
		return activationFunction;
	}

	/**
	 * Methode qui change la fonction d'activation du reseau
	 * @param activationFunction fonction d'activation reseau
	 */
	//Simon Daze
	public void setActivationFunction(ActivationFunctions activationFunction) {
		this.activationFunction = activationFunction;
	}

	/**
	 * Methode qui retourne le taux d'apprentissage
	 * @return taux d'apprentissage
	 */
	//Simon Daze
	public double getLearningRate() {
		return learningRate;
	}

	/**
	 * Methode qui change le taux d'apprentissage
	 * @param learningRate taux d'apprentissage
	 */
	//Simon Daze
	public void setLearningRate(double learningRate) {
		this.learningRate = learningRate;
	}

	/**
	 * Methode qui retourne les sorties sur reseau
	 * @return sorties
	 */
	//Simon Daze
	public Matrix getOutputs() {
		return outputs;
	}

	/**
	 * Methode qui change les sorties sur reseau
	 * @param outputs sorties
	 */
	//Simon Daze
	public void setOutputs(Matrix outputs) {
		this.outputs = outputs;
	}

	/**
	 * test de l'activation : a permi de regler les differentes operations entre les matrices pour obtenir la dimension finale : on y voit que les layers
	 * se transmettent leurs inputs/outputs entre elles et que les differentes operations sont respectees 
	 * A chaque ajout de layer, les operations se repettent et elles contiennent le print : il ya donc plusieurs iterations de print
	 * @param args
	 */
	// Gaya Mehenni
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
