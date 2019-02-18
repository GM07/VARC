package neural.network;

import java.io.Serializable;

import functions.ActivationFunctions;
import math.Matrix;

public class Layer implements Serializable {

	private  int NB_NEURONS, NB_INPUT_NEURONS;
	private Matrix weights, outputs, inputs, biases, errors;
	private ActivationFunctions function;
	private LayerType type;
	
	public Layer() {
		NB_NEURONS = 0;
		NB_INPUT_NEURONS = 0;
	}

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

		// Inputs
		inputs = new Matrix(NB_INPUT_NEURONS, 1);

		// Errors
		errors = new Matrix(NB_NEURONS, 1);

		// Init both matrices with random values between -1 and 1
		weights.initWithRandomValues(-1, 1);
		biases.initWithRandomValues(-1, 1);
		
		if (type == LayerType.InputLayer) outputs = inputs;
	}

	/**
	 * Calculates the output of the layer
	 * @param input
	 */
	public void feedForward() {

		if (type != LayerType.InputLayer) {
			outputs = Matrix.sum(weights.multiply(inputs), biases);
			outputs = outputs.applyFunction(function);
		} else {
			outputs = inputs;
		}
	}
	
	/**
	 * Adujsts the weights and the biases with the errors
	 * @param learningRate
	 */
	public void backpropagation(double learningRate) {
		
		if (type != LayerType.InputLayer) {
			
			for(int neuron = 0; neuron < NB_NEURONS; neuron++) {
				
				for(int prevNeuron = 0; prevNeuron < NB_INPUT_NEURONS; prevNeuron++) {
					
					weights.setElement(neuron, prevNeuron, weights.getElement(neuron, prevNeuron) + learningRate * errors.getElement(neuron, 0) * inputs.getElement(prevNeuron, 0));
					biases.setElement(neuron, 0, biases.getElement(neuron, 0) + learningRate * errors.getElement(neuron, 0));
				}
				
			}
		}
	}

	public String toString() {

		String s = "";

		if (type != LayerType.InputLayer) {

			for(int i = 0; i < NB_NEURONS; i++) {

				s += "NEURON (" + i + ") : " + outputs.getElement(i, 0);

				for(int j = 0; j < NB_INPUT_NEURONS; j++) {

					s += "\n";
					s += " -- PREV NEURON " + j + " : " + inputs.getElement(j, 0) + " -----> W = " + weights.getElement(i, j);
				}
				
				s += "\n -- B = " + biases.getElement(i, 0) + "\n";

			}
		} else {

			for(int i = 0; i < NB_NEURONS; i++) {

				s += "NEURON (" + i + ") : " + outputs.getElement(i, 0) + " ";
			}
		}

		return s;
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


	public int getNB_NEURONS() {
		return NB_NEURONS;
	}

	public int getNB_INPUT_NEURONS() {
		return NB_INPUT_NEURONS;
	}

	public void setNB_NEURONS(int nB_NEURONS) {
		NB_NEURONS = nB_NEURONS;
	}

	public void setNB_INPUT_NEURONS(int nB_INPUT_NEURONS) {
		NB_INPUT_NEURONS = nB_INPUT_NEURONS;
	}
	
	



}
