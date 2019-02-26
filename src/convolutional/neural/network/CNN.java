package convolutional.neural.network;

import math.Matrix;

public class CNN {
	int nbLayers;
	CNNLayer[] layers;
	int inputLayerSize;
	int filterSize;
	Matrix[] inputs;

	/**
	 * constructeur du reseau
	 * @param nbLayers le nombre de layers du reseau
	 * @param filterSize la taille du premier filter
	 * @param nbFilters le nombre de filters de la premiere couche
	 */
	public CNN(int nbLayers, int filterSize, int nbFilters, Matrix[] input ) {
		layers =  new CNNLayer[nbLayers];
		layers[0] = new ConvolutionLayer(filterSize, nbFilters);
		activation(input);
		
	}

	/**
	 * activation du reseau 
	 * @param inputs la matrice contenant les valeurs de RGB de l'image a evaluee
	 */
	public void activation(Matrix[] inputs) {
		Matrix[] in;
		Matrix[]out;
		for(int i = 0 ; i < layers.length ; i++) {
			if (i == 0) {
				layers[i].setInputs(inputs);
			}
			if( i == layers.length-1) {
				layers[i].setOutputs(layers[i].getInputs());
			}else {
				in = layers[i].getInputs();
				out = layers[i].operation(in);
				layers[i+1].setInputs(out);
			
			}
		}
	}
}
