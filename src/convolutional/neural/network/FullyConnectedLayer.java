package convolutional.neural.network;

import functions.ActivationFunctions;
import math.Matrix;
import neural.network.LayerType;
/**
 * correspond à une layer de reseau de neurones fully connected
 * @author Gaya Mehenni
 *
 */
public class FullyConnectedLayer extends CNNLayer {

	private int NB_NEURONS, NB_INPUT_NEURONS;
	private Matrix weights, outputs, outputsZ, inputs, biases, errors;
	private ActivationFunctions function;
	private LayerType type;

	public FullyConnectedLayer() {}

	public FullyConnectedLayer(int nbNeurons, int inputNeurons, LayerType type, ActivationFunctions function) {

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
	 * Calculates the output of the layer
	 */
	public void feedForward() {

		if (type != LayerType.InputLayer) {
			outputsZ = Matrix.sum(weights.multiply(inputs), biases);
			outputs = outputsZ.applyFunction(function);
		} else {
			outputsZ = inputs;
			outputs = outputsZ;
		}
		//outputs = outputsZ.applyFunction(function);
	}

	/**
	 * Adujsts the weights and the biases with the errors
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

	public String toString() {

		String s = "";

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
	// il faudra lineraliser les derniers outputs pour les mettre en 2 dimensions et non 3
	/*public Matrix getOutputs() {
		return outputs;
	}*/

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
	/* Il faudra lineariser les inputs pour les transformer en 2 dimensions au lieu de 3
	public Matrix getInputs() {
		return inputs;
	}*/

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

	public void setNB_NEURONS(int NB_NEURONS) {
		this.NB_NEURONS = NB_NEURONS;
	}

	public void setNB_INPUT_NEURONS(int NB_INPUT_NEURONS) {
		this.NB_INPUT_NEURONS = NB_INPUT_NEURONS;
	}

	@Override
	public Matrix[] operation() {
		// TODO Auto-generated method stub
		return null;
	}

}
