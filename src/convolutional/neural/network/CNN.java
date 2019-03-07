package convolutional.neural.network;

import java.util.ArrayList;

import math.Matrix;
/**
 * Le reseau de convolution contenant differents types de layers
 * @author Simon Daze
 *
 */
public class CNN {
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
		activation(input);

	}

	/**
	 * Methode qui permet d'ajouter une layer au reseau
	 * @param layer la layer a ajouter au reseau
	 */
	public void addLayer( CNNLayer layer) {
		layers.add(layer);
		activation(layers.get(layers.size()-2).getOutputs());
	}

	/**
	 * Activation du reseau
	 * @param inputs la matrice contenant les valeurs de RGB de l'image a evaluee
	 */
	public void activation(Matrix[] inputs) {
		Matrix[] in;
		Matrix[]out;
		for(int i = 0 ; i < layers.size() ; i++) {
			if (i == 0) {
				layers.get(i).setInputs(inputs);
				layers.get(i).setOutputs(layers.get(i).operation(layers.get(i).getInputs()));
			}
			if( i == layers.size()-1) {
				layers.get(i).setOutputs(layers.get(i).operation(layers.get(i).getInputs()));
			}else {
				in = layers.get(i-1).getOutputs();
				layers.get(i).setInputs(in);
				out = layers.get(i).operation(in);
				layers.get(i).setOutputs(out);
			}
		}
	}
}
