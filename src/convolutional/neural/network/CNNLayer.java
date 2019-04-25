package convolutional.neural.network;

import java.io.Serializable;
import java.util.ArrayList;

import functions.ActivationFunctions;
import math.Matrix;
import neural.network.LayerType;

/**
 * Classe contenant les methodes communes a toutes les layers en plus de leurs caracteristiques
 * @author simon Daze
 *@author Gaya Mehenni
 */
public abstract class CNNLayer implements Serializable {
	
	private static final long serialVersionUID = -4404518561044873277L;
	protected Matrix[] inputs ; 
	protected Matrix[] outputs;
	protected Filter[] filters;
	protected double learningRate;

	/**
	 * constructeur du reseau
	 */
	//Simon Daze
	public CNNLayer() {

		filters = new Filter[1];
		filters[0] = new Filter(0, 0);

	}

	/**
	 * Methode qui passe les inputs a travers la couche
	 * @return une matrice des outputs
	 */
	//Simon Daze
	public abstract Matrix[] operation();

	/**
	 * Methode qui retourne l'erreur de la couche et qui entraine les couches en fonction de l'erreur qui lui est donnee
	 * @param target erreur de la couche d'apres
	 * @return erreur de la couche actuelle
	 */
	//Gaya Mehenni
	public abstract Matrix[] backpropagation(Matrix[] target);

	/**
	 * permet d'acceder a la matrice des inputs
	 * @return le tableau de matrices des inputs
	 */
	//Simon Daze
	public Matrix[] getInputs() {
		return inputs;
	}


	/**
	 * permet de modifier le tableau de matrices de inputs
	 * @param inputs : le tableau de matrices a mettre comme input
	 */
	//Simon Daze
	public void setInputs(Matrix[] inputs) {
		this.inputs = inputs;
	}


	/**
	 * permet d'obtenir le tableau de matrices des outputs
	 * @return le tableau de matrices des outputs
	 */
	//Simon Daze
	public Matrix[] getOutputs() {
		return outputs;
	}

	/**
	 * methode qui permet de definir de definir un nouveau tableau de matrices comme inputs
	 * @param outputs le tableau de matrices a definir comme output
	 */
	//Simon Daze

	public void setOutputs(Matrix[] outputs) {
		this.outputs = outputs;
	}

	/**
	 * Methode qui retourne le tableau des filtres d'une layer
	 * @return le tableau des filtres
	 */
	//Simon Daze
	public Filter[] getFilters() {
		return filters;
	}

	/**
	 * Methode pour definir de nouveaux filtres
	 * @param filters le tableau contenant les nouveaux filtres a definir
	 */
	//Simon Daze
	public void setFilters(Filter[] filters) {
		this.filters = filters;
	}

	/**
	 * Methode qui retourne le taux d'apprentissage du resau
	 * @return taux d'apprentissage
	 */
	//Simon Daze
	public double getLearningRate() {
		return learningRate;
	}

	/**
	 * Methode qui change le taux d'apprentissage du reseau
	 * @param learningRate taux d'apprentissage
	 */
	//Simon Daze
	public void setLearningRate(double learningRate) {
		this.learningRate = learningRate;
	}


	/**
	 * Mehode qui ecrit le reseau sous forme de texte
	 *@return s : le reseau sous forme de texte
	 */
	//Gaya Mehenni
	public String toString() {

		String s = getClass().getName() + "\n\n" ;

		s += "INPUTS : \n";
		for(int i = 0; i < inputs.length; i++) {
			s += "\tinput : " + i + "->" + inputs[i].getMatrixSize() + "\n\t" + inputs[i].toString();
		}

		s += "FILTERS : \n";

		for(int i = 0; i < filters.length; i++) {
			s += "\tfilter : " + i + "->" + filters[i].getMatrixSize() + "\n\t" + filters[i].toString();
		}

		s += "OUTPUTS : \n";
		for(int i = 0; i < outputs.length; i++) {
			s += "\toutput : " + i + "->" + outputs[i].getMatrixSize() + "\n\t" + outputs[i].toString();
		}

		return s;
	}
}
