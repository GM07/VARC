package convolutional.neural.network;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.rmi.activation.ActivationID;
import java.util.ArrayList;

import functions.ActivationFunctions;
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
	private int nbLayers;
	private ArrayList<CNNLayer> layers;
	private int filterSize;
	private Matrix[] inputs;
	private ActivationFunctions activationFunction;


	/**
	 * constructeur du resea
	 * @param filterSize la taille du premier filter
	 * @param nbFilters le nombre de filters de la premiere couche
	 */
	public CNN(int filterSize, int nbFilters, ActivationFunctions activationFunction) {
		layers = new ArrayList<CNNLayer>();
		layers.set(0, new ConvolutionLayer(filterSize, nbFilters));
		this.activationFunction = activationFunction;
		activation();

	}
	/**
	 * Constructeur sans paramètres pour l'encodage XML
	 */
	public CNN() {
		layers = new ArrayList<CNNLayer>();
	}

	/**
	 * Methode qui permet d'ajouter une layer au reseau
	 * @param layer la layer a ajouter au reseau
	 */
	public void addLayer( CNNLayer layer) {
		layers.add(layer);
		activation();
	}

	/**
	 * Activation du reseau
	 */
	public void activation() {
		Matrix[] in;
		for(int i = 0 ; i < layers.size() ; i++) {
			if (i == 0) {
				layers.get(i).setInputs(inputs);
				layers.get(i).setOutputs(layers.get(i).operation());
			} else {
				in = layers.get(i-1).getOutputs();
				layers.get(i).setInputs(in);
				layers.get(i).setOutputs(layers.get(i).operation());
			}
		}
	}

	/**
	 * Methode qui va me donner envie d'exploser mon ordi durant la semaine de relache...
	 * Sur une note plus serieuse : methode pour calibrer les poids du reseau
	 */
	public void backPropagation() {


	}
	/**
	 * Methode qui sauvegarde un reseau de convolution
	 * @param path
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
	 * Methode qui charge un reseau de convolution
	 * @param path chemin d'acces du reseau
	 * @return reseau de convolution
	 */
	public static CNN loadNetwork(String path){
		try {
			System.out.println(path);
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
		}
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
	 * Retourne la dimension des matrices des filtres 
	 * @return la dimension des matrices des filtres
	 */
	public int getFilterSize() {
		return filterSize;
	}
	/**
	 * modifie la dimension des filtres
	 * @param filterSize : la nouvelle dimension des filtres
	 */
	public void setFilterSize(int filterSize) {
		this.filterSize = filterSize;
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
	 * test de l'activation : a permi de regler les differentes operations entre les matrices pour obtenir la dimension finale : on y voit que les layers
	 * se transmettent leurs inputs/outputs entre elles et que les differentes operations sont respectees 
	 * A chaque ajout de layer, les operations se repettent et elles contiennent le print : il ya donc plusieurs iterations de print
	 * @param args
	 */
	public static void main(String[] args) {
		CNN cnnTest = new CNN();
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
		
		





	}
}
