package functions;

import math.Matrix;

/**
 * Classe de classification a l'aide d'une fonction qui tansforme des outputs en probabilites 
 * @author Simon Daze
 * 
 *
 */
public class SoftmaxFunction {


	//public double getValue(double x){return x;}
	//public DerivativeFunctions getDerivative() {return null;}

	/**
	 * Applique la fonction softmax a un vecteur pour en ressortir des probabilites
	 * @param logits le vecteur 
	 * @param index l'element du vecteur a evalue
	 * @return la probabilite associee a l'element du vecteur en index
	 */
	public double getValue(Matrix logits , int index) {
		double eSum = 0;
		double d =  logits.getMaxValue();
		for (int  i = 0 ; i < logits.getROWS(); i++) {
			eSum += Math.exp(logits.getElement(i, 0) - d);
		}
		
		return (Math.exp(logits.getElement(index, 0) - d )/ eSum);
	}
	
	/**
	 * Evalue la derivee dela fonction softmax pour un element d'un vecteur
	 * @param index l'index de l'element ou la derivee est evaluee
	 * @param logits l'element du vecteur
	 * @param results la probabilite associee a cet element
	 * @return la derivee de softmax relative a cet element
	 */
	public double getDerivative(int index, Matrix logits, Matrix results) {
		return (getValue(results,index) * (1 - getValue(logits,index)));
	}
	 
	/**
	 *  Fonction du cout de softmax pour la classification entre 2 outputs
	 * @param output la probabilite obtenue
	 * @param expected la probabilite souhaitee
	 * @return la regression entre la valeur souhaitee et celle obtenue
	 */
	public double crossEntropy(double output,double expected) {
		if(output == 1) {
			return -Math.log(expected);
		}else {
			return -Math.log(1-expected);
		}
		
	}
	
	/**
	 * Fonction du cout de softmax lors de la classification de plus de 2 categories
	 * @param outputs la matrice des outputs
	 * @param expected la matrice des resultats souhaites
	 * @return la regression entre le resultat souhaite et obtenu
	 */
	
	public double crossEntropyLoss( Matrix outputs, double[] expected) {
		double sum = 0;
		for (int i = 0 ; i < outputs.getROWS(); i++) {
			sum += expected[i]*Math.log(outputs.getElement(i, 0) );
		}
		return -1*sum;
	}
	
	/**
	 * Derivee de la fonction crossEntropy
	 * @param expected valeur attendue
	 * @param logits matrice des outputs
	 * @param index l'index de la valeur evaluee par rapport a la valeur attendue
	 * @return
	 */
	public double lostFunctionDerivative(double expected, Matrix logits, int index) {
		return (getValue(logits,index) - expected);
		
	}
	
	/**
	 * regression de la fonction softmax pour un reseau de neurones en restreignant la taille des weights
	 * @param outputs : le output du reseau
	 * @param expected : le output souhaite
	 * @param index : le neurone sur lequel la regression est appliquee
	 * @return : l'erreur du neurone
	 */
	public double weightRegression(Matrix outputs, double[] expected, int index) {
		double m = -1.0 / outputs.getROWS();
		double decay = 0;
		double x = 0;
		double wDecay = 10;
		for  (int i = 0 ; i < outputs.getROWS() ; i++) {
			if (i == index && expected[i] == 1) {
				x = 1;
			}else {
				x = 0;
			}
			decay += (x - getValue(outputs, i));
		}
		decay = m * decay + wDecay * expected[index];
		
		return decay;
		
	}
	
	/**
	 * test softmax
	 * @param args
	 */
	public static void main(String[] args) {
		SoftmaxFunction s = new SoftmaxFunction();
		Matrix logits = new Matrix(3,1);
		Matrix results = new Matrix(3,1);
		logits.setElement(0, 0, 2);
		logits.setElement(1, 0, 1);
		logits.setElement(2, 0, 0.1);
		results.setElement(0, 0, s.getValue(logits,0));
		results.setElement(1, 0, s.getValue(logits,1));
		results.setElement(2, 0, s.getValue(logits,2));


		System.out.println(logits);
		System.out.println(results);

		//System.out.println(logits);
		//System.out.println(results);
//	/* testDerivative = getDerivative(0,logits,results);
//		System.out.println(testDerivative);
//		*/
	}
		

	
}
