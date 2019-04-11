package convolutional.neural.network;

import java.io.Serializable;

import functions.ActivationFunctions;
import functions.SoftmaxFunction;
import math.MathTools;
import math.Matrix;
import neural.network.Layer;
import neural.network.LayerType;
import neural.network.NeuralNetwork;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Classe qui correspond à une layer de reseau de neurones fully connected
 * @author Gaya Mehenni
 *
 */
public class FullyConnectedLayer extends CNNLayer implements Serializable{

	private ActivationFunctions function;
	private Matrix weights;
	private Matrix biases;
	private Matrix variationWeights;
	private Matrix variationBiases;

	public FullyConnectedLayer(ActivationFunctions function, int numberInputs, int numberOutputs, double learningRate) {

		this.function = function;
		this.learningRate = learningRate;

		inputs = new Matrix[1];
		inputs[0] = new Matrix(numberInputs, 1);

		outputs = new Matrix[1];
		outputs[0] = new Matrix(numberOutputs, 1);

		weights = new Matrix(outputs[0].getROWS(), inputs[0].getROWS());
		weights.initWithRandomValues(-0.5, 0.5);

		biases = new Matrix(outputs[0].getROWS(), 1);
		biases.initWithRandomValues(0, 0);

		variationWeights = new Matrix(outputs[0].getMat().length, inputs[0].getMat().length);
		variationBiases = new Matrix(outputs[0].getMat().length, 1);
	}

	/**
	 * Methode qui passe les inputs a travers la couche
	 * @return une matrice des outputs
	 */
	@Override
	public Matrix[] operation() {

		if (inputs != null) {

			outputs[0] = Matrix.sum(weights.multiply(inputs[0]), biases).applyFunction(function);
			if (outputs == null) {
				System.out.print("L'operation de la couche n'a pas pu etre effectuee : " + getClass().getName());
			}
			return outputs;

		} else {
			System.out.print("Les entrees de la couche n'existe pas : " + getClass().getName());
			return null;
		}
	}

	/**
	 * Methode qui entraine la couche
	 * @param target valeur attendue
	 */
	public Matrix[] backpropagation(Matrix[] target) {

		Matrix deltaY = new Matrix(outputs[0].getROWS(), 1);

		for(int i = 0; i < deltaY.getROWS(); i++) {
			for(int j = 0; j < deltaY.getCOLS(); j++) {

				double value = outputs[0].getElement(i, 0) - target[0].getElement(i, 0) * function.getDerivative().getValue(outputs[0].getElement(i, 0));
				deltaY.setElement(i, j, value);
			}
		}

		// Calcul de la variation des poids
		for(int i = 0; i < variationWeights.getROWS(); i++) {
			for(int j = 0; j < variationWeights.getCOLS(); j++) {

				double value = deltaY.getElement(i, 0) * inputs[0].getElement(j, 0);
				variationWeights.setElement(i, j, value);
			}
		}

		// Calcul de la variation des biais
		for(int i = 0; i < variationBiases.getROWS(); i++) {
			double value = deltaY.getElement(i, 0);
			variationBiases.setElement(i,0, value);
		}

		updateWeightsAndBiases();

		Matrix[] error = new Matrix[1];
		error[0] = new Matrix(inputs[0].getROWS(), 1);

		for(int j = 0; j < error[0].getROWS(); j++) {

			double sum = 0;
			for(int i = 0; i < deltaY.getROWS(); i++) {
				sum += deltaY.getElement(i, 0) * weights.getElement(i, j);
			}
		}

		//error[0] = weights.transpose().multiply(deltaY);

		return error;

	}

	/**
	 * Methode qui met a jour les poids et les biais
	 */
	public void updateWeightsAndBiases() {

		weights.subtract(variationWeights.scalarProduct(learningRate));
		biases.subtract(variationBiases.scalarProduct(learningRate));
	}

	public String toString() {

		String s = getClass().getName() + "\n\n" ;

		for(int i = 0; i < outputs[0].getROWS(); i++) {

			s += "NEURON (" + i + ") : " + outputs[0].getElement(i, 0);

			for(int j = 0; j < inputs[0].getROWS(); j++) {

				s += "\n";
				s += "\t\tPREV NEURON " + j + " : " + inputs[0].getElement(j, 0) + " -----> W = " + weights.getElement(i, j);
			}

			s += "\n\tB = " + biases.getElement(i, 0) + "\n";

		}

		return s;
	}

	/**
	 * Methode qui retourne la fonction d'activation du reseau
	 * @return fonction d'activation
	 */
	public ActivationFunctions getFunction() {
		return function;
	}

	/**
	 * Methode qui change la fonction d'activation du reseau
	 * @param function fonction d'activation
	 */
	public void setFunction(ActivationFunctions function) {
		this.function = function;
	}

	/**
	 * Methode de test face a la couche completement connectee
	 * @param args
	 */
	public static void main(String[] args) {

		FullyConnectedLayer fc = new FullyConnectedLayer(ActivationFunctions.Sigmoid,  5, 1, 0.03);

		Matrix[] i = new Matrix[1];
		i[0] = new Matrix(5, 1, 4);

		Matrix[] i2 = new Matrix[1];
		i2[0] = new Matrix(5, 1, 2);

		fc.setInputs(i);

		double[] in = {1, 0, 0, 1};

		double[] out = {1};
		double[][] a = new double[1][0];
		a[0] = out;

		Matrix[] b = {
				new Matrix(a)
		};

		fc.operation();

		System.out.println(fc);

		fc.setInputs(i2);
		fc.operation();

		System.out.println(fc);

		//fc.backpropagation(b);


	}
}
