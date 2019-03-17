package convolutional.neural.network;

import java.util.Arrays;

import math.Matrix;
/**
 * La couche du reseau dont l'operation est la convolution
 * @author Simon Daze
 *
 */
public class ConvolutionLayer extends CNNLayer {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8927458212916690704L;
	int filterSize;



	public ConvolutionLayer(int size, int nbFilter) {

		filterSize = size;
		filters = new Filter[nbFilter];


		for(int i = 0; i < nbFilter; i++ ) {
			filters[i] = new Filter(filterSize, filterSize);
		}
	}


	/**
	 * L'operation qui est effectue sur les inputs de cette couche : la convolution 
	 */
	@Override

	public Matrix[] operation() {
		Matrix[] out = new Matrix[filters.length];

		for(int i = 0 ; i < filters.length ; i++ ) {
			out[i] = new Matrix(inputs[0].getCOLS()-filters[0].getCOLS()+1,inputs[0].getCOLS()-filters[0].getCOLS()+1 );


			System.out.println("IN " + Arrays.toString(inputs));
			System.out.println("FIL " + Arrays.toString(filters));





			for(int j = 0; j < this.inputs.length ; j++) {
				out[i] = Matrix.sum(out[i], (filters[i].convolution(inputs[j])));
			}
			//out[i].applyFunction(function.Sigmoid);

		}
		System.out.println("OUT " + Arrays.toString(out));
		return out;
	}

	/**
	 * Methode d'entrainement du reseau
	 */
	public void backPropagation() {

	}



	/**
	 * test de la convolution sur une layer
	 * @param args
	 */
	public static void main(String[] args) {


		Matrix[] inputs = new Matrix[1];
		inputs[0] = new Matrix (6,6);
		inputs[0].initWithRandomValues();
		ConvolutionLayer test = new ConvolutionLayer(3,1);
		test.setInputs(inputs);
		Filter[] filter = test.getFilters();
		System.out.println(filter[0]);
		System.out.println(test.operation()[0]);
		test.setOutputs(test.operation());
		test.linearizeV1();

	}
}