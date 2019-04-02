
package convolutional.neural.network;

import java.util.Arrays;

import math.Matrix;
/**
 *  Type de layer qui extrait les valeurs maximales de matrices
 * @author Simon Daze
 *
 */
public class MaxPoolingLayer extends CNNLayer {

	private static final long serialVersionUID = -3259937901909895649L;
	protected int poolingSize;
	int nbFilters = inputs.length ; 

	/**
	 * COnstruteur d'un layer de MaxPooling 
	 * @param poolSize : la taille du filtre de pooling
	 */
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
	 * Methode qui calcul l'erreur de la couche
	 * @param target erreur de la couche d'apres
	 * @return
	 */
	public Matrix[] backpropagation(Matrix[] target) {
		return null;
	}

	/**
	 * operation de cette layer : le max pooling 
	 * La fonction d'activation permet de limiter les ecarts entre les valeurs
	 */
	@Override
	public Matrix[] operation() {
		//System.out.println(Arrays.toString(inputs));
		Matrix[] out = new Matrix[inputs.length];
		for(int i = 0; i < inputs.length; i++) {
			out[i] = filters[0].maxPool(inputs[i]);
			//out[i].applyFunction(function.Sigmoid);
			System.out.println(out[i]);
		}
		return out;
	}

}

