package convolutional.neural.network;

import math.Matrix;

public class MaxPoolingLayer extends CNNLayer {

	protected int filterSize;
	protected int poolingSize;

	public void MaxPoolingLayer(int size, int nbFilters, int poolSize) {
		filterSize = size;
		filters = new Filter[nbFilters];

		poolingSize = poolSize;
		for(int i = 0; i < nbFilters; i++ ) {
			filters[i] = new Filter(filterSize,filterSize);
		}
	}

	public Matrix[] operation(Matrix[] inputs){
		Matrix[] out = new Matrix[filters.length];
		for(int i = 0; i < inputs.length; i++) {
			out[i] = filters[i].maxPool(inputs[i]);
		}
		return out;
	}

}
