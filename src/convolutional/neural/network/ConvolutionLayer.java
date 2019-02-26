package convolutional.neural.network;

import math.Matrix;

public class ConvolutionLayer extends CNNLayer {
	int filterSize;



	public ConvolutionLayer(int size, int nbFilter) {

		filterSize = size;
		filters = new Filter[nbFilter];

		for(int i = 0; i < nbFilter; i++ ) {
			filters[i] = new Filter(filterSize,filterSize);
		}
	}

	@Override
	/**
	 * L'operation qui est effectue sur les inputs de cette couche : la convolution 
	 */
	public Matrix[] operation(Matrix[] inputs) {
		Matrix[] out = new Matrix[filters.length];
		for(int i = 0 ; i < inputs.length; i++ ) {
			//out[i] = new Matrix(inputs[i].getCOLS()-filters[i].getCOLS()+1,inputs[i].getCOLS()-filters[i].getCOLS()+1 );
			out[i] = filters[i].convolution(inputs[i]);
		}
		return out;
	}
	
	


	/**
	 * test de la convolution sur une layer
	 * @param args
	 */
	public static void main(String[] args) {
		ConvolutionLayer test = new ConvolutionLayer(3,1);

		Matrix[] inputs = new Matrix[1];
		inputs[0] = new Matrix (6,6);
		inputs[0].initWithRandomValues();
		Filter[] filter = test.getFilters();
		System.out.println(filter[0]);
		System.out.println(test.operation(inputs)[0]);
		

	}
}