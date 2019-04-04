package convolutional.neural.network;

import math.Matrix;

/**
 * Classe de la layer qui calcul les valeurs moyennes de sous matrices
 * @author Simon Daze
 *
 */
public class AveragePoolingLayer extends CNNLayer {
	protected int poolingSize;
	int nbFilters = inputs.length ; 
	/**
	 * Constructeur de la layer
	 * @param poolSize la taille des filtres de pooling ( taille des sous-matrices extraites)
	 */
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
	 * propagation de l'erreur de la couche
	 * @param expected l'erreur de la layer precedente
	 * @return l'erreur de cette couche en fonction de la precedente
	 */
	public Matrix[] backpropagation(Matrix[] expected) {
		Matrix[] error = new Matrix[expected.length];

		
		for (int q = 0 ; q < error.length ; q++) {
			//initialise la dimension de l'erreur
			error[q] = new Matrix(expected[0].getROWS(),expected[0].getCOLS());
			for(int i = 0; i < inputs[i].getROWS(); i++) {
				for (int j = 0; j < inputs[i].getCOLS(); j++) {
					//calcul de l'erreur
					error[q].setElement(i, j, (1.0/(double)poolingSize *expected[q].getElement((int)Math.round(i/2.0), (int)Math.round(j/2.0))));
				}
			}
		}
		
		return error;
	}

}
