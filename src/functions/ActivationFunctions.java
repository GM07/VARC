package functions;

import java.io.Serializable;

/**
 * @author Gaya Mehenni
 */

public enum ActivationFunctions implements ActivationFunctionsInterface, Serializable {
	
	Linear {
		
		/**
		 * Methode qui donne la valeur f(x) pour un certain x
		 * @param x valeur
		 * @return y de la fonction
		 */
		public double getValue(double x) {
			return x;
		}
		
		/**
		 * @return fonction derivee
		 */
		public DerivativeFunctions getDerivative() {
			return DerivativeFunctions.Linear;
		}

	},
	
	Sigmoid {

		/**
		 * @param x valeur
		 * @return y de la fonction
		 */
		public double getValue(double x) { return (1.0 / (1.0 + Math.pow(Math.E, -x))); }

		/**
		 * @return fonction derivee
		 */
		public DerivativeFunctions getDerivative() {
			return DerivativeFunctions.Sigmoid;
		}
	},
	
	Tanh {

		/**
		 * @param x valeur
		 * @return y de la fonction
		 */
		public double getValue(double x) {
			return (2 / (1 + Math.pow(Math.E, -2 * x)));
		}

		/**
		 * @return fonction derivee
		 */
		public DerivativeFunctions getDerivative() {
			return DerivativeFunctions.Tanh;
		}

	},
	
	ReLU {
		/**
		 * @param x valeur
		 * @return y de la fonction
		 */
		public double getValue(double x) {
			if (x >= 0) return x;
			else return 0;
		}

		/**
		 * @return fonction derivee
		 */
		public DerivativeFunctions getDerivative() {
			return DerivativeFunctions.ReLU;
		}

	}

}
