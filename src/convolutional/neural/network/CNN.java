package convolutional.neural.network;

import java.io.Serializable;
import java.util.ArrayList;

import math.Matrix;
/**
 * Le reseau de convolution contenant differents types de layers
 * @author Simon Daze
 *
 */
public class CNN implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -3966726810488689816L;
	private int nbLayers;
	private ArrayList<CNNLayer> layers;
	private int filterSize;
	private Matrix[] inputs;


	/**
	 * constructeur du reseau
	 * @param nbLayers le nombre de layers du reseau
	 * @param filterSize la taille du premier filter
	 * @param nbFilters le nombre de filters de la premiere couche
	 */
	public CNN(int filterSize, int nbFilters, Matrix[] input ) {
		layers = new ArrayList<CNNLayer>();
		layers.set(0, new ConvolutionLayer(filterSize, nbFilters));
		activation();

	}
	/**
	 * constructeur sans paramètres
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
	 * activation du reseau 
	 * @param inputs la matrice contenant les valeurs de RGB de l'image a evaluee
	 */
	public void activation() {
		Matrix[] in;
		Matrix[]out;
		for(int i = 0 ; i < layers.size() ; i++) {
			if (i == 0) {
				layers.get(i).setInputs(inputs);
				layers.get(i).setOutputs(layers.get(i).operation());
			} else {
				in = layers.get(i-1).getOutputs();
				layers.get(i).setInputs(in);
				out = layers.get(i).operation();
				layers.get(i).setOutputs(out);

			}
		}
	}


	/**
	 * Methode qui va me donner envie d'exploser mon ordi durant la semaine de relache...
	 * Sur une note plus serieuse : methode pour calibrer les weights du reseau
	 */
	public void backPropagation() {

	}



	public int getNbLayers() {
		return nbLayers;
	}
	public void setNbLayers(int nbLayers) {
		this.nbLayers = nbLayers;
	}
	public ArrayList<CNNLayer> getLayers() {
		return layers;
	}
	public void setLayers(ArrayList<CNNLayer> layers) {
		this.layers = layers;
	}
	public int getFilterSize() {
		return filterSize;
	}
	public void setFilterSize(int filterSize) {
		this.filterSize = filterSize;
	}
	public Matrix[] getInputs() {
		return inputs;
	}
	public void setInputs(Matrix[] inputs) {
		this.inputs = inputs;
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
		
		cnnTest.addLayer( new MaxPoolingLayer (2));
		
		cnnTest.addLayer(new ConvolutionLayer(2,2));
		
		cnnTest.addLayer(new MaxPoolingLayer(2));





	}
}
