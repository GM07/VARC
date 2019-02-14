package functions;

import java.io.Serializable;

/*
 * @author Gaya Mehenni
 * @author Simon Daze
 */

public enum ActivationFunctions implements ActivationFunctionsInterface,  Serializable {
	
	Linear {
		
		/**
		 * @param	x	value
		 * @return		output of the function
		 */
		public double getValue(double x) {
			return x;
		}
		
		/**
		 * @return		derivative of the function
		 */
		public DerivativeFunctions getDerivative() {
			return DerivativeFunctions.Linear;
		}
	},
	
	Sigmoid {
		
		/**
		 * @param	x	value
		 * @return		output of the function
		 */
		public double getValue(double x) {
			return (1.0 / (1.0 + Math.pow(Math.E, -x)));
		}
		
		/**
		 * @return		derivative of the function
		 */
		public DerivativeFunctions getDerivative() {
			return DerivativeFunctions.Sigmoid;
		}
	},
	
	Tanh {
		
		/**
		 * @param	x	value
		 * @return		output of the function
		 */
		public double getValue(double x) {
			return (2 / (1 + Math.pow(Math.E, -2 * x)));
		}
		
		/**
		 * @return		derivative of the function
		 */
		public DerivativeFunctions getDerivative() {
			return DerivativeFunctions.Tanh;
		}
	},
	
	ReLU {
		/**
		 * @param	x	value
		 * @return		output of the function
		 */
		public double getValue(double x) {
			if (x >= 0) return x;
			else return 0;
		}
		
		/**
		 * @return		derivative of the function
		 */
		public DerivativeFunctions getDerivative() {
			return DerivativeFunctions.ReLU;
		}
	}

}
