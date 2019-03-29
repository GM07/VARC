package convolutional.neural.network;

import math.Matrix;

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
	
	

}
