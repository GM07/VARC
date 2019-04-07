package convolutional.neural.network;

import java.io.Serializable;

import math.Matrix;
/**
 * La couche du reseau dont l'operation est la convolution
 * @author Simon Daze
 * @author Gaya Mehenni
 */
public class ConvolutionLayer extends CNNLayer implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8927458212916690704L;
	private int filterSize;
	protected Matrix[] biases;

	/**
	 * Constructeur d'une laye de convolution
	 * @param size : la taille des filters (dimensions)
	 * @param nbFilter : le nombre de filters
	 */
	public ConvolutionLayer(int size, int nbFilter) {

		filterSize = size;
		filters = new Filter[nbFilter];

		// Initialisation des filters
		for(int i = 0; i < nbFilter; i++ ) {
			filters[i] = new Filter(filterSize, filterSize);
		}

		// Initialisation des biases
		initBiases();
	}


	/**
	 * L'operation qui est effectue sur les inputs de cette couche : la convolution 
	 */
	@Override

	public Matrix[] operation() {
		Matrix[] out = new Matrix[filters.length];

		for(int i = 0 ; i < filters.length ; i++ ) {

			/*
			OUTPUTSIZE = INPUTSIZE - FILTERSIZE + 1
			 */

			out[i] = new Matrix(inputs[0].getCOLS() - filters[0].getCOLS() + 1,inputs[0].getCOLS() - filters[0].getCOLS() + 1);

			for(int j = 0; j < this.inputs.length ; j++) {
				out[i] = Matrix.sum(out[i], (filters[i].convolution(inputs[j])));
			}

			out[i].add(biases[i]);
			out[i].applyFunction(activationFunction);

		}

		return out;
	}

	/**
	 * Methode d'entrainement du reseau
	 */
	public void backPropagation() {

	}
	
	/**
	 * Methode qui initialise les biais 
	 */
	public void initBiases() {
		biases = new Matrix[filters.length];
		for( int i = 0 ; i < biases.length ; i++) {
			biases[i] = new Matrix(inputs[0].getCOLS()-filters[0].getCOLS()+1,inputs[0].getCOLS()-filters[0].getCOLS()+1); 
			biases[i].initWithRandomValues(-1,1);
		}
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