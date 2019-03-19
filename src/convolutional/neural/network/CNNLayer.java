package convolutional.neural.network;

import java.io.Serializable;
import java.util.ArrayList;

import functions.ActivationFunctions;
import math.Matrix;
import neural.network.LayerType;

/**
 * Classe contenant les methodes communes a toutes les layers en plus de leurs caracteristiques
 * @author simon Daze
 *
 */
public abstract class CNNLayer implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -4404518561044873277L;
	protected Matrix[] inputs ; 
	protected Matrix[] outputs;
	protected Filter[] filters;
	protected ActivationFunctions function;
	

	public CNNLayer() {}

	/**
	 * L'operation specifique a certaines layer
	 * @return une matrice des outputs
	 */
	public abstract Matrix[] operation();

	public void feedforward() {
		operation();
	}

	/**
	 *Methode d'entraienement du reseau
	 */
	public void backPropagation() {

	}
	/**
	 * permet d'acceder a la matrice des inputs
	 * @return le tableau de matrices des inputs
	 */
	public Matrix[] getInputs() {
		return inputs;
	}


	/**
	 * permet de modifier le tableau de matrices de inputs
	 * @param inputs : le tableau de matrices a mettre comme input
	 */
	public void setInputs(Matrix[] inputs) {
		this.inputs = inputs;
	}


	/**
	 * permet d'obtenir le tableau de matrices des outputs
	 * @return le tableau de matrices des outputs
	 */
	public Matrix[] getOutputs() {
		return outputs;
	}

	/**
	 * methode qui permet de definir de definir un nouveau tableau de matrices comme inputs
	 * @param outputs le tableau de matrices a definir comme output
	 */

	public void setOutputs(Matrix[] outputs) {
		this.outputs = outputs;
	}

	/**
	 * Methode qui retourne le tableau des filtres d'une layer
	 * @return le tableau des filtres
	 */
	public Filter[] getFilters() {
		return filters;
	}


	/**
	 * methode pour definir de nouveaux filtres
	 * @param filters le tableau contenant les nouveaux filtres a definir
	 */
	public void setFilters(Filter[] filters) {
		this.filters = filters;
	}

	/**
	 * Linearise les outputs sous forme de vecteurs pour les utiliser comme inputs d'une FullyConnectedLayer
	 * @return le vecteur contenant les outputs
	 */
	public Matrix linearizeV1() {
		int linearizedMatrixSize = 0;
		Matrix linearizedOutputs;
		ArrayList<Double> arrayOutput  = new ArrayList<Double>();
		for (int f = 0 ; f < outputs.length; f++ ) {
			for (int i = 0 ; i < outputs[f].getROWS(); i++) {
				for (int j = 0 ; j < outputs[f].getCOLS(); j++) {
					arrayOutput.add(outputs[f].getElement(i, j));
					linearizedMatrixSize += 1;
				}
			}
		}
		linearizedOutputs = new Matrix(1,linearizedMatrixSize);
		for(int i = 0; i < linearizedMatrixSize ; i++) {
			linearizedOutputs.setElement(0, i, arrayOutput.get(i));
		}
		System.out.println(linearizedOutputs);
		return linearizedOutputs;

	}


	/**
	 * Linearise les outputs sous forme de vecteurs pour les utiliser comme inputs d'une FullyConnectedLayer
	 * Cette version additionne toutes les matrices des outputs ensemble avant de les lineariser
	 * @return le vecteur contenant les outputs
	 */
	public Matrix linearizeV2() {
		int linearizedMatrixSize = 0;
		Matrix linearizedOutputs;
		Matrix intermediateMatrix = new Matrix();
		ArrayList<Double> arrayOutput  = new ArrayList<Double>();
		for (int f = 0 ; f < outputs.length; f++ ) {
			intermediateMatrix.add(outputs[f]);
		}
		for (int i = 0; i < intermediateMatrix.getROWS(); i++) {
			for (int j = 0; j < intermediateMatrix.getCOLS(); j++) {
				arrayOutput.add(intermediateMatrix.getElement(i, j));
			}
		}
		linearizedOutputs = new Matrix(0,linearizedMatrixSize);
		for(int i = 0; i < linearizedMatrixSize; i++) {
			linearizedOutputs.setElement(0, i, arrayOutput.get(i));
		}
		System.out.println(linearizedOutputs);
		return linearizedOutputs;
	}





}
