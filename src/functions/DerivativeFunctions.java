package functions;

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

	}
	
}
