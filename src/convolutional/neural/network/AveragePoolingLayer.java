package convolutional.neural.network;

import math.Matrix;

/**
 * Classe qui represente la structure d'une couche de average pooling dans les reseaux de convolution
 * @author Simon Daze
 */
public class AveragePoolingLayer extends CNNLayer {
	protected int poolingSize;
	int nbFilters = inputs.length ; 
	
	public AveragePoolingLayer(int poolSize) {
		poolingSize = poolSize;
		
		for(int i = 0; i < filters.length; i++) {
			filters[i] = new Filter(poolingSize,poolingSize);
		}
		
		}
	
	
	/**
	 * Operation de cete couche : le average pooling 
	 */
	@Override
	public Matrix[] operation() {
		Matrix[] out = new Matrix[nbFilters];
		for(int i = 0; i < inputs.length; i++) {
			out[i] = filters[i].averagePool(inputs[i]);
		}
		return out;

	}

	/**
	 * Methode qui permet de retourner l'erreur de la couche en fonction de l'erreur de la couche d'apres
	 * @param target erreur de la couche d'apres
	 * @return erreur de la couche actuelle
	 */
	public Matrix[] backpropagation(Matrix[] target) {
		return null;
	}


}
