package functions;

import math.Matrix;

/**
 * Classe de classification a l'aide d'une fonction qui tansforme des outputs en probabilites 
 * @author Simon Daze
 * 
 *
 */
public class SoftmaxFunction {


	public double getValue(double x){return 0;}
	public DerivativeFunctions getDerivative() {return null;}

	/**
	 * Applique la fonction softmax a un vecteur pour en ressortir des probabilites
	 * @param logits le vecteur 
	 * @param index l'element du vecteur a evalue
	 * @return la probabilite associee a l'element du vecteur en index
	 */
	public double getValue(Matrix logits , int index) {
		double eSum = 0;
		double d = -1 * logits.getMaxValue();
		for (int  i = 0 ; i < logits.getROWS(); i++) {
			eSum+= Math.exp(logits.getElement(i, 0)+d);
		}
		
		return (Math.exp(logits.getElement(index, 0)+d )/ eSum);
	}
	
	/**
	 * Evalue la derivee dela fonction softmax pour un element d'un vecteur
	 * @param index l'index de l'element ou la derivee est evaluee
	 * @param logits l'element du vecteur
	 * @param results la probabilite associee a cet element
	 * @return la derivee de softmax relative a cet element
	 */
	public double getDerivative(int index, Matrix logits, Matrix results) {
		return (getValue(results,index)*(1-getValue(logits,index)));
	}
	
	public static void main(String[] args) {
//		SoftmaxFunction s = new SoftmaxFunction();
//		Matrix logits = new Matrix(3,1);
//		Matrix results = new Matrix(3,1);
//		logits.setElement(0, 0, 2);
//		logits.setElement(1, 0, 1);
//		logits.setElement(2, 0, 0.1);
//		results.setElement(0, 0, getValue(logits,0));
//		results.setElement(1, 0, getValue(logits,1));
//		results.setElement(2, 0, getValue(logits,2));
//		System.out.println(logits);
//		System.out.println(results);
//	/* testDerivative = getDerivative(0,logits,results);
//		System.out.println(testDerivative);
//		*/
	}
		

	
}
