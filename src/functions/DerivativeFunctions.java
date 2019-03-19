package functions;

import java.io.Serializable;

/**
 * @author Gaya Mehenni
 */


public enum DerivativeFunctions implements DerivativeFunctionsInterface, Serializable {

	Linear {

		/**
		 * @param x valeur
		 * @return y de la fonction
		 */
		public double getValue(double x) {
			return 1;
		}
	},
	
	Sigmoid {

		/**
		 * @param x valeur
		 * @return y de la fonction
		 */
		public double getValue(double x) {
			return ActivationFunctions.Sigmoid.getValue(x) * (1 - ActivationFunctions.Sigmoid.getValue(x));
		}
	},
	
	Tanh {

		/**
		 * @param x valeur
		 * @return y de la fonction
		 */
		public double getValue(double x) {
			return (1 - Math.pow(ActivationFunctions.Tanh.getValue(x), 2));
		}
	},
	
	ReLU {

		/**
		 * @param x valeur
		 * @return y de la fonction
		 */
		public double getValue(double x) {
			if (x >= 0) return 1;
			else return 0;
		}

	}
	
}
