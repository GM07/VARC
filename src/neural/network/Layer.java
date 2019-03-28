package neural.network;

import functions.ActivationFunctions;
import functions.SoftmaxFunction;
import math.Matrix;

import java.io.Serializable;

/**
 * Classe representant une couche dans le reseau de neurones
 * @author Gaya Mehenni
 */
public class Layer implements Serializable{

	private static final long serialVersionUID = -6865142233942855068L;
	private int NB_NEURONS, NB_INPUT_NEURONS;
	private Matrix weights, outputs, outputsZ, inputs, biases, errors;
	private ActivationFunctions function;
	private LayerType type;

	/**
	 * Constructeur nul pour la sauvegarde xml
	 */
	public Layer() {}

	/**
	 * Constructeur
	 * @param nbNeurons nombre de neurones de la couche
	 * @param inputNeurons nombre de neurones en entree
	 * @param type type de couche
	 * @param function fonction d'activation de la couche
	 */
	public Layer(int nbNeurons, int inputNeurons, LayerType type, ActivationFunctions function) {

		NB_NEURONS = nbNeurons;
		NB_INPUT_NEURONS = inputNeurons;
		this.function = function;
		this.type = type;

		// Weights (to, from) 
		weights = new Matrix(NB_NEURONS, NB_INPUT_NEURONS);

		// Biases (
		biases = new Matrix(NB_NEURONS, 1);

		// Outputs
		outputs = new Matrix(NB_NEURONS, 1);

		// Outputs
		outputsZ = new Matrix(NB_NEURONS, 1);

		// Inputs
		inputs = new Matrix(NB_INPUT_NEURONS, 1);

		// Errors
		errors = new Matrix(NB_NEURONS, 1);

		// Init both matrices with random values between -1 and 1
		if (type != LayerType.InputLayer) {
			weights.initWithRandomValues(-1, 1);
			biases.initWithRandomValues(-1, 1);
		} else {
			weights = new Matrix(NB_NEURONS, NB_INPUT_NEURONS, 1);
			weights = new Matrix(NB_NEURONS, NB_INPUT_NEURONS, 0);
		}
		
		if (type == LayerType.InputLayer) {
			outputs = inputs;
			outputsZ = outputs;
		}
	}

	/**
	 * Methode qui calcul les sorties de la couche
	 */
	public void feedForward() {

		if (type != LayerType.InputLayer) {
			outputsZ = Matrix.sum(weights.multiply(inputs), biases);


			if (function == ActivationFunctions.Softmax) {

				SoftmaxFunction sf = new SoftmaxFunction();
				Matrix o = new Matrix(outputsZ.getROWS(), outputsZ.getCOLS());
				for(int i = 0; i < outputsZ.getROWS(); i++) {
					o.setElement(i, 0, sf.getValue(outputsZ, i));
				}

				outputs = o;

			} else {
				outputs = outputsZ.applyFunction(function);
			}
		} else {
			outputsZ = inputs;
			outputs = outputsZ;
		}
		//outputs = outputsZ.applyFunction(function);
	}
	
	/**
	 * Methode qui ajuste les poids et les biais de la couche en fonction de l'erreur de la couche
	 * @param learningRate
	 */
	public void backpropagation(double learningRate) {
		
		if (type != LayerType.InputLayer) {

			// Remove outputs.applyFunctionDerivative
			Matrix variation = errors.multiply(inputs.transpose());//errors.product(outputs.applyFunctionDerivative(function)).scalarProduct(-learningRate);
			weights.add(variation.scalarProduct(-learningRate));
			biases.add(errors.scalarProduct(-learningRate));

			/*
			for(int neuron = 0; neuron < NB_NEURONS; neuron++) {

				for(int prevNeuron = 0; prevNeuron < NB_INPUT_NEURONS; prevNeuron++) {

					weights.setElement(neuron, prevNeuron, weights.getElement(neuron, prevNeuron) - learningRate * errors.getElement(neuron, 0) * inputs.getElement(prevNeuron, 0));
					biases.setElement(neuron, 0, biases.getElement(neuron, 0) - learningRate * errors.getElement(neuron, 0));
				}

			}
			*/
		}
	}

