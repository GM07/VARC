package math;

import java.io.Serializable;
import java.util.Arrays;

/**
 * Cette classe fournit des fonctions utiles a la creation d'un reseau de neurones
 * @author Gaya Mehenni
 */
public class MathTools implements Serializable {
	
	/**
	 * Retourne l'index de l'element le plus grand dans le tableau
	 * @param a tableau
	 * @return index du nombre le plus grand
	 */
	public static int getHighestIndex(double[] a) {
		int index = 0;
		
		for(int i = 1; i < a.length; i++) {
			if (a[index] < a[i]) {
				index = i;
			}
		}
		
		return index;
	}

	/**
	 * Transforme un 2D array en 1D array
	 * @param arr
	 * @return
	 */
	public static double[] getAsOneDimension(double[][] arr) {
		double[] array = new double[arr.length * arr[0].length];
		for(int i = 0; i < arr.length; i ++) {
			
			for(int s = 0; s < arr[i].length; s ++) {
				
				array[(i * arr[0].length) + s] = arr[i][s];
			}
		}

		return array;
	}
	
	/**
	 * Transforme un tableau a une dimension en tableau a deux dimensions
	 * @param inputs tableau a une dimension
	 * @return tableau a deux dimensions
	 */
	public static double[][] getAsTwoDimensionalArray(double[] inputs) {
		double[][] in = new double[inputs.length][1];

		for(int i = 0; i < in.length; i++) {

			for(int j = 0; j < in[i].length; j++) {

				in[i][j] = inputs[i];
			}
		}

		return in;
	}

	/**
	 * Permet de transformer les valeurs d'un tableau contenu dans un certain intervalle a sa valeur dans l'intervalle plus petit
	 * @param in tableau
	 * @param current1 borne inferieure intervalle debut
	 * @param current2 borne superieure intervalle debut
	 * @param target1 borne inferieure intervalle fin
	 * @param target2 borne superieure intervale fin
	 * @return valeur dans l'intervalle de fin
	 */
	public static double[] mapArray(double[] in, double current1, double current2, double target1, double target2) {

		double[] out = new double[in.length];
		for(int i = 0 ;i < in.length; i++) {
			out[i] = target1 + (target2 - target1) * ((in[i] - current1) / (current2 - current1));
		}

		return out;
	}

	/**
	 * Permet de transformer une valeur contenu dans un certain intervalle a sa valeur dans l'intervalle plus petit
	 * @param value valeur
	 * @param c1 borne inferieure intervalle debut
	 * @param c2 borne superieure intervalle debut
	 * @param t1 borne inferieure intervalle fin
	 * @param t2 borne superieure intervale fin
	 * @return valeur dans l'intervalle de fin
	 * @return
	 */
	public static double mapValue(double value, double c1, double c2, double t1, double t2) {
		return t1 + (t2 - t1) * ((value - c1) / (c2 - c1));
	}

}
