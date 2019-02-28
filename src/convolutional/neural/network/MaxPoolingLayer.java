package convolutional.neural.network;

import math.Matrix;
/**
 *  Type de layer qui extrait les valeurs maximales de matrices
 * @author Simon Daze
 *
 */
public class MaxPoolingLayer extends CNNLayer {

	
	protected int poolingSize;
	int nbFilters = 1 ; 

	public MaxPoolingLayer( int poolSize) {
		
		filters = new Filter[nbFilters];

		poolingSize = poolSize;
		for(int i = 0; i < nbFilters; i++ ) {
			filters[i] = new Filter(poolSize,poolSize);
		}
	}

	/**
	 * L'operation de ce type de Layer : le max pool qui retourne la valeur maximale d'une sous matrice dans une matrice
	 * @param inputs : le tableau des matrices sur lesquels on va appliquer le maxPooling
	 * @return out : le tableau de matrices contenant les valeurs maximales de chaque matrices
	 */
	
	public Matrix[] operation(Matrix[] inputs){
		Matrix[] out = new Matrix[filters.length];
		for(int i = 0; i < inputs.length; i++) {
			out[i] = filters[i].maxPool(inputs[i]);
		}
		return out;
	}
	/**
	 * Methode qui va me donner envie d'exploser mon ordi durant la semaine de relache...
	 * Sur une note plus serieuse : methode pour calibrer les weights du reseau
	 */
	public void backPropagation() {
		
	}

	@Override
	public Matrix[] operation() {
		Matrix[] out = new Matrix[inputs.length];
		for(int i = 0; i < inputs.length; i++) {
			out[i] = filters[0].maxPool(inputs[i]);
			System.out.println(out[i]);
		}
		return out;
	}

}
