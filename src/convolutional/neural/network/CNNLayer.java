package convolutional.neural.network;

import java.io.Serializable;

import math.Matrix;
import neural.network.LayerType;

public abstract class CNNLayer implements Serializable {
	protected Matrix[] inputs ; 
	protected Matrix[] outputs;
	protected Filter[] filters;
	
	public CNNLayer() {}

	/**
	 * L'operation specifique a certaines layer
	 * @param inputs les inputs de l'operation
	 * @return une matrice des outputs
	 */
	public abstract Matrix[] operation();

	public void feedforward() {
		operation();
	}

	/**
	 * Methode qui va me donner envie d'exploser mon ordi durant la semaine de relache...
	 * Sur une note plus serieuse : methode pour calibrer les weights du reseau
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

	
	
	
	
	

}