	/**
	 * Methode qui affiche la couche dans la console
	 * @return chaine de caractere qui represente la couche
	 */
	public String toString() {

		String s = this.function.getClass().getName() + "";

		if (type != LayerType.InputLayer) {

			for(int i = 0; i < NB_NEURONS; i++) {

				s += "NEURON (" + i + ") : " + outputs.getElement(i, 0);

				for(int j = 0; j < NB_INPUT_NEURONS; j++) {

					s += "\n";
					s += "\t\tPREV NEURON " + j + " : " + inputs.getElement(j, 0) + " -----> W = " + weights.getElement(i, j);
				}
				
				s += "\n\tB = " + biases.getElement(i, 0) + "\n";

			}
		} else {

			for(int i = 0; i < NB_NEURONS; i++) {

				s += "\n\tNEURON (" + i + ") : " + outputs.getElement(i, 0);
			}
		}

		return s + "\n";
	}

	/**
	 * Returns the weights of the layer (from the inputs to the outputs)
	 * @return
	 */
	public Matrix getWeights() {
		return weights;
	}

	/**
	 * Sets the weights of the layer
	 * @param weights
	 */
	public void setWeights(Matrix weights) {
		this.weights = weights;
	}

	/**
	 * Returns the output of the layer
	 * @return
	 */
	public Matrix getOutputs() {
		return outputs;
	}

	/**
	 * Sets the outputs of the layer
	 * @param outputs
	 */
	public void setOutputs(Matrix outputs) {
		this.outputs = outputs;
	}

	/**
	 * Returns a matrix of the outputs before they have been put in the activation function
	 * @return
	 */
	public Matrix getOutputsZ() {
		return outputsZ;
	}

	/**
	 * Returns the biases of the layer
	 * @return
	 */
	public Matrix getBiases() {
		return biases;
	}

	/**
	 * Sets the biases of the layer
	 * @param biases
	 */
	public void setBiases(Matrix biases) {
		this.biases = biases;
	}

	/**
	 * Returns the error of that layer
	 * @return
	 */
	public Matrix getErrors() {
		return errors;
	}

	/**
	 * Sets the error of that layer
	 * @param errors
	 */
	public void setErrors(Matrix errors) {
		this.errors = errors;
	}

	/**
	 * Returns the inputs of that layer
	 * @return
	 */
	public Matrix getInputs() {
		return inputs;
	}

	/**
	 * Sets the input of the layer
	 * @param input
	 */
	public void setInputs(Matrix input) {
		this.inputs = input;
	}

	/**
	 * Returns the type of the layer
	 * @return
	 */
	public LayerType getType() {
		return type;
	}

	/**
	 * Sets the type of the layer
	 * @param type
	 */
	public void setType(LayerType type) {
		this.type = type;
	}

	/**
	 * Returns the activation function
	 * @return
	 */
	public ActivationFunctions getFunction() {
		return function;
	}

	/**
	 * Sets the activation function
	 * @param function
	 */
	public void setFunction(ActivationFunctions function) {
		this.function = function;
	}

	/**
	 * Returns the number of neurons in the layer
	 * @return
	 */
	public int getNB_NEURONS() {
		return NB_NEURONS;
	}

	/**
	 * Returns the number of neurons in the previous layer
	 * @return
	 */
	public int getNB_INPUT_NEURONS() {
		return NB_INPUT_NEURONS;
	}

	/**
	 * Methode qui change le nombre de neurones dans la couche
	 * @param NB_NEURONS nombre de neurones
	 */
	public void setNB_NEURONS(int NB_NEURONS) {
		this.NB_NEURONS = NB_NEURONS;
	}

	/**
	 * Methode qui change le nombre de neurones en entree
	 * @param NB_INPUT_NEURONS nombre de neurones en entree
	 */
	public void setNB_INPUT_NEURONS(int NB_INPUT_NEURONS) {
		this.NB_INPUT_NEURONS = NB_INPUT_NEURONS;
	}

	/**
	 * Methode qui change la valeur des activations de la couche
	 * @param outputsZ matrice des activations
	 */
	public void setOutputsZ(Matrix outputsZ) {
		this.outputsZ = outputsZ;
	}
}
