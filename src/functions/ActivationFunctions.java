package functions;


import math.Matrix;

import java.io.Serializable;

/**
 * Enumeration qui prend en compte les fonctions d'activations que le reseau de neurones peut prendre en compte
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
		 * Methode qui retourne la derivee de la fonction
		 * @return fonction derivee
		 */
		public DerivativeFunctions getDerivative() {
			return DerivativeFunctions.Linear;
		}

	},
	
	Sigmoid {

		/**
		 * Methode qui donne la valeur f(x) pour un certain x
		 * @param x valeur
		 * @return y de la fonction
		 */
		public double getValue(double x) { return (1.0 / (1.0 + Math.pow(Math.E, -x))); }

		/**
		 * Methode qui retourne la derivee de la fonction
		 * @return fonction derivee
		 */
		public DerivativeFunctions getDerivative() {
			return DerivativeFunctions.Sigmoid;
		}
	},
	
	Tanh {

		/**
		 * Methode qui donne la valeur f(x) pour un certain x
		 * @param x valeur
		 * @return y de la fonction
		 */
		public double getValue(double x) {
			return (2 / (1 + Math.pow(Math.E, -2 * x)));
		}

		/**
		 * Methode qui retourne la derivee de la fonction
		 * @return fonction derivee
		 */
		public DerivativeFunctions getDerivative() {
			return DerivativeFunctions.Tanh;
		}

	},
	
	ReLU {

		/**
		 * Methode qui donne la valeur f(x) pour un certain x
		 * @param x valeur
		 * @return y de la fonction
		 */
		public double getValue(double x) {
			if (x >= 0) return x;
			else return x/3.0;
		}

		/**
		 * Methode qui retourne la derivee de la fonction
		 * @return fonction derivee
		 */
		public DerivativeFunctions getDerivative() {
			return DerivativeFunctions.ReLU;
		}

	},

	Softmax {

		public Matrix inputs;
		public Matrix outputs;
		

		public void setInputs(Matrix i) {
			inputs = i;
		}

		public void setOutputs(Matrix o) {
			outputs = o;
		}
		public double getValue(double x) {
			return x;
		}

		public double getValueInput(int index) {

			if (inputs != null) {

				double eSum = 0;
				for (int i = 0; i < inputs.getROWS(); i++) {
					eSum += Math.exp(inputs.getElement(i, 0));
				}

				return (Math.exp(inputs.getElement(index, 0)) / eSum);
			} else {
				System.out.print("La matrice des valeurs n'a pas ete implementee");
				return 0;
			}
		}
		public double getValueOutput(int index) {

			if (inputs != null) {

				double eSum = 0;
				for (int i = 0; i < outputs.getROWS(); i++) {
					eSum += Math.exp(outputs.getElement(i, 0));
				}

				return (Math.exp(outputs.getElement(index, 0)) / eSum);
			} else {
				System.out.print("La matrice des valeurs n'a pas ete implementee");
				return 0;
			}
		}

		public DerivativeFunctions getDerivative() {
			return DerivativeFunctions.Softmax;
		}


	};


}
