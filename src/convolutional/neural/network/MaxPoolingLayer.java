package convolutional.neural.network;

import math.Matrix;

public class MaxPoolingLayer extends CNNLayer {

	protected double stride;
	protected int filterSize;
	protected int poolingSize;

	public void MaxPoolingLayer(int size, int nbFilters, int st, int poolSize) {
		filterSize = size;
		filters = new Filter[nbFilters];
		stride = st;
		poolingSize = poolSize;
		for(int i = 0; i < nbFilters; i++ ) {
			filters[i] = new Filter(filterSize,filterSize);
		}
	}

	public Matrix[] operation(Matrix[] inputs){
		Matrix[] out = new Matrix[filters.length];
		for(int i = 0; i < inputs.length; i++) {
			out[i] = inputs[i].maxPool(poolingSize, stride);
		}
		return out;
	}

}
