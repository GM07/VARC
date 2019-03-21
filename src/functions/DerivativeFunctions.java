package functions;

import math.Matrix;

import java.io.Serializable;

/**
 * Enumeration des derivees des fonctions d'activation
 * @author Gaya Mehenni
 */

public enum DerivativeFunctions implements DerivativeFunctionsInterface, Serializable {

	Linear {

		/**
		 * Methode qui donne la valeur f(x) pour un certain x
		 * @param x valeur
		 * @return y de la fonction
		 */
		public double getValue(double x) {
			return 1;
		}
	},
	
	Sigmoid {

		/**
		 * Methode qui donne la valeur f(x) pour un certain x
		 * @param x valeur
		 * @return y de la fonction
		 */
		public double getValue(double x) {
			return ActivationFunctions.Sigmoid.getValue(x) * (1 - ActivationFunctions.Sigmoid.getValue(x));
		}
	},
	
	Tanh {

		/**
		 * Methode qui donne la valeur f(x) pour un certain x
		 * @param x valeur
		 * @return y de la fonction
		 */
		public double getValue(double x) {
			return (1 - Math.pow(ActivationFunctions.Tanh.getValue(x), 2));
		}
	},
	
	ReLU {

		/**
		 * Methode qui donne la valeur f(x) pour un certain x
		 * @param x valeur
		 * @return y de la fonction
		 */
		public double getValue(double x) {
			if (x >= 0) return 1;
			else return 0;
		}

	},

	Softmax {

		Matrix outputs;
		Matrix inputs;
		int indexInput;
		int indexOutput;

		public void setInputs(Matrix i) {
			inputs = i;
		}

		public void setOutputs(Matrix o) {
			outputs = o;
		}

		public double getValue(double x) {
			if (indexInput == indexOutput) {
				return (ActivationFunctions.Softmax.getValue(x) * (1 - ActivationFunctions.Softmax.getValue(x)));
			}
		}
	}
	
}
