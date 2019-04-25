package convolutional.neural.network;

import java.util.ArrayList;
import java.util.Arrays;
import java.io.Serializable;

import functions.ActivationFunctions;
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

	/*
		Pour chaque filtre (kernel), il y a un tableau de biais lui etant associe
		Formule pour trouver la taille de sortie (o) d'une couche de convolution en fonction de la taille du filtre (f) et de la taille en entree (i) :
		o = i - f + 1
	 */
	private double[] biases;

	private ActivationFunctions activationFunction;

	/**
	 * Constructeur d'une laye de convolution
	 * @param size : la taille des filters (dimensions)
	 * @param nbFilter : le nombre de filters
	 */
	// Gaya Mehenni
	public ConvolutionLayer(int size, int nbFilter, int inputSize, int nbChannels) {
		inputs = new Matrix[nbChannels];

		for(int i = 0; i < inputs.length; i++) {
			inputs[i] = new Matrix(inputSize, inputSize);
		}

		filterSize = size;
		filters = new Filter[nbFilter];
		outputs = new Matrix[nbFilter];

		// Initialisation des sorties
		for(int i = 0; i < outputs.length; i++) {
			int outputSize = inputs[0].getROWS() - size + 1;
			outputs[i] = new Matrix(outputSize, outputSize);
		}

		// Initialisation des filters
		for(int i = 0; i < nbFilter; i++ ) {
			filters[i] = new Filter(filterSize, filterSize);
			filters[i].initWithRandomValues(-0.1, 0.1);
		}

		// Initialisation des biases
		biases = new double[filters.length];
		for( int i = 0 ; i < biases.length ; i++) {
			// Tous les biais sont initialises a zero
			biases[i] = 0;

		}
	}

	/**
	 * Constructeur d'une couche de convolution autre que la premiere couche
	 * @param size taille du filtre
	 * @param nbFilter nombre de filtres
	 */
	// Gaya Mehenni
	public ConvolutionLayer(int size, int nbFilter) {

		filterSize = size;
		filters = new Filter[nbFilter];
		outputs = new Matrix[nbFilter];

//		// Initialisation des sorties
//		for(int i = 0; i < outputs.length; i++) {
//			outputs[i] = new Matrix(inputs[0].getROWS() - size + 1, inputs[0].getROWS() - size + 1);
//		}

		// Initialisation des filters
		for(int i = 0; i < nbFilter; i++ ) {
			filters[i] = new Filter(filterSize, filterSize);
			filters[i].initWithRandomValues(-0.25, 0.25);
		}

		// Initialisation des biases
		biases = new double[filters.length];
		for( int i = 0 ; i < biases.length ; i++) {
			// Tous les biais sont initialises a zero
			biases[i] = Math.random() * 2 - 1;

			//new Matrix(inputs[0].getCOLS() - filters[0].getCOLS() + 1,inputs[0].getCOLS() - filters[0].getCOLS() + 1, 0);
			//biases[i].initWithRandomValues(-1,1);
		}
	}


	/**
	 * L'operation qui est effectue sur les inputs de cette couche : la convolution
	 */
	// Simon Daze
	@Override
	public Matrix[] operation() {

		if (inputs != null) {
			Matrix[] out = new Matrix[filters.length];

			for (int i = 0; i < filters.length; i++) {

				out[i] = new Matrix(inputs[0].getROWS() - filters[0].getROWS() + 1, inputs[0].getROWS() - filters[0].getROWS() + 1);

				for (int j = 0; j < this.inputs.length; j++) {

					out[i] = Matrix.sum(out[i], (filters[i].convolution(inputs[j])));

				}

				out[i].addToElements(biases[i]);
				out[i].applyFunction(activationFunction);

			}

			outputs = out;
			return out;
		} else {
			System.out.println("Les entrees de la couche n'existent pas : " + getClass().getName());
			return null;
		}
	}

	/*
	p = indice de l'image sur laquelle le filter est applique = input
	q = indice du filter qui est applique = filter
	8 = outputsSize = i - f + 1

	 */

	/**
	 * Methode qui entraine la couche de convolution en fonction de l'erreur qui est donnee
	 * @param target erreur de la couche d'apres
	 * @return error de cette couche
	 */
	// Gaya Mehenni
	public Matrix[] backpropagation(Matrix[] target) {

		Matrix[] deltaC = new Matrix[filters.length];
		Matrix[] deltaK = new Matrix[filters.length];
		Matrix[] deltaS = new Matrix[inputs.length];

		for(int input = 0; input < inputs.length; input++) {

			// On parcoure chaque entree
			deltaS[input] = new Matrix(inputs[input].getROWS(), inputs[input].getCOLS());

			for(int filter = 0; filter < filters.length; filter++) {

				// On parcourche chaque filtre
				deltaK[filter] = new Matrix(filters[filter].getROWS(), filters[filter].getCOLS());

				deltaC[filter] = new Matrix(outputs[filter].getROWS(), outputs[filter].getCOLS());
				for(int i = 0; i < deltaC[filter].getROWS(); i++) {
					for(int j = 0; j < deltaC[filter].getCOLS(); j++) {
						deltaC[filter].setElement(i, j, target[filter].getElement(i, j) * outputs[filter].applyFunctionDerivative(activationFunction).getElement(i, j));
					}
				}

				// Calcul de l'erreur de la couche
				//Matrix errorFilter = filters[filter].convolutionWithPadding(target[filter], filters[filter].getROWS() - 1);
				// Probleme
				deltaS[input].add(filters[filter].convolutionWithPadding(deltaC[filter], filters[filter].getROWS() - 1));

				// Calcul de la variation des poids
//				for (int u = 0; u < filters[filter].getROWS(); u++) {
//
//					for (int v = 0; v < filters[filter].getCOLS(); v++) {
//
//						double sum = 0;
//						for (int i = 0; i < target[filter].getROWS(); i++) {
//							for (int j = 0; j < target[filter].getCOLS(); j++) {
//
//								//Matrix outputDerivative = outputs[filter].applyFunctionDerivative(activationFunction);
//								// Math.abs ?
//								sum += target[filter].getElement(i, j) * outputs[filter].applyFunctionDerivative(activationFunction).getElement(i, j) * inputs[input].getElement(i - u, j - v);
//							}
//						}
//
//						deltaK[filter].setElement(u, v, sum);
//					}
//				}

				deltaK[filter] = deltaC[filter].convolution(inputs[input]);

				filters[filter].add(deltaK[filter].scalarProduct(-learningRate));

				// Calcul de la variation des biais
				double sum = 0;
				for (int i = 0; i < target[filter].getROWS(); i++) {
					for (int j = 0; j < target[filter].getCOLS(); j++) {

						sum += target[filter].getElement(i, j);
					}
				}
				biases[filter] += -learningRate * sum;

			}
		}

		/*
		for(int input = 0; input < inputs.length; input++) {

			for(int filter = 0; filter < filters.length; filter++) {

				// On initialise les matrices de variation
				deltaC[filter] = new Matrix(outputs[0].getROWS(), outputs[0].getCOLS());
				deltaK[filter] = new Matrix(filterSize, filterSize);

				// Erreur de la couche
				for(int i = 0; i < target[filter].getROWS(); i++) {
					for(int j = 0; j < target[filter].getCOLS(); j++) {
						double value = target[filter].getElement(i, j) * activationFunction.getDerivative().getValue(outputs[filter].getElement(i, j));
						deltaC[filter].setElement(i, j, value);
					}
				}

				//deltaC[filter] = target[filter].product(outputs[filter].applyFunctionDerivative(activationFunction));

				//deltaK[filter] = inputs[input].convolution(deltaC[filter]);


				// Erreur des filtres
				for(int u = 0; u < filters[filter].getROWS(); u++) {
					for(int v = 0; v < filters[filter].getCOLS(); v++) {

						double sum = 0;
						// PAS SUR DE CA
						for (int i = u; i < outputs[filter].getROWS(); i++) {
							for (int j = v; j < outputs[filter].getCOLS(); j++) {

								sum += target
								//sum += inputs[input].getElement(i - u, j - v) * deltaC[filter].getElement(i, j);
							}
						}

						deltaK[filter].setElement(u, v, sum);
					}
				}

				// On met a jour les poids
				filters[filter].add(deltaK[filter].scalarProduct(-learningRate));

				// Erreurs des biais
				double sum = 0;
				for(int i = 0; i < deltaC[filter].getROWS(); i++) {
					for(int j = 0; j < deltaC[filter].getCOLS(); j++) {
						sum += deltaC[filter].getElement(i, j);
					}
				}

				// On met a jour les biais
				biases[filter] += -sum * learningRate;

				// Erreur de la couche d'avant
				if (input == 0) deltaS[input] = filters[filter].convolutionWithPadding(deltaC[filter], deltaC[filter].getROWS() - 1);
				else deltaS[input] = Matrix.sum(deltaS[input - 1], filters[filter].convolution(deltaC[filter]));

			}


		}
		*/

		//System.out.println("ERROR : " + deltaS);
		return deltaS;
	}

	/**
	 * Methode qui retourne la fonction d'activation de la couche
	 * @return fonction d'activation
	 */
	//Simon Daze
	public ActivationFunctions getFunction() {
		return activationFunction;
	}

	/**
	 * Methode qui change la fonction d'activation
	 * @param function fonction d'activation
	 */
	//Simon Daze
	public void setFunction(ActivationFunctions function) {
		this.activationFunction = function;
	}


	/**
	 * test de la convolution sur une layer
	 * @param args
	 */
	//Simon Daze
	public static void main(String[] args) {

		Matrix[] inputs = new Matrix[1];
		inputs[0] = new Matrix (6,6, 1);
		Matrix[] outputs = new Matrix[2];
		outputs[0] = new Matrix(4, 4, 0.5);
		outputs[1] = new Matrix(4, 4, 0.5);
		ConvolutionLayer test = new ConvolutionLayer(3,2, 6, 1);
		test.setFunction(ActivationFunctions.Linear);
		test.setInputs(inputs);
		System.out.println("FEED " + Arrays.toString(test.operation()));

		System.out.println("BACK " + Arrays.toString(test.backpropagation(outputs)));
	}
}